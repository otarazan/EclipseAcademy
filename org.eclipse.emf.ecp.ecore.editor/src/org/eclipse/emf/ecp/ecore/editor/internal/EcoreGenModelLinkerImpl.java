/**
 * Copyright (c) 2005-2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM - Initial API and implementation
 * David Soto Setzke
 * 
 * Class derived from org.eclipse.emf.importer.ecore.EcoreImporterApplication
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

/**
 * A class which creates a new ecore file and a new genmodel file and
 * links the two of them.
 */
public class EcoreGenModelLinkerImpl implements IEcoreGenModelLinker {

	private final Monitor monitor = EclipseUtil.createMonitor(
		new NullProgressMonitor(), 1);
	private IPath modelProjectLocationPath;
	private IPath modelFragmentPath;
	private IPath genModelPath;
	private String modelLocation;
	private final ModelImporter modelImporter = new EcoreImporter();
	private final IPath defaultSrcPath = new Path("src");

	/**
	 * Creates a new ecore file and a new genmodel file and links
	 * the two of them.
	 * 
	 * @param ecorePath The path (absolute) of the new ecore file
	 * @param genModelPathParam The path (relative) of the new genmodel file
	 * @param modelProjectPath The path (relative) of the model project
	 */
	public void generateGenModel(String ecorePath, String genModelPathParam,
		String modelProjectPath) {

		modelProjectLocationPath = getPathFromPathString(modelProjectPath);
		modelFragmentPath = defaultSrcPath;
		genModelPath = getPathFromPathString(genModelPathParam);
		modelLocation = ecorePath;

		adjustModelImporter();
		computeEPackages();
		adjustEPackages();
		createGenModel();
	}

	private IPath getPathFromPathString(String pathString) {
		return new Path(new File(pathString).getAbsolutePath());
	}

	private void adjustModelImporter() {
		modelImporter.setUsePlatformURI(false);
		modelImporter.setGenModelProjectLocation(modelProjectLocationPath);
		modelImporter.setModelPluginDirectory(modelProjectLocationPath + "/./"
			+ modelFragmentPath + "/.");
		handleGenModelPath(genModelPath);
		final File modelFile = new File(modelLocation);
		final URI modelFileURI = URI.createFileURI(modelFile.getPath());
		modelImporter.setModelLocation(modelFileURI.toString());
	}

	private void handleGenModelPath(IPath genModelPath) {
		modelImporter.setGenModelContainerPath(genModelPath
			.removeLastSegments(1));
		modelImporter.setGenModelFileName(genModelPath.lastSegment());
	}

	private void computeEPackages() {
		try {
			modelImporter.computeEPackages(monitor);
		} catch (final Exception e) {
			System.err
				.println("The packages could not be computed by the model importer. "
					+ "This might lead to unexpected results.");
			e.printStackTrace();
		}
	}

	private void adjustEPackages() {
		final List<EPackage> ePackages = modelImporter.getEPackages();
		traverseEPackages(ePackages);
		modelImporter.adjustEPackages(monitor);
	}

	private void createGenModel() {
		modelImporter.prepareGenModelAndEPackages(monitor);
		adjustModelImporterAfterPrepare();
		try {
			modelImporter.saveGenModelAndEPackages(monitor);
		} catch (final Exception e) {
			System.err.println("Gen model and packages could not be saved!");
			e.printStackTrace();
		}
	}

	private void adjustModelImporterAfterPrepare() {
		final GenModel genModel = modelImporter.getGenModel();
		genModel.setComplianceLevel(GenJDKLevel.JDK50_LITERAL);
		genModel.setImportOrganizing(true);
		genModel.setOperationReflection(true);
	}

	private void traverseEPackages(List<EPackage> ePackages) {
		for (final EPackage ePackage : ePackages) {
			handleQualifiedEPackageName(ePackage);
			traverseEPackages(ePackage.getESubpackages());
		}
	}

	private void handleQualifiedEPackageName(EPackage ePackage) {
		final String packageName = ePackage.getName();
		final int index = packageName.lastIndexOf(".");
		if (index != -1) {
			modelImporter.getEPackageImportInfo(ePackage).setBasePackage(
				packageName.substring(0, index));
			ePackage.setName(packageName.substring(index + 1));
		}
	}

}
