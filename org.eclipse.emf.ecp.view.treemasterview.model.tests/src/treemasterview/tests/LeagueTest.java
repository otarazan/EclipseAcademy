/**
 */
package treemasterview.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import treemasterview.League;
import treemasterview.TreemasterviewFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>League</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class LeagueTest extends TestCase {

	/**
	 * The fixture for this League test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected League fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(LeagueTest.class);
	}

	/**
	 * Constructs a new League test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LeagueTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this League test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(League fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this League test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected League getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(TreemasterviewFactory.eINSTANCE.createLeague());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //LeagueTest
