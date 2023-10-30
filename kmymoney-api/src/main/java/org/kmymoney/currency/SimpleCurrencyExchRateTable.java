package org.kmymoney.currency;

import java.io.Serializable;
import java.util.Collection;
import java.util.Currency;
import java.util.Hashtable;
import java.util.Map;

import org.kmymoney.numbers.FixedPointNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCurrencyExchRateTable implements SimplePriceTable,
                                                    Serializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCurrencyExchRateTable.class);

    private static final long serialVersionUID = -1650896703880682721L;

    // -----------------------------------------------------------

    /**
     * maps a currency-name in capital letters(e.g. "GBP") to a factor
     * {@link FixedPointNumber} that is to be multiplied with an amount of that
     * currency to get the value in the base-currency.
     *
     * @see {@link #getConversionFactor(String)}
     */
    private Map<String, FixedPointNumber> mIso4217CurrCodes2Factor = null;

    // -----------------------------------------------------------

    public SimpleCurrencyExchRateTable() {
	// super();
	mIso4217CurrCodes2Factor = new Hashtable<String, FixedPointNumber>();
	
	setConversionFactor("EUR", new FixedPointNumber(1));
	// setConversionFactor("GBP", new FixedPointNumber("769/523"));
    }

    // -----------------------------------------------------------

    /**
     * @param iso4217CurrCode a currency-name in capital letters(e.g. "GBP")
     * @return a factor {@link FixedPointNumber} that is to be multiplied with an
     *         amount of that currency to get the value in the base-currency.
     */
    public FixedPointNumber getConversionFactor(final String iso4217CurrCode) {
	return mIso4217CurrCodes2Factor.get(iso4217CurrCode);
    }

    /**
     * @param iso4217CurrCode a currency-name in capital letters(e.g. "GBP")
     * @param factor          a factor {@link FixedPointNumber} that is to be
     *                        multiplied with an amount of that currency to get the
     *                        value in the base-currency.
     */
    public void setConversionFactor(final String iso4217CurrCode, final FixedPointNumber factor) {
	mIso4217CurrCodes2Factor.put(iso4217CurrCode, factor);
    }

    // ---------------------------------------------------------------

    /**
     * @param value               the value to convert
     * @param iso4217CurrencyCode the currency to convert to
     * @return false if the conversion is not possible
     */
    public boolean convertFromBaseCurrency(final FixedPointNumber value, final String iso4217CurrencyCode) {
        FixedPointNumber factor = getConversionFactor(iso4217CurrencyCode);
        if (factor == null) {
            return false;
        }
        value.divideBy(factor);
        return true;
    }

    /**
     * @param value               the value to convert
     * @param iso4217CurrencyCode it's currency
     * @return false if the conversion is not possible
     */
    public boolean convertToBaseCurrency(final FixedPointNumber value, final String iso4217CurrencyCode) {
	FixedPointNumber factor = getConversionFactor(iso4217CurrencyCode);
	if (factor == null) {
	    return false;
	}
	value.multiply(factor);
	return true;
    }

    /**
     * @param value     the value to convert
     * @param pCurrency the currency to convert to
     * @return false if the conversion is not possible
     */
    public boolean convertToBaseCurrency(final FixedPointNumber value, final Currency pCurrency) {
	return convertFromBaseCurrency(value, pCurrency.getCurrencyCode());
    }

    // ---------------------------------------------------------------

    /**
     * @return all currency-names
     */
    public Collection<String> getCurrencies() {
	return mIso4217CurrCodes2Factor.keySet();
    }

    // ---------------------------------------------------------------

    /**
     * forget all conversion-factors.
     */
    public void clear() {
        mIso4217CurrCodes2Factor.clear();
    }

    @Override
    public String toString() {
	String result = "[SimpleCurrencyExchRateTable:\n";
	
	result += "No. of entries: " + mIso4217CurrCodes2Factor.size() + "\n";
	
	result += "Entries:\n";
	for ( String currCode : mIso4217CurrCodes2Factor.keySet() ) {
	    // result += " - " + currCode + "\n";
	    result += " - " + currCode + ";" + mIso4217CurrCodes2Factor.get(currCode) + "\n";
	}
	
	result += "]";
	
	return result;
    }

}
