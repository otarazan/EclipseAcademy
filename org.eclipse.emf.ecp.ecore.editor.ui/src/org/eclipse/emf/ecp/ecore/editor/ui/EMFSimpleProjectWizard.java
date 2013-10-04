/**
 * Copyright (c) 2005-2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 */
package org.eclipse.emf.ecp.ecore.editor.ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.codegen.ecore.Generator;
import org.eclipse.emf.codegen.ecore.genmodel.provider.GenModelEditPlugin;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.presentation.EcoreEditorPlugin;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecp.ecore.editor.ui.operations.CreateModelsWorkspaceModifyOperation;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;

/**
 * @since 2.1.0
 */
public class EMFSimpleProjectWizard extends Wizard implements INewWizard {
	protected IWorkbench workbench;
	protected IPath genModelProjectLocation;
	protected IPath genModelContainerPath;
	protected IProject project;
	protected String initialProjectName;
	protected IStructuredSelection selection;
	protected WizardNewProjectCreationPage newProjectCreationPage;
	private EcoreModelWizardInitialObjectCreationPage initialObjectCreationPage;
	protected List<String> initialObjectNames;
	protected EcorePackage ecorePackage = EcorePackage.eINSTANCE;
	protected EcoreFactory ecoreFactory = ecorePackage.getEcoreFactory();

