package org.kmymoney.api;

public class ConstTest extends Const {

    public static final String KMM_FILENAME = "test.xml";

    public static final String KMM_FILENAME_IN = KMM_FILENAME;

    public static final String KMM_FILENAME_OUT = "test_out.xml";

    // ---------------------------------------------------------------
    // Stats for above-mentioned KMyMoney test file (before write operations)
    
    public class Stats {
    
    	public static final int NOF_INST      = 2;
    	public static final int NOF_ACCT      = 76;
    	public static final int NOF_TRX       = 19;
    	public static final int NOF_TRX_SPLT  = 39;
	
    	public static final int NOF_PYE       = 9;
	
    	public static final int NOF_SEC       = 2;
    	public static final int NOF_CURR      = 2;
    	public static final int NOF_PRCPR     = 3;
    	public static final int NOF_PRC       = 6;
    
    }

}
