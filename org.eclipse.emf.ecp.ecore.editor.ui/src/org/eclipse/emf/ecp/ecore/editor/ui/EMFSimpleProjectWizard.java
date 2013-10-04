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


import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.emf.codegen.ecore.Generator;
import org.eclipse.emf.codegen.ecore.genmodel.provider.GenModelEditPlugin;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.presentation.EcoreEditorPlugin;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecp.ecore.editor.ui.EcoreModelWizard.EcoreModelWizardInitialObjectCreationPage;
import org.eclipse.emf.ecp.ecore.editor.ui.EcoreModelWizard.EcoreModelWizardNewFileCreationPage;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;


/**
 * @since 2.1.0
 */
public class EMFSimpleProjectWizard extends Wizard implements INewWizard
{
  protected IWorkbench workbench;
  protected IPath genModelProjectLocation;
  protected IPath genModelContainerPath;
  protected IProject project;
  protected String initialProjectName;
  protected IStructuredSelection selection;
  protected WizardNewProjectCreationPage newProjectCreationPage;
  EcoreModelWizard ecoreModelWizard;
  
  

  @Override
public void setContainer(IWizardContainer wizardContainer) {
	  ecoreModelWizard.setContainer(wizardContainer);
	super.setContainer(wizardContainer);
}

public void init(IWorkbench workbench, IStructuredSelection selection)
  {
    this.workbench = workbench;
    this.selection = selection;
    setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(GenModelEditPlugin.INSTANCE.getImage("full/wizban/NewEmptyEMFProject")));
    setWindowTitle(GenModelEditPlugin.INSTANCE.getString("_UI_NewEmptyProject_title"));
    ecoreModelWizard=new EcoreModelWizard();
    ecoreModelWizard.init(workbench, selection);
    
  }

  @Override
  public void addPages()
  {
    newProjectCreationPage = new WizardNewProjectCreationPage("NewProjectCreationPage")
      {
        @Override
        protected boolean validatePage()
        {
          if (super.validatePage())
          {
            IPath locationPath = getLocationPath();
            genModelProjectLocation = Platform.getLocation().equals(locationPath) ? null : locationPath;
            IPath projectPath = getProjectHandle().getFullPath();
            genModelContainerPath = projectPath.append("src");
            return true;
          }
          else
          {
            return false;
          }
        }
        
      

        @Override
        public void createControl(Composite parent)
        {
          super.createControl(parent);
          createWorkingSetGroup
            ((Composite) getControl(), 
             selection, 
             new String[] { "org.eclipse.jdt.ui.JavaWorkingSetPage", "org.eclipse.pde.ui.pluginWorkingSet", "org.eclipse.ui.resourceWorkingSetPage"});
        }
      };
      
    newProjectCreationPage.setInitialProjectName(initialProjectName);
    newProjectCreationPage.setTitle(GenModelEditPlugin.INSTANCE.getString("_UI_EmptyProject_title"));
    newProjectCreationPage.setDescription(GenModelEditPlugin.INSTANCE.getString("_UI_EmptyProject_description"));
    
    addPage(newProjectCreationPage);
   
    addEcoreModelWizardPages();
  }

  
  public void addEcoreModelWizardPages()
  {
	  
	    // Create a page, set the title, and the initial model file name.
	    
	 // ecoreModelWizard.newFileCreationPage = ecoreModelWizard.new EcoreModelWizardNewFileCreationPage("Whatever", selection);
//	  ecoreModelWizard.newFileCreationPage.setTitle(EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreModelWizard_label"));
//	  ecoreModelWizard.newFileCreationPage.setDescription(EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreModelWizard_description"));
//	  ecoreModelWizard.newFileCreationPage.setFileName(EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreEditorFilenameDefaultBase") + "." + ecoreModelWizard.FILE_EXTENSIONS.get(0));
//	   addPage(ecoreModelWizard.newFileCreationPage);

	    // Try and get the resource selection to determine a current directory for the file dialog.
	    //
//	    if (selection != null && !selection.isEmpty())
//	    {
//	      // Get the resource...
//	      //
//	      Object selectedElement = selection.iterator().next();
//	      if (selectedElement instanceof IResource)
//	      {
//	        // Get the resource parent, if its a file.
//	        //
//	        IResource selectedResource = (IResource)selectedElement;
//	        if (selectedResource.getType() == IResource.FILE)
//	        {
//	          selectedResource = selectedResource.getParent();
//	        }
//
//	        // This gives us a directory...
//	        //
//	        if (selectedResource instanceof IFolder || selectedResource instanceof IProject)
//	        {
//	          // Set this for the container.
//	          //
//	        	ecoreModelWizard.newFileCreationPage.setContainerFullPath(selectedResource.getFullPath());
//
//	          // Make up a unique new name here.
//	          //
//	        	
//	          String defaultModelBaseFilename = EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreEditorFilenameDefaultBase");
//	          String defaultModelFilenameExtension = ecoreModelWizard.FILE_EXTENSIONS.get(0);
//	          String modelFilename = defaultModelBaseFilename + "." + defaultModelFilenameExtension;
//	          for (int i = 1; ((IContainer)selectedResource).findMember(modelFilename) != null; ++i)
//	          {
//	            modelFilename = defaultModelBaseFilename + i + "." + defaultModelFilenameExtension;
//	          }
//	          ecoreModelWizard.newFileCreationPage.setFileName(modelFilename);
//	        }
//	      }
//	    }
	    ecoreModelWizard.initialObjectCreationPage = ecoreModelWizard.new EcoreModelWizardInitialObjectCreationPage("Whatever2");
	    ecoreModelWizard.initialObjectCreationPage.setTitle(EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreModelWizard_label"));
	    ecoreModelWizard.initialObjectCreationPage.setDescription(EcoreEditorPlugin.INSTANCE.getString("_UI_Wizard_initial_object_description"));
	    
	    addPage(ecoreModelWizard.initialObjectCreationPage);
	    
	  
  }
  
  @Override
  public boolean performFinish()
  {
    WorkspaceModifyOperation operation = new WorkspaceModifyOperation()
      {
        @Override
        protected void execute(IProgressMonitor progressMonitor)
        {
          try
          {
            modifyWorkspace(progressMonitor);
          }
          catch (Exception exception)
          {
            GenModelEditPlugin.INSTANCE.log(exception);
          }
          finally
          {
            progressMonitor.done();
          }
        }
      };

    try
    {
      getContainer().run(false, false, operation);
    }
    catch (Exception exception)
    {
      GenModelEditPlugin.INSTANCE.log(exception);
      return false;
    }

    if (project != null)
    {
      IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
      final IWorkbenchPart activePart = page.getActivePart();
      if (activePart instanceof ISetSelectionTarget)
      {
        final ISelection targetSelection = new StructuredSelection(project);
        getShell().getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              ((ISetSelectionTarget)activePart).selectReveal(targetSelection);
            }
          });
      }
    }
    
    //String projectPath=newProjectCreationPage.getLocationPath().append(newProjectCreationPage.getProjectName());
    ecoreModelWizard.initialObjectCreationPage.setProjectName(newProjectCreationPage.getProjectName());
    ecoreModelWizard.performFinish();
    
    return true;
  }
  
  public void modifyWorkspace(IProgressMonitor progressMonitor) throws CoreException, UnsupportedEncodingException, IOException
  {
     project = Generator.createEMFProject(
       new Path(genModelContainerPath.toString()),
       genModelProjectLocation,
       Collections.<IProject>emptyList(),
       progressMonitor,
       Generator.EMF_MODEL_PROJECT_STYLE | Generator.EMF_PLUGIN_PROJECT_STYLE);

     IWorkingSet[] workingSets = newProjectCreationPage.getSelectedWorkingSets();
     if (workingSets != null)
     {
       workbench.getWorkingSetManager().addToWorkingSets(project, workingSets);
     }

     CodeGenUtil.EclipseUtil.findOrCreateContainer
       (new Path("/" + genModelContainerPath.segment(0) + "/model"), true, genModelProjectLocation, progressMonitor);

     PrintStream manifest = 
       new PrintStream
         (URIConverter.INSTANCE.createOutputStream
            (URI.createPlatformResourceURI("/" + genModelContainerPath.segment(0) + "/META-INF/MANIFEST.MF", true), null),
          false, "UTF-8");
     manifest.println("Manifest-Version: 1.0");
     manifest.println("Bundle-ManifestVersion: 2");
     manifest.print("Bundle-Name: ");
     manifest.println(genModelContainerPath.segment(0));
     manifest.print("Bundle-SymbolicName: ");
     manifest.print(CodeGenUtil.validPluginID(genModelContainerPath.segment(0)));
     manifest.println("; singleton:=true");
     manifest.println("Bundle-Version: 0.1.0.qualifier");
     manifest.print("Require-Bundle: ");
     String[] requiredBundles = getRequiredBundles();
     for (int i = 0, size = requiredBundles.length; i < size; )
     {
       manifest.print(requiredBundles[i]);
       if (++i == size)
       {
         manifest.println();
         break;
       }
       else
       {
         manifest.println(",");
         manifest.print(" ");
       }
     }
     manifest.close();
  }

  public void setInitialProjectName(String value)
  {
    initialProjectName = value;
  }
  

  protected String[] getRequiredBundles()
  {
    return new String [] { "org.eclipse.emf.ecore" };
  }


}

