/**
 */
package org.eclipse.emf.ecp.view.treemasterview.model.treemasterview;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.TreemasterviewPackage
 * @generated
 */
public interface TreemasterviewFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TreemasterviewFactory eINSTANCE = org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl.TreemasterviewFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Tree Master View</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tree Master View</em>'.
	 * @generated
	 */
	TreeMasterView createTreeMasterView();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	TreemasterviewPackage getTreemasterviewPackage();

} //TreemasterviewFactory
