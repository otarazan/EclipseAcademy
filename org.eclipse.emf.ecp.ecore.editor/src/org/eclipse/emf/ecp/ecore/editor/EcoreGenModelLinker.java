package org.eclipse.emf.ecp.ecore.editor;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.ecore.genmodel.GenJDKLevel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.importer.ModelImporter;
import org.eclipse.emf.importer.ecore.EcoreImporter;

public class EcoreGenModelLinker {

	private Monitor monitor = new Monitor() {

		@Override
		public void worked(int work) {
			// TODO Auto-generated method stub

		}

		@Override
		public void subTask(String name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setTaskName(String name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setCanceled(boolean value) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setBlocked(Diagnostic reason) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isCanceled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void internalWorked(double work) {
			// TODO Auto-generated method stub

		}

		@Override
		public void done() {
			// TODO Auto-generated method stub

		}

		@Override
		public void clearBlocked() {
			// TODO Auto-generated method stub

		}

		@Override
		public void beginTask(String name, int totalWork) {
			// TODO Auto-generated method stub

		}
	};

	private Path modelProjectLocationPath;
	private Path modelFragmentPath;
	private Path editProjectLocationPath;
	private Path editFragmentPath;
	private Path editorProjectLocationPath;
	private Path editorFragmentPath;
	private IPath genModelFullPath;
	private String modelLocation;
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
		// don't need that
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
