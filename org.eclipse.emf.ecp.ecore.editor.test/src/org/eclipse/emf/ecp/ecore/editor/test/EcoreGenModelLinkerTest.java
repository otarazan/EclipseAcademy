package org.eclipse.emf.ecp.ecore.editor.test;

import static org.junit.Assert.*;

import java.io.File;

import junit.framework.TestCase;

import org.eclipse.emf.ecp.ecore.editor.EcoreGenModelLinker;
import org.junit.Test;

public class EcoreGenModelLinkerTest extends TestCase {

	@Test
	public void test() throws Exception {
		String absPath = new File("").getAbsolutePath();
		EcoreGenModelLinker linker = new EcoreGenModelLinker();
		String ecorePath = absPath + "/resources/model/library.ecore";
		String genModelPath = absPath + "/tmp/library.genmodel";
		String modelProjectPath = absPath + "/tmp/result";
		String editProjectPath = absPath + "/tmp/result.edit";
		String editorProjectPath = absPath + "/tmp/result.editor";
		linker.generateGenModel(ecorePath, genModelPath, modelProjectPath,
				editProjectPath, editorProjectPath);
		File resultFolder = new File(modelProjectPath);
		File genModelFile = new File(genModelPath);
		File projectFile = new File(modelProjectPath + "/.project");
		File sourceFolder = new File(modelProjectPath + "/src");
		assertTrue(resultFolder.exists());
		assertTrue(genModelFile.exists());
		assertTrue(projectFile.exists());
		assertTrue(sourceFolder.exists());
	}

	@Override
	protected void setUp() {
		File folder = new File("tmp");
		folder.mkdir();
		System.out.println(folder.getAbsolutePath());
	}

	@Override
	protected void tearDown() {
		//File folder = new File("tmp");
		//folder.delete();
	}
}
