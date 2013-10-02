package org.eclipse.emf.ecp.ecore.editor;

public interface IEcoreGenModelLinker {
	public void generateGenModel(String ecorePath, String genModelPath,
			String modelProjectPath, String editProjectPath,
			String editorProjectPath);
}