	@Override
	public void setContainer(IWizardContainer wizardContainer) {
		super.setContainer(wizardContainer);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE
				.getImageDescriptor(GenModelEditPlugin.INSTANCE
						.getImage("full/wizban/NewEmptyEMFProject")));
		setWindowTitle(GenModelEditPlugin.INSTANCE
				.getString("_UI_NewEmptyProject_title"));

	}

	@Override
	public void addPages() {
		newProjectCreationPage = new WizardNewProjectCreationPage(
				"NewProjectCreationPage") {
			@Override
			protected boolean validatePage() {
				if (super.validatePage()) {
					IPath locationPath = getLocationPath();
					genModelProjectLocation = Platform.getLocation().equals(
							locationPath) ? null : locationPath;
					IPath projectPath = getProjectHandle().getFullPath();
					genModelContainerPath = projectPath.append("src");
					return true;
				} else {
					return false;
				}
			}

			@Override
			public void createControl(Composite parent) {
				super.createControl(parent);
				createWorkingSetGroup((Composite) getControl(), selection,
						new String[] { "org.eclipse.jdt.ui.JavaWorkingSetPage",
								"org.eclipse.pde.ui.pluginWorkingSet",
								"org.eclipse.ui.resourceWorkingSetPage" });
			}
		};
		newProjectCreationPage.setInitialProjectName(initialProjectName);
		newProjectCreationPage.setTitle(GenModelEditPlugin.INSTANCE
				.getString("_UI_EmptyProject_title"));
		newProjectCreationPage.setDescription(GenModelEditPlugin.INSTANCE
				.getString("_UI_EmptyProject_description"));

		addPage(newProjectCreationPage);

		initialObjectCreationPage = new EcoreModelWizardInitialObjectCreationPage(
				"Whatever2");
		initialObjectCreationPage.setTitle(EcoreEditorPlugin.INSTANCE
				.getString("_UI_EcoreModelWizard_label"));
		initialObjectCreationPage.setDescription(EcoreEditorPlugin.INSTANCE
				.getString("_UI_Wizard_initial_object_description"));

		addPage(initialObjectCreationPage);
	}

	@Override
	public boolean performFinish() {
		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor progressMonitor) {
				try {
					modifyWorkspace(progressMonitor);
				} catch (Exception exception) {
					GenModelEditPlugin.INSTANCE.log(exception);
				} finally {
					progressMonitor.done();
				}
			}
		};

		try {
			getContainer().run(false, false, operation);
		} catch (Exception exception) {
			GenModelEditPlugin.INSTANCE.log(exception);
			return false;
		}

		if (project != null) {
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow()
					.getActivePage();
			final IWorkbenchPart activePart = page.getActivePart();
			if (activePart instanceof ISetSelectionTarget) {
				final ISelection targetSelection = new StructuredSelection(
						project);
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						((ISetSelectionTarget) activePart)
								.selectReveal(targetSelection);
					}
				});
			}
		}
		initialObjectCreationPage.setProjectName(newProjectCreationPage
				.getProjectName());
		IFile modelFile = getModelFile(newProjectCreationPage.getProjectName(),
				"test.ecore");
		try {

			// Do the work within an operation.
			WorkspaceModifyOperation createModelsOperation = new CreateModelsWorkspaceModifyOperation(
					modelFile, "EPackage", "UTF-8",
					newProjectCreationPage.getProjectName());

			getContainer().run(false, false, createModelsOperation);

			// Select the new file resource in the current view.
			IWorkbenchWindow workbenchWindow = workbench
					.getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			final IWorkbenchPart activePart = page.getActivePart();
			if (activePart instanceof ISetSelectionTarget) {
				final ISelection targetSelection = new StructuredSelection(
						modelFile);
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						((ISetSelectionTarget) activePart)
								.selectReveal(targetSelection);
					}
				});
			}

			// Open an editor on the new file.
			try {
				IEditorDescriptor defaultEditor = workbench.getEditorRegistry()
						.getDefaultEditor(modelFile.getFullPath().toString());
				page.openEditor(
						new FileEditorInput(modelFile),
						defaultEditor == null ? "org.eclipse.emf.ecore.presentation.EcoreEditorID"
								: defaultEditor.getId());
			} catch (PartInitException exception) {
				MessageDialog.openError(workbenchWindow.getShell(),
						EcoreEditorPlugin.INSTANCE
								.getString("_UI_OpenEditorError_label"),
						exception.getMessage());
				return false;
			}

			return true;
		} catch (Exception exception) {
			EcoreEditorPlugin.INSTANCE.log(exception);
			return false;
		}
	}

	protected String modelString;

	public void modifyWorkspace(IProgressMonitor progressMonitor)
			throws CoreException, UnsupportedEncodingException, IOException {
		project = Generator.createEMFProject(
				new Path(genModelContainerPath.toString()),
				genModelProjectLocation, Collections.<IProject> emptyList(),
				progressMonitor, Generator.EMF_MODEL_PROJECT_STYLE
						| Generator.EMF_PLUGIN_PROJECT_STYLE);

		IWorkingSet[] workingSets = newProjectCreationPage
				.getSelectedWorkingSets();
		if (workingSets != null) {
			workbench.getWorkingSetManager().addToWorkingSets(project,
					workingSets);
		}

		CodeGenUtil.EclipseUtil.findOrCreateContainer(new Path("/"
				+ genModelContainerPath.segment(0) + "/model"), true,
				genModelProjectLocation, progressMonitor);

		PrintStream manifest = new PrintStream(
				URIConverter.INSTANCE.createOutputStream(
						URI.createPlatformResourceURI("/"
								+ genModelContainerPath.segment(0)
								+ "/META-INF/MANIFEST.MF", true), null), false,
				"UTF-8");
		manifest.println("Manifest-Version: 1.0");
		manifest.println("Bundle-ManifestVersion: 2");
		manifest.print("Bundle-Name: ");
		manifest.println(genModelContainerPath.segment(0));
		manifest.print("Bundle-SymbolicName: ");
		manifest.print(CodeGenUtil.validPluginID(genModelContainerPath
				.segment(0)));
		manifest.println("; singleton:=true");
		manifest.println("Bundle-Version: 0.1.0.qualifier");
		manifest.print("Require-Bundle: ");
		String[] requiredBundles = getRequiredBundles();
		for (int i = 0, size = requiredBundles.length; i < size;) {
			manifest.print(requiredBundles[i]);
			if (++i == size) {
				manifest.println();
				break;
			} else {
				manifest.println(",");
				manifest.print(" ");
			}
		}
		manifest.close();
	}

	public void setInitialProjectName(String value) {
		initialProjectName = value;
	}

	protected String[] getRequiredBundles() {
		return new String[] { "org.eclipse.emf.ecore" };
	}

	/**
	 * This is the page where the type of object to create is selected. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public class EcoreModelWizardInitialObjectCreationPage extends WizardPage {
		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected Combo initialObjectField;

		/**
		 * @generated <!-- begin-user-doc --> <!-- end-user-doc -->
		 */
		protected List<String> encodings;

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected Combo encodingField;

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		private Text ecoreFileNameText;

		/**
		 * Pass in the selection. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public EcoreModelWizardInitialObjectCreationPage(String pageId) {
			super(pageId);
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public void createControlGen(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			{
				GridLayout layout = new GridLayout();
				layout.numColumns = 1;
				layout.verticalSpacing = 12;
				composite.setLayout(layout);

				GridData data = new GridData();
				data.verticalAlignment = GridData.FILL;
				data.grabExcessVerticalSpace = true;
				data.horizontalAlignment = GridData.FILL;
				composite.setLayoutData(data);
			}

			Label fileNameLabel = new Label(composite, SWT.LEFT);
			{
				fileNameLabel.setText("File Name");

				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				fileNameLabel.setLayoutData(data);
			}
			ecoreFileNameText = new Text(composite, SWT.BORDER);
			{
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				data.grabExcessHorizontalSpace = true;
				ecoreFileNameText.setLayoutData(data);
			}

			Label containerLabel = new Label(composite, SWT.LEFT);
			{
				containerLabel.setText(EcoreEditorPlugin.INSTANCE
						.getString("_UI_ModelObject"));

				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				containerLabel.setLayoutData(data);
			}

			initialObjectField = new Combo(composite, SWT.BORDER);
			{
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				data.grabExcessHorizontalSpace = true;
				initialObjectField.setLayoutData(data);
			}

			for (String objectName : getInitialObjectNames()) {
				initialObjectField.add(getLabel(objectName));
			}

			if (initialObjectField.getItemCount() == 1) {
				initialObjectField.select(0);
			}
			initialObjectField.addModifyListener(validator);

			Label encodingLabel = new Label(composite, SWT.LEFT);
			{
				encodingLabel.setText(EcoreEditorPlugin.INSTANCE
						.getString("_UI_XMLEncoding"));

				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				encodingLabel.setLayoutData(data);
			}
			encodingField = new Combo(composite, SWT.BORDER);
			{
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				data.grabExcessHorizontalSpace = true;
				encodingField.setLayoutData(data);
			}

			for (String encoding : getEncodings()) {
				encodingField.add(encoding);
			}

			encodingField.select(0);
			encodingField.addModifyListener(validator);

			setPageComplete(validatePage());
			setControl(composite);
		}

		public void createControl(Composite parent) {
			createControlGen(parent);
			initialObjectField.setText("EPackage");
			ecoreFileNameText.setText("model.ecore");
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected ModifyListener validator = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());
			}
		};

		private String projectName;

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected boolean validatePage() {
			return getInitialObjectName() != null
					&& getEncodings().contains(encodingField.getText());
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public void setVisible(boolean visible) {
			super.setVisible(visible);
			if (visible) {
				if (initialObjectField.getItemCount() == 1) {
					initialObjectField.clearSelection();
					encodingField.setFocus();
				} else {
					encodingField.clearSelection();
					initialObjectField.setFocus();
				}
			}
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public String getInitialObjectName() {
			String label = initialObjectField.getText();

			for (String name : getInitialObjectNames()) {
				if (getLabel(name).equals(label)) {
					return name;
				}
			}
			return null;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public String getEncoding() {
			return encodingField.getText();
		}

		/**
		 * Returns the label for the specified type name. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected String getLabel(String typeName) {
			try {
				return EcoreEditPlugin.INSTANCE.getString("_UI_" + typeName
						+ "_type");
			} catch (MissingResourceException mre) {
				EcoreEditorPlugin.INSTANCE.log(mre);
			}
			return typeName;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected Collection<String> getEncodings() {
			if (encodings == null) {
				encodings = new ArrayList<String>();
				for (StringTokenizer stringTokenizer = new StringTokenizer(
						EcoreEditorPlugin.INSTANCE
								.getString("_UI_XMLEncodingChoices")); stringTokenizer
						.hasMoreTokens();) {
					encodings.add(stringTokenizer.nextToken());
				}
			}
			return encodings;
		}

		public String getEcoreFileNameText() {
			return ecoreFileNameText.getText();
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;

		}

		public String getProjectName() {
			return projectName;
		}

	}

	protected Collection<String> getInitialObjectNames() {
		if (initialObjectNames == null) {
			initialObjectNames = new ArrayList<String>();
			for (EClassifier eClassifier : ecorePackage.getEClassifiers()) {
				if (eClassifier instanceof EClass) {
					EClass eClass = (EClass) eClassifier;
					if (!eClass.isAbstract()) {
						initialObjectNames.add(eClass.getName());
					}
				}
			}
			Collections.sort(initialObjectNames,
					CommonPlugin.INSTANCE.getComparator());
		}
		return initialObjectNames;
	}

	private IFile getModelFile(String projectName, String fileName) {
		return ResourcesPlugin
				.getWorkspace()
				.getRoot()
				.getFile(
						new Path(new File(projectName + "/model/" + fileName)
								.getPath()));
	}
}
