package org.eclipse.emf.ecp.ecore.editor.test;

import static org.junit.Assert.*;

import java.io.File;

import org.eclipse.emf.ecp.ecore.editor.EcoreGenModelLinker;
import org.junit.Test;

public class EcoreGenModelLinkerTest {

	@Test
	public void test() {
		EcoreGenModelLinker linker = new EcoreGenModelLinker();
		String ecorePath = "resource/model/library.ecore";
		File folder = new File("tmp");
		folder.mkdir();
		String genModelPath = "tmp/library.genmodel";
		String modelProjectPath = "tmp/result";
		String editProjectPath = "tmp/result.edit";
		String editorProjectPath = "tmp/result.editor";
		linker.generateGenModel(ecorePath, genModelPath, modelProjectPath, editProjectPath, editorProjectPath);
		File resultFolder = new File(modelProjectPath);
		File genModelFile = new File(genModelPath);
		File projectFile = new File(modelProjectPath + "/.project");
		File sourceFolder = new File(modelProjectPath + "/src");
		assertTrue(resultFolder.exists());
		assertTrue(genModelFile.exists());
		assertTrue(projectFile.exists());
		assertTrue(sourceFolder.exists());
	}
}
