package org.kmymoney.read.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPackage extends TestCase {
    public static void main(String[] args) throws Exception {
	junit.textui.TestRunner.run(suite());
    }

    @SuppressWarnings("exports")
    public static Test suite() throws Exception {
	TestSuite suite = new TestSuite();

	suite.addTest(org.kmymoney.read.impl.TestKMyMoneyFileImpl.suite());
	suite.addTest(org.kmymoney.read.impl.TestKMyMoneyAccountImpl.suite());
	suite.addTest(org.kmymoney.read.impl.TestKMyMoneySecurityImpl.suite());
	suite.addTest(org.kmymoney.read.impl.TestKMyMoneyTransactionImpl.suite());
	// ::TODO
	// suite.addTest(org.kmymoney.read.impl.TestKMyMoneyTransactionSplitImpl.suite());

	// ::TODO
	// suite.addTest(org.kmymoney.read.impl.aux.TestPackage.suite());

	return suite;
    }
}
