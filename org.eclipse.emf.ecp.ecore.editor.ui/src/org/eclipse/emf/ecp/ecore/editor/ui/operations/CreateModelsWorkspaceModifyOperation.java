package org.eclipse.emf.ecp.ecore.editor.ui.operations;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.presentation.EcoreEditorPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecp.ecore.editor.IEcoreGenModelLinker;
import org.eclipse.emf.ecp.ecore.editor.factory.EcoreGenModelLinkerFactory;
import org.eclipse.emf.ecp.ecore.editor.util.ProjectHelper;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class CreateModelsWorkspaceModifyOperation extends
		WorkspaceModifyOperation {

	private IFile modelFile;
	private ProjectHelper projectHelper = new ProjectHelper();
	private String initialObjectName;
	private String encoding;
	protected EcorePackage ecorePackage = EcorePackage.eINSTANCE;
	protected EcoreFactory ecoreFactory = ecorePackage.getEcoreFactory();

	public CreateModelsWorkspaceModifyOperation(IFile modelFile,
			String initialObjectName, String encoding, String projectFullName) {
		this.modelFile = modelFile;
		this.initialObjectName = initialObjectName;
		this.encoding = encoding;
		this.projectHelper.setProjectFullName(projectFullName);
	}

	protected EObject createInitialModel() {
		EClass eClass = (EClass) ecorePackage.getEClassifier(initialObjectName);
		EObject rootObject = ecoreFactory.create(eClass);

		// We can't have a null name, in case we're using EMOF serialization.
		if (rootObject instanceof ENamedElement) {
			((ENamedElement) rootObject).setName("");
		}
		return rootObject;
	}

	@Override
	protected void execute(IProgressMonitor progressMonitor) {
		try {
			// Create a resource set
			//
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getURIConverter().getURIMap()
					.putAll(EcorePlugin.computePlatformURIMap(true));

			// Get the URI of the model file.
			//
			URI fileURI = URI.createPlatformResourceURI(modelFile.getFullPath()
					.toString(), true);

			// Create a resource for this file. Don't specify a
			// content type, as it could be Ecore or EMOF.
			//
			Resource resource = resourceSet.createResource(fileURI);

			// Add the initial model object to the contents.
			//
			EObject rootObject = createInitialModel();
			if (rootObject != null) {
				resource.getContents().add(rootObject);
			}

			// Save the contents of the resource to the file system.
			Map<Object, Object> options = new HashMap<Object, Object>();
			options.put(XMLResource.OPTION_ENCODING, encoding);
			options.put(Resource.OPTION_LINE_DELIMITER,
					Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);
			EPackage ecorepack = EcorePackage.eINSTANCE;
			ecorepack = (EPackage) resource.getContents().get(0);
			ecorepack.setName(projectHelper.getProjectName());
			ecorepack.setNsPrefix(projectHelper.getNSPrefix());
			ecorepack.setNsURI(projectHelper.getNSURL());
			resource.save(options);
			IEcoreGenModelLinker linker = EcoreGenModelLinkerFactory
					.getEcoreGenModelLinker();
			String genModelPath = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(modelFile.getLocation()).getFullPath()
					.removeLastSegments(1).toFile().getAbsolutePath()
					+ "/model.genmodel";
			String ecorePath = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(modelFile.getLocation()).getFullPath().toFile()
					.getAbsolutePath();
			String projectPath = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(modelFile.getLocation()).getFullPath()
					.removeLastSegments(2).toFile().getAbsolutePath();
			linker.generateGenModel(ecorePath, genModelPath, projectPath,
					projectPath, projectPath);
		} catch (Exception exception) {
			EcoreEditorPlugin.INSTANCE.log(exception);
		} finally {
			progressMonitor.done();
		}
	}

}
