/**
 */
package org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.TreeMasterView;
import org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.TreemasterviewFactory;
import org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.TreemasterviewPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TreemasterviewPackageImpl extends EPackageImpl implements TreemasterviewPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass treeMasterViewEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.ecp.view.treemasterview.model.treemasterview.TreemasterviewPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TreemasterviewPackageImpl() {
		super(eNS_URI, TreemasterviewFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link TreemasterviewPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TreemasterviewPackage init() {
		if (isInited) return (TreemasterviewPackage)EPackage.Registry.INSTANCE.getEPackage(TreemasterviewPackage.eNS_URI);

		// Obtain or create and register package
		TreemasterviewPackageImpl theTreemasterviewPackage = (TreemasterviewPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof TreemasterviewPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new TreemasterviewPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theTreemasterviewPackage.createPackageContents();

		// Initialize created meta-data
		theTreemasterviewPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTreemasterviewPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TreemasterviewPackage.eNS_URI, theTreemasterviewPackage);
		return theTreemasterviewPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTreeMasterView() {
		return treeMasterViewEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreemasterviewFactory getTreemasterviewFactory() {
		return (TreemasterviewFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		treeMasterViewEClass = createEClass(TREE_MASTER_VIEW);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(treeMasterViewEClass, TreeMasterView.class, "TreeMasterView", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //TreemasterviewPackageImpl
