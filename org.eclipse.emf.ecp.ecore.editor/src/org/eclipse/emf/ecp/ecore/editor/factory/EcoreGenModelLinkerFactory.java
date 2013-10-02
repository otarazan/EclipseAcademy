package org.eclipse.emf.ecp.ecore.editor.factory;

import org.eclipse.emf.ecp.ecore.editor.IEcoreGenModelLinker;
import org.eclipse.emf.ecp.ecore.editor.internal.EcoreGenModelLinkerImpl;

public class EcoreGenModelLinkerFactory {

	public static IEcoreGenModelLinker getEcoreGenModelLinker() {
		return new EcoreGenModelLinkerImpl();
	}

}
