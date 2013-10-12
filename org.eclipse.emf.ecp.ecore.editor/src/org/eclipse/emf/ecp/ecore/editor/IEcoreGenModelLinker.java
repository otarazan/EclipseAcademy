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
package org.eclipse.emf.ecp.ecore.editor;

/**
 * A class which creates a new ecore file and a new genmodel file and
 * links the two of them.
 */
public interface IEcoreGenModelLinker {

	/**
	 * Creates a new ecore file and a new genmodel file and links
	 * the two of them.
	 * 
	 * @param ecorePath The path (absolute) of the new ecore file
	 * @param genModelPath The path (relative) of the new genmodel file
	 * @param modelProjectPath The path (relative) of the model project
	 */
	void generateGenModel(String ecorePath, String genModelPath,
		String modelProjectPath);
}
