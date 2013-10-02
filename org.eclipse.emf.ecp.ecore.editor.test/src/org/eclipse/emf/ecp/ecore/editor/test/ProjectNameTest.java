package org.eclipse.emf.ecp.ecore.editor.test;

import static org.junit.Assert.*;


import org.eclipse.emf.ecp.ecore.editor.ProjectHelper;
import org.junit.Test;

public class ProjectNameTest {


	
	@Test
	public void testGetProjectName()
	{
		ProjectHelper.setProjectFullName("org.eclipse.ecp.emf.ecore.editor");
		String string = ProjectHelper.getProjectName();
		
		assertEquals("editor", string);
		
	}
	
	@Test
	public void testGetProjectNameOnePart()
	{
		ProjectHelper.setProjectFullName("org");
		String string = ProjectHelper.getProjectName();
		
		assertEquals("org", string);
		
	}
	
	@Test
	public void TestGetNSPrefix()
	{
		ProjectHelper.setProjectFullName("org.eclipse.ecp.emf.ecore.editor");
		String string = ProjectHelper.getNSPrefix();
		assertEquals("org.eclipse.ecp.emf.ecore", string);
		
	}
	
	
	@Test
	public void TestGetNSURL()
	{
		ProjectHelper.setProjectFullName("org.eclipse.ecp.emf.ecore.editor");
		String string = ProjectHelper.getNSURL();
		
		assertEquals("http://eclipse.org/ecp/emf/ecore/editor", string);
		
	}
	

}
