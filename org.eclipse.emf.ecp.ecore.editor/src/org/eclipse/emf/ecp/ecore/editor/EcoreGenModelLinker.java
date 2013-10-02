package org.eclipse.emf.ecp.ecore.editor;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.importer.ModelImporter;
import org.eclipse.emf.importer.ecore.EcoreImporter;

public class EcoreGenModelLinker {

	private Path modelProjectLocationPath;
	private Path modelFragmentPath;
	private IPath genModelFullPath;
	private ModelImporter modelImporter = new EcoreImporter();

	public void generateGenModel(String ecorePath, String genModelPath,
			String modelProjectPath, String editorProjectPath,
			String editorProjectPath2) {

		modelProjectLocationPath = new Path(
				new File(modelProjectPath).getAbsolutePath());
		modelFragmentPath = new Path("src");
		genModelFullPath = new Path(new File(genModelPath).getAbsolutePath());

	}

	public void execute() throws Exception {

		adjustModelImporter();

		computeEPackages();
		adjustEPackages();
		adjustGenModel();

		doExecute();

	}

	private void doExecute() {
		// TODO Auto-generated method stub

	}

	private void adjustGenModel() {
		// TODO Auto-generated method stub

	}

	private void adjustEPackages() {
		// TODO Auto-generated method stub

	}

	private void computeEPackages() {
		// TODO Auto-generated method stub

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

	protected void handleGenModelPath(IPath genModelFullPath) {
		modelImporter.setGenModelContainerPath(genModelFullPath
				.removeLastSegments(1));
		modelImporter.setGenModelFileName(genModelFullPath.lastSegment());
	}

	private ModelImporter getModelImporter() {
		return modelImporter;
	}
}
