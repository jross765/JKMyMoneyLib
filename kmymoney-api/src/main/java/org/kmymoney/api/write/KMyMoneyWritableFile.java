package org.kmymoney.api.write;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.kmymoney.api.basetypes.complex.KMMQualifSecID;
import org.kmymoney.api.basetypes.simple.KMMSecID;
import org.kmymoney.api.generated.KMYMONEYFILE;
import org.kmymoney.api.numbers.FixedPointNumber;
import org.kmymoney.api.read.KMyMoneyFile;
import org.kmymoney.api.write.hlp.KMyMoneyWritableObject;

/**
 * Extension of GnucashFile that allows writing. <br/>
 * All the instances for accounts,... it returns can be assumed
 * to implement the respetive *Writable-interfaces.
 *
 * @see KMyMoneyFile
 * @see org.kmymoney.write.impl.KMyMoneyWritableFileImpl
 */
public interface KMyMoneyWritableFile extends KMyMoneyFile, 
                                              KMyMoneyWritableObject 
{

	/**
	 * @return true if this file has been modified.
	 */
	boolean isModified();

	/**
	 * The value is guaranteed not to be bigger then the maximum of the current
	 * system-time and the modification-time in the file at the time of the last
	 * (full) read or sucessfull write.<br/ It is thus suitable to detect if the
	 * file has been modified outside of this library
	 * 
	 * @return the time in ms (compatible with File.lastModified) of the last
	 *         write-operation
	 */
	long getLastWriteTime();

	/**
	 * @param pB true if this file has been modified.
	 * @see {@link #isModified()}
	 */
	void setModified(boolean pB);

	/**
	 * Write the data to the given file. That file becomes the new file returned by
	 * {@link KMyMoneyFile#getKMyMoneyFile()}
	 * 
	 * @param file the file to write to
	 * @throws IOException kn io-poblems
	 */
	void writeFile(File file) throws IOException;

	/**
	 * @return the underlying JAXB-element
	 */
	@SuppressWarnings("exports")
	KMYMONEYFILE getRootElement();

	// ---------------------------------------------------------------

	/**
	 * @see KMyMoneyFile#getAccountByName(String)
	 * @param name the name to look for
	 * @return A changable version of the account.
	 */
	KMyMoneyWritableAccount getWritableAccountByName(String name);

	/**
	 * @param type the type to look for
	 * @return A changable version of all accounts of that type.
	 */
	Collection<KMyMoneyWritableAccount> getWritableAccountsByType(String type);

	/**
	 * @see KMyMoneyFile#getAccountByID(String)
	 * @param id the id of the account to fetch
	 * @return A changable version of the account or null of not found.
	 */
	KMyMoneyWritableAccount getWritableAccountByID(String id);

	/**
	 *
	 * @return a read-only collection of all accounts
	 */
	Collection<? extends KMyMoneyWritableAccount> getWritableAccounts();

	/**
	 *
	 * @return a read-only collection of all accounts that have no parent
	 */
	Collection<? extends KMyMoneyWritableAccount> getWritableRootAccounts();

	// ----------------------------

	/**
	 * @return a new account that is already added to this file as a top-level
	 *         account
	 */
	KMyMoneyWritableAccount createWritableAccount();

	// -----------------------------------------------------------

	/**
	 * @param impl the account to remove
	 */
	void removeAccount(KMyMoneyWritableAccount impl);

	// ---------------------------------------------------------------

	/**
	 * @see KMyMoneyFile#getTransactionByID(String)
	 * @return A changable version of the transaction.
	 */
	KMyMoneyWritableTransaction getWritableTransactionByID(String id);
	
	/**
	 * @see KMyMoneyFile#getTransactions()
	 * @return writable versions of all transactions in the book.
	 */
	Collection<? extends KMyMoneyWritableTransaction> getWritableTransactions();

	// ----------------------------

	/**
	 * @return a new transaction with no splits that is already added to this file
	 */
	KMyMoneyWritableTransaction createWritableTransaction();

	/**
	 *
	 * @param impl the transaction to remove.
	 */
	void removeTransaction(KMyMoneyWritableTransaction impl);

	// ---------------------------------------------------------------

	/**
	 * @param id the unique id of the customer to look for
	 * @return the customer or null if it's not found
	 */
	KMyMoneyWritablePayee getWritablePayeeByID(String id);

	// ----------------------------

	/**
	 * @return a new customer with no values that is already added to this file
	 */
	KMyMoneyWritablePayee createWritablePayee();

	// ---------------------------------------------------------------

	/**
	 * @see KMyMoneyFile#getTransactionByID(String)
	 * @return A changable version of the transaction.
	 */
	KMyMoneyWritableSecurity getWritableSecurityByID(KMMSecID secID);
	
	KMyMoneyWritableSecurity getWritableSecurityByQualifID(KMMQualifSecID qualifID);
	
	/**
	 * @see KMyMoneyFile#getTransactions()
	 * @return writable versions of all transactions in the book.
	 */
	Collection<KMyMoneyWritableSecurity> getWritableSecurities();

	// ----------------------------

	/**
	 * @return a new transaction with no splits that is already added to this file
	 */
	KMyMoneyWritableSecurity createWritableSecurity();

	/**
	 *
	 * @param sec the transaction to remove.
	 */
	void removeSecurity(KMyMoneyWritableSecurity sec);

	// ---------------------------------------------------------------

	/**
	 * Add a new currency.<br/>
	 * If the currency already exists, add a new price-quote for it.
	 * 
	 * @param pCmdtySpace        the namespace (e.g. "GOODS" or "CURRENCY")
	 * @param pCmdtyId           the currency-name
	 * @param conversionFactor   the conversion-factor from the base-currency (EUR).
	 * @param pCmdtyNameFraction number of decimal-places after the comma
	 * @param pCmdtyName         common name of the new currency
	 */
	public void addCurrency(final String pCmdtySpace, final String pCmdtyId, final FixedPointNumber conversionFactor,
			final int pCmdtyNameFraction, final String pCmdtyName);

}