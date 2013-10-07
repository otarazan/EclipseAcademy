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

public class FileUtil {

	public static boolean delete(File fileToBeDeleted) {
		if (fileToBeDeleted.isDirectory()) {
			if (fileToBeDeleted.list().length == 0)
				return fileToBeDeleted.delete();
			else {
				String files[] = fileToBeDeleted.list();
				for (String temp : files) {
					File fileDelete = new File(fileToBeDeleted, temp);
					delete(fileDelete);
				}
				if (fileToBeDeleted.list().length == 0)
					fileToBeDeleted.delete();
				return true;
			}
		} else
			return fileToBeDeleted.delete();
	}
}
