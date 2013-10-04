package org.eclipse.emf.ecp.ecore.editor.test;

import static org.junit.Assert.*;

import org.eclipse.emf.ecp.ecore.editor.util.ProjectHelper;
import org.junit.Before;
import org.junit.Test;

public class ProjectNameTest {

	private ProjectHelper projectHelper;

	@Before
	public void init() {
		projectHelper = new ProjectHelper();
	}

	@Test
	public void testGetProjectName() {
		projectHelper.setProjectFullName("org.eclipse.ecp.emf.ecore.editor");
		String string = projectHelper.getProjectName();
		assertEquals("editor", string);

	}

	@Test
	public void testGetProjectNameOnePart() {
		projectHelper.setProjectFullName("org");
		String string = projectHelper.getProjectName();
		assertEquals("org", string);
	}

	@Test
	public void TestGetNSPrefix() {
		projectHelper.setProjectFullName("org.eclipse.ecp.emf.ecore.editor");
		String string = projectHelper.getNSPrefix();
		assertEquals("org.eclipse.ecp.emf.ecore", string);
	}

	@Test
	public void TestGetNSURL() {
		projectHelper.setProjectFullName("org.eclipse.ecp.emf.ecore.editor");
		String string = projectHelper.getNSURL();
		assertEquals("http://eclipse.org/ecp/emf/ecore/editor", string);
	}

}
