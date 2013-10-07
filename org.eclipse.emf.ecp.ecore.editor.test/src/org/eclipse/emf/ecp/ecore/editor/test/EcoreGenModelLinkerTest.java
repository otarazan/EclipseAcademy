/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * David Soto Setzke
 ******************************************************************************/
package org.eclipse.emf.ecp.ecore.editor.test;

import java.io.File;

import junit.framework.TestCase;

import org.eclipse.emf.ecp.ecore.editor.IEcoreGenModelLinker;
import org.eclipse.emf.ecp.ecore.editor.factory.EcoreGenModelLinkerFactory;
import org.junit.Test;

public class EcoreGenModelLinkerTest extends TestCase {

	@Test
	public void test() {
		String absPath = new File("").getAbsolutePath();
		IEcoreGenModelLinker linker = EcoreGenModelLinkerFactory
				.getEcoreGenModelLinker();
		String ecorePath = absPath + "/resources/model/library.ecore";
		String genModelPath = absPath + "/tmp/library.genmodel";
		String modelProjectPath = absPath + "/tmp/result";
		linker.generateGenModel(ecorePath, genModelPath, modelProjectPath);
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
		File tempFolder = new File("tmp");
		tempFolder.mkdir();
	}

	@Override
	protected void tearDown() {
		File tempFolder = new File("tmp");
		FileUtil.delete(tempFolder);
	}
}
