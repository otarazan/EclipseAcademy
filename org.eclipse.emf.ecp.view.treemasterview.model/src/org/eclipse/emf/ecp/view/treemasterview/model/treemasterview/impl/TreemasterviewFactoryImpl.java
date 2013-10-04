/**
 */
package org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TreemasterviewFactoryImpl extends EFactoryImpl implements TreemasterviewFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TreemasterviewFactory init() {
		try {
			TreemasterviewFactory theTreemasterviewFactory = (TreemasterviewFactory)EPackage.Registry.INSTANCE.getEFactory(TreemasterviewPackage.eNS_URI);
			if (theTreemasterviewFactory != null) {
				return theTreemasterviewFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TreemasterviewFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreemasterviewFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case TreemasterviewPackage.TREE_MASTER_VIEW: return createTreeMasterView();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreeMasterView createTreeMasterView() {
		TreeMasterViewImpl treeMasterView = new TreeMasterViewImpl();
		return treeMasterView;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreemasterviewPackage getTreemasterviewPackage() {
		return (TreemasterviewPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TreemasterviewPackage getPackage() {
		return TreemasterviewPackage.eINSTANCE;
	}

} //TreemasterviewFactoryImpl
