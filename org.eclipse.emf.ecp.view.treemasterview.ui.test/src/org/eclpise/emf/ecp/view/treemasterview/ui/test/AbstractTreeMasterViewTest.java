package org.eclpise.emf.ecp.view.treemasterview.ui.test;

import static org.junit.Assert.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecp.ui.view.test.HierarchyViewModelHandle;
import org.junit.Test;

public class AbstractTreeMasterViewTest {

	private static EClass horizontalClass;
	
	@Test
	public void testTreeMasterViewWithoutChildren() {
		final HierarchyViewModelHandle horizontalHandle = createHorizontalWithoutChildren();
		final Node<Renderable> node = buildNode(horizontalHandle.getRoot());
		assertEquals(1, ViewTestHelper.countNodes(node));
		assertEquals(horizontalHandle.getRoot(), node.getRenderable());
	}

	private HierarchyViewModelHandle createHorizontalWithoutChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
