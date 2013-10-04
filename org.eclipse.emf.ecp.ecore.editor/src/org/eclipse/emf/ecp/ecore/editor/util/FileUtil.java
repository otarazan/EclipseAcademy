package org.eclipse.emf.ecp.ecore.editor.util;

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
					return fileToBeDeleted.delete();
				return true;
			}
		} else
			return false;
	}
}
