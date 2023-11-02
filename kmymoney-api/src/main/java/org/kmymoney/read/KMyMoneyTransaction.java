package org.kmymoney.read;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.kmymoney.generated.TRANSACTION;
import org.kmymoney.numbers.FixedPointNumber;

/**
 * It is comparable and sorts primarily on the date the transaction happened
 * and secondarily on the date it was entered.
 */
public interface KMyMoneyTransaction extends Comparable<KMyMoneyTransaction> {

    // For the following enumarations, cf.:
    // https://github.com/KDE/kmymoney/blob/master/kmymoney/mymoney/mymoneyenums.h

    // ::MAGIC
    // ::TODO Convert to enum
    public static final int INVEST_TYPE_UNKNOWN           = -1;
    public static final int INVEST_TYPE_BUY_SHARES        = 0;
    public static final int INVEST_TYPE_SELL_SHARES       = 1;
    public static final int INVEST_TYPE_DIVIVEND          = 2;
    public static final int INVEST_TYPE_REINVEST_DIVIVEND = 3;
    public static final int INVEST_TYPE_YIELD             = 4;
    public static final int INVEST_TYPE_ADD_SHARES        = 5;
    public static final int INVEST_TYPE_REMOVE_SHARES     = 6;
    public static final int INVEST_TYPE_SPLIT_SHARES      = 7;
    public static final int INVEST_TYPE_INTEREST_INCOME   = 8;
    
    // ::MAGIC
    // ::TODO Convert to enum
    public static final int INVEST_ACTION_NONE              = 0;
    public static final int INVEST_ACTION_BUY               = 1;
    public static final int INVEST_ACTION_SELL              = 2;
    public static final int INVEST_ACTION_REINVEST_DIVIDEND = 3;
    public static final int INVEST_ACTION_CASH_DIVIDEND     = 4;
    public static final int INVEST_ACTION_SHARES_IN         = 5;
    public static final int INVEST_ACTION_SHARES_OUT        = 6;
    public static final int INVEST_ACTION_STOCK_SPLIT       = 7;
    public static final int INVEST_ACTION_FEES              = 8;
    public static final int INVEST_ACTION_INTEREST          = 9;
    public static final int INVEST_ACTION_INVALID           = 10;
    
    // ---------------------------------------------------------------
	    
    @SuppressWarnings("exports")
    TRANSACTION getJwsdpPeer();

    /**
     * The gnucash-file is the top-level class to contain everything.
     * 
     * @return the file we are associated with
     */
    KMyMoneyFile getKMyMoneyFile();

    // ----------------------------------------------------------------

    /**
     *
     * @return the unique-id to identify this object with across name- and
     *         hirarchy-changes
     */
    String getId();

    /**
     * @return the user-defined description for this object (may contain multiple
     *         lines and non-ascii-characters)
     */
    String getMemo();

    /**
     *
     * @return the date the transaction was entered into the system
     */
    LocalDate getEntryDate();

    /**
     *
     * @return the date the transaction happened
     */
    LocalDate getDatePosted();

    /**
     *
     * @return date the transaction happened
     */
    String getDatePostedFormatted();

    // ----------------------------------------------------------------

    /**
     * Do not modify the returned collection!
     * 
     * @return all splits of this transaction.
     */
    List<KMyMoneyTransactionSplit> getSplits();

    /**
     * Get a split of this transaction it's id.
     * 
     * @param id the id to look for
     * @return null if not found
     */
    KMyMoneyTransactionSplit getSplitByID(String id);

    /**
     *
     * @return the first split of this transaction or null.
     * @throws SplitNotFoundException
     */
    KMyMoneyTransactionSplit getFirstSplit() throws SplitNotFoundException;

    /**
     * @return the second split of this transaction or null.
     * @throws SplitNotFoundException
     */
    KMyMoneyTransactionSplit getSecondSplit() throws SplitNotFoundException;

    /**
     *
     * @return the number of splits in this transaction.
     */
    int getSplitsCount();

    /**
     *
     * @return true if the sum of all splits adds up to zero.
     */
    boolean isBalanced();

    String getCommodity();

    /**
     * The result is in the currency of the transaction.<br/>
     * if the transaction is unbalanced, get sum of all split-values.
     * 
     * @return the sum of all splits
     * @see #isBalanced()
     */
    FixedPointNumber getBalance();

    /**
     * The result is in the currency of the transaction.
     * 
     * @see KMyMoneyTransaction#getBalance()
     */
    String getBalanceFormatted();

    /**
     * The result is in the currency of the transaction.
     * 
     * @see KMyMoneyTransaction#getBalance()
     */
    String getBalanceFormatted(Locale lcl);

    /**
     * The result is in the currency of the transaction.<br/>
     * if the transaction is unbalanced, get the missing split-value to balance it.
     * 
     * @return the sum of all splits
     * @see #isBalanced()
     */
    FixedPointNumber getNegatedBalance();

    /**
     * The result is in the currency of the transaction.
     * 
     * @see KMyMoneyTransaction#getNegatedBalance()
     */
    String getNegatedBalanceFormatted();

    /**
     * The result is in the currency of the transaction.
     * 
     * @see KMyMoneyTransaction#getNegatedBalance()
     */
    String getNegatedBalanceFormatted(Locale lcl);
}
