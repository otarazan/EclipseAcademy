/**
 */
package org.eclipse.emf.ecp.view.treemasterview.model.treemasterview;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.TreemasterviewFactory
 * @model kind="package"
 * @generated
 */
public interface TreemasterviewPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "treemasterview";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/emf/ecp/view/treemasterview";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.emf.ecp.view";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TreemasterviewPackage eINSTANCE = org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl.TreemasterviewPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl.TreeMasterViewImpl <em>Tree Master View</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl.TreeMasterViewImpl
	 * @see org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl.TreemasterviewPackageImpl#getTreeMasterView()
	 * @generated
	 */
	int TREE_MASTER_VIEW = 0;

	/**
	 * The number of structural features of the '<em>Tree Master View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE_MASTER_VIEW_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Tree Master View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE_MASTER_VIEW_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.TreeMasterView <em>Tree Master View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tree Master View</em>'.
	 * @see org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.TreeMasterView
	 * @generated
	 */
	EClass getTreeMasterView();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TreemasterviewFactory getTreemasterviewFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl.TreeMasterViewImpl <em>Tree Master View</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl.TreeMasterViewImpl
		 * @see org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl.TreemasterviewPackageImpl#getTreeMasterView()
		 * @generated
		 */
		EClass TREE_MASTER_VIEW = eINSTANCE.getTreeMasterView();

	}

} //TreemasterviewPackage
