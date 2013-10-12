/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.ecore.editor.util;

/**
 * Computes several strings like a namespace URL based on a project name.
 */
public class ProjectHelper {

	/**
	 * @param projectFullName The projects full name which should be used
	 *            for the computations.
	 */
	public ProjectHelper(String projectFullName) {
		this.projectFullName = projectFullName;
	}

	private String projectFullName;

	/**
	 * Returns the simple project name.
	 * Example: Will return "project" if the full project name is "org.eclipse.project".
	 * 
	 * @return The simple project name based on the full project name
	 */
	public String getProjectName() {
		if (getProjectFullName() == null) {
			return "";
		}
		final String[] split = getProjectFullName().split("\\.");
		if (split.length <= 0) {
			return "";
		}
		final String lastSegment = split[split.length - 1];
		if (lastSegment.toLowerCase().equals("model") && split.length > 1) {
			return split[split.length - 2];
		}
		return lastSegment;
	}

	/**
	 * Returns the namespace prefix.
	 * Example: Will return "org.eclipse" if the full project name is "org.eclipse.project".
	 * 
	 * @return The namespace based on the full project name
	 */
	public String getNSPrefix() {
		return projectFullName.substring(0, projectFullName.lastIndexOf("."));
	}

	/**
	 * Returns the namespace URL.
	 * Example: Will return "http://eclipse.org/project" if the full project name is "org.eclipse.project".
	 * 
	 * @return The namespace URL based on the full project name
	 */
	public String getNSURL() {
		final String[] split = projectFullName.split("\\.");

		final String temp = split[0];
		split[0] = split[1];
		split[1] = temp;

		String result = join(split, "/");
		final StringBuilder mystring = new StringBuilder(result);
		mystring.setCharAt(result.indexOf('/'), '.');
		result = "http://" + mystring;

		return result;
	}

	// join(String array,delimiter)
	private String join(String[] r, String d) {
		if (r.length == 0) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		int i;
		for (i = 0; i < r.length - 1; i++) {
			sb.append(r[i] + d);
		}
		return sb.toString() + r[i];
	}

	/**
	 * @return The full project name which is used for the computations.
	 */
	public String getProjectFullName() {
		return projectFullName;
	}

	/**
	 * Sets the projects full name which is used for the computations.
	 * 
	 * @param projectFullName The full name of the project
	 */
	public void setProjectFullName(String projectFullName) {
		this.projectFullName = projectFullName;
	}
}
