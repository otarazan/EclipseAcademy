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

public class ProjectHelper {

	private String projectFullName;

	public String getProjectName() {
		if (getProjectFullName() == null)
			return "";
		String[] split = getProjectFullName().split("\\.");
		if (split.length <= 0)
			return "";
		String lastSegment = split[split.length - 1];
		if (lastSegment.toLowerCase().equals("model") && split.length > 1)
			return split[split.length - 2];
		return lastSegment;
	}

	public String getNSPrefix() {
		String projectName = getProjectFullName();
		return projectName.substring(0, projectName.lastIndexOf("."));
	}

	public String getNSURL() {
		String[] split = getProjectFullName().split("\\.");

		String temp = split[0];
		split[0] = split[1];
		split[1] = temp;

		String result = join(split, "/");
		StringBuilder mystring = new StringBuilder(result);
		mystring.setCharAt(result.indexOf('/'), '.');
		result = "http://" + mystring;

		return result;
	}

	// join(String array,delimiter)
	public String join(String r[], String d) {
		if (r.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		int i;
		for (i = 0; i < r.length - 1; i++)
			sb.append(r[i] + d);
		return sb.toString() + r[i];
	}

	public String getProjectFullName() {
		return projectFullName;
	}

	public void setProjectFullName(String projectFullName) {
		this.projectFullName = projectFullName;
	}
}
