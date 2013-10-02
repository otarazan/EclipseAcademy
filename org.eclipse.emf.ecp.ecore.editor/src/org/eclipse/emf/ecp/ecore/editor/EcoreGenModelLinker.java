package org.eclipse.emf.ecp.ecore.editor;

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
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.importer.ModelImporter;
import org.eclipse.emf.importer.ecore.EcoreImporter;
import org.eclipse.emf.importer.ecore.EcoreImporterApplication.PackageInfo;

public class EcoreGenModelLinker {

	private Monitor monitor = EclipseUtil.createMonitor(
			new NullProgressMonitor(), 1);
	protected List<EPackage> referencedEPackages;
	private Path modelProjectLocationPath;
	private Path modelFragmentPath;
	private Path editProjectLocationPath;
	private Path editFragmentPath;
	private Path editorProjectLocationPath;
	private Path editorFragmentPath;
	private IPath genModelFullPath;
	private String modelLocation;
	private ModelImporter modelImporter = new EcoreImporter();
	protected Map<URI, Set<String>> referencedGenModelURIToEPackageNSURIs;

	public void generateGenModel(String ecorePath, String genModelPath,
			String modelProjectPath, String editProjectPath,
			String editorProjectPath) throws Exception {

		modelProjectLocationPath = new Path(
				new File(modelProjectPath).getAbsolutePath());
		modelFragmentPath = new Path("src");
		editProjectLocationPath = new Path(
				new File(editProjectPath).getAbsolutePath());
		editFragmentPath = new Path("src");
		editorProjectLocationPath = new Path(
				new File(editorProjectPath).getAbsolutePath());
		editorFragmentPath = new Path("src");
		genModelFullPath = new Path(new File(genModelPath).getAbsolutePath());
		modelLocation = ecorePath;
		execute();
	}

	public void execute() throws Exception {

		adjustModelImporter();

		computeEPackages();
		adjustEPackages();
		adjustGenModel();

		doExecute();

	}

	private void doExecute() {
		getModelImporter().prepareGenModelAndEPackages(monitor);
		adjustModelImporterAfterPrepare();
		handleReferencedEPackages();
		try {
			getModelImporter().saveGenModelAndEPackages(monitor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleReferencedEPackages() {

	}

	private void adjustModelImporterAfterPrepare() {
		GenModel genModel = getModelImporter().getGenModel();
		genModel.setComplianceLevel(GenJDKLevel.JDK50_LITERAL);
		genModel.setImportOrganizing(false);
		genModel.setOperationReflection(false);
		genModel.setRootExtendsClass("org.eclipse.emf.ecore.impl.EObjectImpl");
	}

	private void adjustGenModel() {

		GenModel genModel = getModelImporter().getGenModel();
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
		List<EPackage> ePackages = ((EcoreImporter) getModelImporter())
				.getEPackages();
		traverseEPackages(ePackages);
		((EcoreImporter) getModelImporter()).adjustEPackages(CodeGenUtil
				.createMonitor(monitor, 1));
	}

	protected void traverseEPackages(List<EPackage> ePackages) {
		for (EPackage ePackage : ePackages) {
			/*if (nameToPackageInfo != null) {
				PackageInfo packageInfo = nameToPackageInfo.get(ePackage
						.getNsURI());
				if (packageInfo != null) {
					handleEPackage(ePackage, true);

					ModelImporter.EPackageImportInfo ePackageInfo = ((EcoreImporter) getModelImporter())
							.getEPackageImportInfo(ePackage);
					if (ePackageInfo.getBasePackage() == null) {
						ePackageInfo.setBasePackage(packageInfo.base);
					}
					if (ePackageInfo.getPrefix() == null) {
						ePackageInfo.setPrefix(packageInfo.prefix);
					}
				}
			}*/

			handleQualifiedEPackageName(ePackage);
			traverseEPackages(ePackage.getESubpackages());
		}
	}

	protected void handleEPackage(EPackage ePackage, boolean generate) {
		getModelImporter().getEPackageImportInfo(ePackage).setConvert(generate);
		if (!generate) {
			// The referencedEPackages list is used to track the packages for
			// which is necessary to create the stub GenModel. So if the
			// ePackage
			// is referenced by an existing GenPackage, it doesn't need to be
			// added to
			// referencedEPackages.

			for (GenPackage genPackage : getModelImporter()
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
			getModelImporter().getEPackageImportInfo(ePackage).setBasePackage(
					packageName.substring(0, index));
			ePackage.setName(packageName.substring(index + 1));
		}
	}

	protected void adjustModelImporter() {

		ModelImporter modelImporter = getModelImporter();
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

	protected final void computeEPackages() throws Exception {
		getModelImporter().computeEPackages(monitor);
	}

	protected void handleGenModelPath(IPath genModelFullPath) {
		modelImporter.setGenModelContainerPath(genModelFullPath
				.removeLastSegments(1));
		modelImporter.setGenModelFileName(genModelFullPath.lastSegment());
	}

	private ModelImporter getModelImporter() {
		return modelImporter;
	}
}
