package org.eclipse.emf.ecp.ecore.editor.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.ecore.genmodel.GenJDKLevel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.codegen.util.CodeGenUtil.EclipseUtil;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.ecore.editor.IEcoreGenModelLinker;
import org.eclipse.emf.importer.ModelImporter;
import org.eclipse.emf.importer.ecore.EcoreImporter;

public class EcoreGenModelLinkerImpl implements IEcoreGenModelLinker {

	private Monitor monitor = EclipseUtil.createMonitor(
			new NullProgressMonitor(), 1);
	protected List<EPackage> referencedEPackages;
	private IPath modelProjectLocationPath;
	private IPath modelFragmentPath;
	private IPath editProjectLocationPath;
	private IPath editFragmentPath;
	private IPath editorProjectLocationPath;
	private IPath editorFragmentPath;
	private IPath genModelFullPath;
	private String modelLocation;
	private ModelImporter modelImporter = new EcoreImporter();
	protected Map<URI, Set<String>> referencedGenModelURIToEPackageNSURIs;
	private IPath defaultSrcPath = new Path("src");

	public void generateGenModel(String ecorePath, String genModelPath,
			String modelProjectPath, String editProjectPath,
			String editorProjectPath) {

		modelProjectLocationPath = getPathFromPathString(modelProjectPath);
		modelFragmentPath = defaultSrcPath;
		editProjectLocationPath = getPathFromPathString(editProjectPath);
		editFragmentPath = defaultSrcPath;
		editorProjectLocationPath = getPathFromPathString(editorProjectPath);
		editorFragmentPath = defaultSrcPath;
		genModelFullPath = getPathFromPathString(genModelPath);
		modelLocation = ecorePath;
		execute();
	}

	public IPath getPathFromPathString(String pathString) {
		return new Path(new File(pathString).getAbsolutePath());
	}

	public void execute() {

		adjustModelImporter();

		computeEPackages();
		adjustEPackages();
		adjustGenModel();

		doExecute();

	}

	private void doExecute() {
		modelImporter.prepareGenModelAndEPackages(monitor);
		adjustModelImporterAfterPrepare();
		handleReferencedEPackages();
		try {
			modelImporter.saveGenModelAndEPackages(monitor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleReferencedEPackages() {

	}

	private void adjustModelImporterAfterPrepare() {
		GenModel genModel = modelImporter.getGenModel();
		genModel.setComplianceLevel(GenJDKLevel.JDK50_LITERAL);
		genModel.setImportOrganizing(false);
		genModel.setOperationReflection(false);
		genModel.setRootExtendsClass("org.eclipse.emf.ecore.impl.EObjectImpl");
	}

	private void adjustGenModel() {

		GenModel genModel = modelImporter.getGenModel();
		if (editProjectLocationPath != null) {
			genModel.setEditDirectory(editProjectLocationPath + "/./"
					+ editFragmentPath + "/.");
		}
		if (editorProjectLocationPath != null) {
			genModel.setEditorDirectory(editorProjectLocationPath + "/./"
					+ editorFragmentPath + "/.");
		}
	}

	private void adjustEPackages() {
		List<EPackage> ePackages = modelImporter.getEPackages();
		traverseEPackages(ePackages);
		modelImporter.adjustEPackages(CodeGenUtil.createMonitor(monitor, 1));
	}

	protected void traverseEPackages(List<EPackage> ePackages) {
		for (EPackage ePackage : ePackages) {
			/*
			 * if (nameToPackageInfo != null) { PackageInfo packageInfo =
			 * nameToPackageInfo.get(ePackage .getNsURI()); if (packageInfo !=
			 * null) { handleEPackage(ePackage, true);
			 * 
			 * ModelImporter.EPackageImportInfo ePackageInfo = ((EcoreImporter)
			 * getModelImporter()) .getEPackageImportInfo(ePackage); if
			 * (ePackageInfo.getBasePackage() == null) {
			 * ePackageInfo.setBasePackage(packageInfo.base); } if
			 * (ePackageInfo.getPrefix() == null) {
			 * ePackageInfo.setPrefix(packageInfo.prefix); } } }
			 */

			handleQualifiedEPackageName(ePackage);
			traverseEPackages(ePackage.getESubpackages());
		}
	}

	protected void handleEPackage(EPackage ePackage, boolean generate) {
		modelImporter.getEPackageImportInfo(ePackage).setConvert(generate);
		if (!generate) {
			// The referencedEPackages list is used to track the packages for
			// which is necessary to create the stub GenModel. So if the
			// ePackage
			// is referenced by an existing GenPackage, it doesn't need to be
			// added to
			// referencedEPackages.

			for (GenPackage genPackage : modelImporter
					.getReferencedGenPackages()) {
				if (genPackage.getEcorePackage() == ePackage
						|| (genPackage.getNSURI() != null && genPackage
								.getNSURI().equals(ePackage.getNsURI()))) {
					return;
				}
			}

			if (referencedEPackages == null) {
				referencedEPackages = new ArrayList<EPackage>();
			}
			referencedEPackages.add(ePackage);
		}
	}

	protected void handleQualifiedEPackageName(EPackage ePackage) {
		String packageName = ePackage.getName();
		int index = packageName.lastIndexOf(".");
		if (index != -1) {
			modelImporter.getEPackageImportInfo(ePackage).setBasePackage(
					packageName.substring(0, index));
			ePackage.setName(packageName.substring(index + 1));
		}
	}

	protected void adjustModelImporter() {
		modelImporter.setUsePlatformURI(false);
		modelImporter.setGenModelProjectLocation(modelProjectLocationPath);
		modelImporter.setModelPluginDirectory(modelProjectLocationPath + "/./"
				+ modelFragmentPath + "/.");

		handleGenModelPath(genModelFullPath);
		File modelFile = new File(modelLocation);
		try {
			modelImporter.setModelLocation(URI.createFileURI(
					modelFile.getCanonicalPath()).toString());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	protected final void computeEPackages() {
		try {
			modelImporter.computeEPackages(monitor);
		} catch (Exception e) {
			System.err
					.println("The packages could not be computed by the model importer. This might lead to unexpected results.");
			e.printStackTrace();
		}
	}

	protected void handleGenModelPath(IPath genModelFullPath) {
		modelImporter.setGenModelContainerPath(genModelFullPath
				.removeLastSegments(1));
		modelImporter.setGenModelFileName(genModelFullPath.lastSegment());
	}
}
