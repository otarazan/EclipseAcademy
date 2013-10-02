package org.eclipse.emf.ecp.ecore.editor.test;

import static org.junit.Assert.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecp.ecore.editor.ProjectHelper;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.junit.Test;

public class ProjectNameTest {

	
//	@Test
//	public void TestGetProjectName()
//	{
		//getting the current project name
//		IEditorPart  editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
//				getActivePage().
//				getActiveEditor();
//				String activeProjectName = null;
//
//				if(editorPart  != null)
//				{
//				IFileEditorInput input = (FileEditorInput)editorPart.getEditorInput();
//				IFile file = input.getFile();
//				IProject activeProject = file.getProject();
//				activeProjectName =activeProject.getName();
//				}
//
//		assertEquals("org.eclipse.ecp.emf.ecore.editor", activeProjectName);
//	}
	
	@Test
	public void TestGetProjectName()
	{
		String string = ProjectHelper.getProjectName("org.eclipse.ecp.emf.ecore.editor");
		
		assertEquals("editor", string);
		
	}
	
	@Test
	public void TestGetNSPrefix()
	{
		String string = ProjectHelper.getNSPrefix("org.eclipse.ecp.emf.ecore.editor");
		
		assertEquals("org.eclipse.ecp.emf.ecore", string);
		
	}
	
	
	@Test
	public void TestGetNSURL()
	{
		String string = ProjectHelper.getNSURL("org.eclipse.ecp.emf.ecore.editor");
		
		assertEquals("http://eclipse.org/ecp/emf/ecore/editor", string);
		
	}
	

}
