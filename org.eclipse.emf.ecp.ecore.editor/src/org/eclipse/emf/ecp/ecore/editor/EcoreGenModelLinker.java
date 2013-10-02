package org.eclipse.emf.ecp.ecore.editor;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.ecore.genmodel.GenJDKLevel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.importer.ModelImporter;
import org.eclipse.emf.importer.ecore.EcoreImporter;

public class EcoreGenModelLinker {

	private Path modelProjectLocationPath;
	private Path modelFragmentPath;
	private Path editProjectLocationPath;
	private Path editFragmentPath;
	private Path editorProjectLocationPath;
	private Path editorFragmentPath;
	private IPath genModelFullPath;
	private ModelImporter modelImporter = new EcoreImporter();

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
		getModelImporter().prepareGenModelAndEPackages(null);
		adjustModelImporterAfterPrepare();
		handleReferencedEPackages();
		try {
			getModelImporter().saveGenModelAndEPackages(null);
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
		// don't need that
	}

	protected void adjustModelImporter() {

		ModelImporter modelImporter = getModelImporter();
		modelImporter.setUsePlatformURI(false);
		modelImporter.setGenModelProjectLocation(modelProjectLocationPath);
		if (modelProjectLocationPath != null && modelFragmentPath != null)
			modelImporter.setModelPluginDirectory(modelProjectLocationPath
					+ "/./" + modelFragmentPath + "/.");

		handleGenModelPath(genModelFullPath);
		// modelImporter.setModelLocation(modelLocations);

	}

	protected final void computeEPackages() throws Exception {
		getModelImporter().computeEPackages(null);
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
