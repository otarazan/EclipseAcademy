/**
 * Copyright (c) 2005-2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *   David Soto Setzke
 */
package org.eclipse.emf.ecp.ecore.editor.internal;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.ecore.genmodel.GenJDKLevel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.util.CodeGenUtil.EclipseUtil;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.ecore.editor.IEcoreGenModelLinker;
import org.eclipse.emf.importer.ModelImporter;
import org.eclipse.emf.importer.ecore.EcoreImporter;

public class EcoreGenModelLinkerImpl implements IEcoreGenModelLinker {

	private Monitor monitor = EclipseUtil.createMonitor(
			new NullProgressMonitor(), 1);
	private IPath modelProjectLocationPath;
	private IPath modelFragmentPath;
	private IPath genModelPath;
	private String modelLocation;
	private ModelImporter modelImporter = new EcoreImporter();
	private IPath defaultSrcPath = new Path("src");

	public void generateGenModel(String ecorePath, String genModelPath,
			String modelProjectPath) {

		modelProjectLocationPath = getPathFromPathString(modelProjectPath);
		modelFragmentPath = defaultSrcPath;
		this.genModelPath = getPathFromPathString(genModelPath);
		modelLocation = ecorePath;

		adjustModelImporter();
		computeEPackages();
		adjustEPackages();
		// adjustGenModel();
		createGenModel();
	}

	public IPath getPathFromPathString(String pathString) {
		return new Path(new File(pathString).getAbsolutePath());
	}

	private void adjustModelImporter() {
		modelImporter.setUsePlatformURI(false);
		modelImporter.setGenModelProjectLocation(modelProjectLocationPath);
		modelImporter.setModelPluginDirectory(modelProjectLocationPath + "/./"
				+ modelFragmentPath + "/.");
		handleGenModelPath(genModelPath);
		File modelFile = new File(modelLocation);
		URI modelFileURI = URI.createFileURI(modelFile.getPath());
		modelImporter.setModelLocation(modelFileURI.toString());
	}

	private void handleGenModelPath(IPath genModelPath) {
		modelImporter.setGenModelContainerPath(genModelPath
				.removeLastSegments(1));
		modelImporter.setGenModelFileName(genModelPath.lastSegment());
	}

	private final void computeEPackages() {
		try {
			modelImporter.computeEPackages(monitor);
		} catch (Exception e) {
			System.err
					.println("The packages could not be computed by the model importer. "
							+ "This might lead to unexpected results.");
			e.printStackTrace();
		}
	}

	private void adjustEPackages() {
		List<EPackage> ePackages = modelImporter.getEPackages();
		traverseEPackages(ePackages);
		modelImporter.adjustEPackages(monitor);
	}

	private void createGenModel() {
		modelImporter.prepareGenModelAndEPackages(monitor);
		adjustModelImporterAfterPrepare();
		try {
			modelImporter.saveGenModelAndEPackages(monitor);
		} catch (Exception e) {
			System.err.println("Gen model and packages could not be saved!");
			e.printStackTrace();
		}
	}

	private void adjustModelImporterAfterPrepare() {
		GenModel genModel = modelImporter.getGenModel();
		genModel.setComplianceLevel(GenJDKLevel.JDK50_LITERAL);
		genModel.setImportOrganizing(true);
		genModel.setOperationReflection(true);
	}

	/*
	 * private void adjustGenModel() { GenModel genModel =
	 * modelImporter.getGenModel(); if (editProjectLocationPath != null)
	 * genModel.setEditDirectory(editProjectLocationPath + "/./" +
	 * editFragmentPath + "/."); if (editorProjectLocationPath != null)
	 * genModel.setEditorDirectory(editorProjectLocationPath + "/./" +
	 * editorFragmentPath + "/."); }
	 */

	private void traverseEPackages(List<EPackage> ePackages) {
		for (EPackage ePackage : ePackages) {
			handleQualifiedEPackageName(ePackage);
			traverseEPackages(ePackage.getESubpackages());
		}
	}

	private void handleQualifiedEPackageName(EPackage ePackage) {
		String packageName = ePackage.getName();
		int index = packageName.lastIndexOf(".");
		if (index != -1) {
			modelImporter.getEPackageImportInfo(ePackage).setBasePackage(
					packageName.substring(0, index));
			ePackage.setName(packageName.substring(index + 1));
		}
	}

}
