// $Header: svn://gila_svn_priv/finanzen/KMyMoneyToolsXML/trunk/KMyMoneyToolsXML/src/main/java/de/riegelmuenchen/kmymoney/tools/xml/gen/GenTrx.java 8590 2024-02-16 08:43:48Z thilo $

package org.example.kmymoneyapi.write;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.kmymoney.api.basetypes.simple.KMMAcctID;
import org.kmymoney.api.basetypes.simple.KMMPyeID;
import org.kmymoney.api.numbers.FixedPointNumber;
import org.kmymoney.api.write.KMyMoneyWritableTransaction;
import org.kmymoney.api.write.KMyMoneyWritableTransactionSplit;
import org.kmymoney.api.write.impl.KMyMoneyWritableFileImpl;

public class GenTrx {
    // BEGIN Example data -- adapt to your needs
    private static String kmmInFileName  = "example_in.xml";
    private static String kmmOutFileName = "example_out.xml";
    
    // ---
    
    // Following two account IDs: sic, not KMMComplAcctID
    private static KMMAcctID        fromAcct1ID  = new KMMAcctID("A000066"); // Asset:Barvermögen:Bargeld:Kasse Ada
    private static KMMAcctID        toAcct1ID    = new KMMAcctID("A000010"); // Expense:Bildung:Zeitungen
    private static KMMPyeID         pye1ID       = new KMMPyeID("P000007");  // optional, may be null
    private static KMyMoneyWritableTransactionSplit.Action act1 = null;      // Do not set here 
    private static FixedPointNumber amt1         = new FixedPointNumber("1234/100");
    private static FixedPointNumber qty1         = amt1;
    private static LocalDate        datPst1      = LocalDate.of(2024, 2, 15);
    private static String           descr1       = "Bahnhof Zeitungskiosk";
    
    // ---
    
    // Following two account IDs: sic, not KMMComplAcctID
    private static KMMAcctID        fromAcct2ID  = new KMMAcctID("A000004"); // Asset:Barvermögen:Giro RaiBa
    private static KMMAcctID        toAcct2ID    = new KMMAcctID("A000063"); // Asset:Finanzanlagen:Depot RaiBa:DE0007164600 SAP
    private static KMMPyeID         pye2ID       = null;                     // Do not set here
    private static KMyMoneyWritableTransactionSplit.Action act2 = KMyMoneyWritableTransactionSplit.Action.BUY_SHARES;
    private static FixedPointNumber amt2         = new FixedPointNumber("234567/100");
    private static FixedPointNumber qty21        = amt2;
    private static FixedPointNumber qty22        = new FixedPointNumber("15");
    private static LocalDate        datPst2      = LocalDate.of(2024, 1, 15);
    private static String           descr2       = "Aktienkauf";
    // END Example data

    // -----------------------------------------------------------------

    public static void main(String[] args) {
	try {
	    GenTrx tool = new GenTrx();
	    tool.kernel();
	} catch (Exception exc) {
	    System.err.println("Execution exception. Aborting.");
	    exc.printStackTrace();
	    System.exit(1);
	}
    }

    protected void kernel() throws Exception {
	KMyMoneyWritableFileImpl kmmFile = new KMyMoneyWritableFileImpl(new File(kmmInFileName));

	System.out.println("---------------------------");
	System.out.println("Generate transaction no. 1:");
	System.out.println("---------------------------");
	genTrx1(kmmFile);

	System.out.println("");
	System.out.println("---------------------------");
	System.out.println("Generate transaction no. 2:");
	System.out.println("---------------------------");
	genTrx2(kmmFile);
	
	System.out.println("");
	System.out.println("---------------------------");
	System.out.println("Write file:");
	System.out.println("---------------------------");
	kmmFile.writeFile(new File(kmmOutFileName));
	System.out.println("OK");
    }

    private void genTrx1(KMyMoneyWritableFileImpl kmmFile) throws IOException {
	System.err.println("Account 1 name (from): '" + kmmFile.getAccountByID(fromAcct1ID).getQualifiedName() + "'");
	System.err.println("Account 2 name (to):   '" + kmmFile.getAccountByID(toAcct1ID).getQualifiedName() + "'");

	// ---

	KMyMoneyWritableTransaction trx = kmmFile.createWritableTransaction();
	// Does not work like that: The description/memo on transaction
	// level is purely internal:
	// trx.setDescription(description);
	trx.setDescription("Generated by GenTrx, " + LocalDateTime.now());

	// ---

	KMyMoneyWritableTransactionSplit split1 = trx.createWritableSplit(kmmFile.getAccountByID(fromAcct1ID));
	split1.setValue(new FixedPointNumber(amt1.negate()));
	split1.setShares(new FixedPointNumber(qty1.negate()));
	split1.setPayeeID(pye1ID);
	// This is what we actually want (cf. above):
	split1.setDescription(descr1);

	// ---

	KMyMoneyWritableTransactionSplit split2 = trx.createWritableSplit(kmmFile.getAccountByID(toAcct1ID));
	split2.setValue(new FixedPointNumber(amt1));
	split2.setShares(new FixedPointNumber(qty1));
	split2.setPayeeID(pye1ID);
	// Cf. above
	split2.setDescription(descr1);

	// ---

	trx.setDatePosted(datPst1);
	trx.setDateEntered(LocalDate.now());

	// ---

	System.out.println("Transaction to write: " + trx.toString());
    }

    private void genTrx2(KMyMoneyWritableFileImpl kmmFile) throws IOException {
	System.err.println("Account 2 name (from): '" + kmmFile.getAccountByID(fromAcct2ID).getQualifiedName() + "'");
	System.err.println("Account 2 name (to):   '" + kmmFile.getAccountByID(toAcct2ID).getQualifiedName() + "'");

	// ---

	KMyMoneyWritableTransaction trx = kmmFile.createWritableTransaction();
	// Does not work like that: The description/memo on transaction
	// level is purely internal:
	// trx.setDescription(description);
	trx.setDescription("Generated by GenTrx, " + LocalDateTime.now());

	// ---

	KMyMoneyWritableTransactionSplit split1 = trx.createWritableSplit(kmmFile.getAccountByID(fromAcct2ID));
	split1.setValue(new FixedPointNumber(amt2.negate()));
	split1.setShares(new FixedPointNumber(qty21.negate()));
	// This is what we actually want (cf. above):
	split1.setDescription(descr2);

	// ---

	KMyMoneyWritableTransactionSplit split2 = trx.createWritableSplit(kmmFile.getAccountByID(toAcct2ID));
	split2.setValue(new FixedPointNumber(amt2));
	split2.setShares(new FixedPointNumber(qty22));
	split2.setAction(act2);
	// Cf. above
	split2.setDescription(descr2);

	// ---

	trx.setDatePosted(datPst2);
	trx.setDateEntered(LocalDate.now());

	// ---

	System.out.println("Transaction to write: " + trx.toString());
    }

}
