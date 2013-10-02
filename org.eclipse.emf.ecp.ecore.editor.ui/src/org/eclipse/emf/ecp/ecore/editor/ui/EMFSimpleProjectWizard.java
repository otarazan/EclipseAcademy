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

import java.awt.Composite;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.presentation.EcoreEditorPlugin;
import org.eclipse.emf.ecp.ecore.editor.ui.EcoreModelWizard.EcoreModelWizardInitialObjectCreationPage;
import org.eclipse.emf.ecp.ecore.editor.ui.EcoreModelWizard.EcoreModelWizardNewFileCreationPage;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.importer.ImporterPlugin;
import org.eclipse.emf.importer.ui.EMFModelWizard;
import org.eclipse.emf.importer.ui.contribution.IModelImporterWizard;
import org.eclipse.emf.importer.ui.contribution.ModelImporterDescriptor;
import org.eclipse.emf.importer.ui.contribution.ModelImporterManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
//import org.eclipse.emf.ecp.ecore.editor.ui.EcoreModelWizard.EcoreModelWizardNewFileCreationPage;


/**
 * @since 2.1.0
 */
public class EMFSimpleProjectWizard extends EMFModelWizard
{
  protected IPath projectLocation;
  protected IPath projectPath;
  protected WizardNewProjectCreationPage newProjectCreationPage;
  
  EcoreModelWizard ecoreModelWizard;

  public EMFSimpleProjectWizard()
  {
    super();
    ecoreModelWizard=new EcoreModelWizard();
    
    setWindowTitle(ImporterPlugin.INSTANCE.getString("_UI_EMFProjectWizard_title"));
  }
  
  @Override
  protected ImageDescriptor getDefaultImageDescriptor()
  {
    return ExtendedImageRegistry.INSTANCE.getImageDescriptor(ImporterPlugin.INSTANCE.getImage("full/wizban/NewEMFProject"));
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
            projectLocation = Platform.getLocation().equals(locationPath) ? null : locationPath;
            projectPath = getProjectHandle().getFullPath();
            return true;
          }
          else
          {
            return false;
          }
        }
        
       @Override
       public boolean isPageComplete() {
    	   return !getProjectName().isEmpty();
      }

        public void createControl(Composite parent)
        {
        	
        }
      };
    newProjectCreationPage.setTitle(ImporterPlugin.INSTANCE.getString("_UI_EMFProjectWizard_name"));
    newProjectCreationPage.setDescription(ImporterPlugin.INSTANCE.getString("_UI_CreateEMFProject_label"));
    addPage(newProjectCreationPage);

    if (defaultPath != null)
    {
      String path = defaultPath.removeLastSegments(defaultPath.segmentCount()-1).toString();
      newProjectCreationPage.setInitialProjectName(path.charAt(0) == '/' ? path.substring(1) : path);
      newProjectCreationPage.setPageComplete(newProjectCreationPage.isPageComplete());  
     
    }

    addEcoreModelPages();

  
  }
  
  void addEcoreModelPages()
  {
	// Create a page, set the title, and the initial model file name.
	    //
	  ecoreModelWizard.newFileCreationPage = ecoreModelWizard.new EcoreModelWizardNewFileCreationPage("Whatever", selection);
	  ecoreModelWizard.newFileCreationPage.setTitle(EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreModelWizard_label"));
	  ecoreModelWizard.newFileCreationPage.setDescription(EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreModelWizard_description"));
	  ecoreModelWizard. newFileCreationPage.setFileName(EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreEditorFilenameDefaultBase") + "." + ecoreModelWizard.FILE_EXTENSIONS.get(0));
	  addPage(ecoreModelWizard.newFileCreationPage);

	    // Try and get the resource selection to determine a current directory for the file dialog.
	    //
	    if (selection != null && !selection.isEmpty())
	    {
	      // Get the resource...
	      //
	      Object selectedElement = selection.iterator().next();
	      if (selectedElement instanceof IResource)
	      {
	        // Get the resource parent, if its a file.
	        //
	        IResource selectedResource = (IResource)selectedElement;
	        if (selectedResource.getType() == IResource.FILE)
	        {
	          selectedResource = selectedResource.getParent();
	        }

	        // This gives us a directory...
	        //
	        if (selectedResource instanceof IFolder || selectedResource instanceof IProject)
	        {
	          // Set this for the container.
	          //
	        	ecoreModelWizard.newFileCreationPage.setContainerFullPath(selectedResource.getFullPath());

	          // Make up a unique new name here.
	          //
	          String defaultModelBaseFilename = EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreEditorFilenameDefaultBase");
	          String defaultModelFilenameExtension = ecoreModelWizard.FILE_EXTENSIONS.get(0);
	          String modelFilename = defaultModelBaseFilename + "." + defaultModelFilenameExtension;
	          for (int i = 1; ((IContainer)selectedResource).findMember(modelFilename) != null; ++i)
	          {
	            modelFilename = defaultModelBaseFilename + i + "." + defaultModelFilenameExtension;
	          }
	          ecoreModelWizard.newFileCreationPage.setFileName(modelFilename);
	        }
	      }
	    }
	    ecoreModelWizard.initialObjectCreationPage = ecoreModelWizard.new EcoreModelWizardInitialObjectCreationPage("Whatever2");
	    ecoreModelWizard.initialObjectCreationPage.setTitle(EcoreEditorPlugin.INSTANCE.getString("_UI_EcoreModelWizard_label"));
	    ecoreModelWizard.initialObjectCreationPage.setDescription(EcoreEditorPlugin.INSTANCE.getString("_UI_Wizard_initial_object_description"));
	    addPage(ecoreModelWizard.initialObjectCreationPage);
  }
  
  @Override
  public boolean canFinish()
  {
	 
	  
     return newProjectCreationPage.isPageComplete();
  }
  
  @Override
  public boolean performFinish()
  {
    selectionPage.performFinish();
    ecoreModelWizard.performFinish();
    return true;
  }

  @Override
  protected List<ModelImporterDescriptor> getModelImporterDescriptors()
  {
    return ModelImporterManager.INSTANCE.filterModelImporterDescriptors(ModelImporterDescriptor.TYPE_PROJECT);
  }
  
  @Override
  protected void adjustModelImporterWizard(IModelImporterWizard modelImporterWizard, ModelImporterDescriptor modelImporterDescriptor)
  {
    super.adjustModelImporterWizard(modelImporterWizard, modelImporterDescriptor);

    if (isValidNewValue(projectLocation, modelImporterWizard.getGenModelProjectLocation()))
    {
      modelImporterWizard.setGenModelProjectLocation(projectLocation);
    }
    if (isValidNewValue(projectPath, modelImporterWizard.getGenModelProjectPath()))
    {
      modelImporterWizard.setGenModelProjectPath(projectPath);
    }
    if (isValidNewValue(newProjectCreationPage.getSelectedWorkingSets(), modelImporterWizard.getWorkingSets()))
    {
      modelImporterWizard.setWorkingSets(newProjectCreationPage.getSelectedWorkingSets());
    }
  }
}
