package org.kmymoney.api.read.impl.hlp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.kmymoney.api.basetypes.complex.KMMQualifCurrID;
import org.kmymoney.api.generated.CURRENCY;
import org.kmymoney.api.generated.KMYMONEYFILE;
import org.kmymoney.api.read.KMyMoneyCurrency;
import org.kmymoney.api.read.impl.KMyMoneyCurrencyImpl;
import org.kmymoney.api.read.impl.KMyMoneyFileImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileCurrencyManager {
    
    protected static final Logger LOGGER = LoggerFactory.getLogger(FileCurrencyManager.class);

    // ---------------------------------------------------------------

    private KMyMoneyFileImpl kmmFile;

    private Map<String, KMyMoneyCurrency> currMap;

    // ---------------------------------------------------------------

    public FileCurrencyManager(KMyMoneyFileImpl kmmFile) {
	this.kmmFile = kmmFile;
	init(kmmFile.getRootElement());
    }

    // ---------------------------------------------------------------

    private void init(final KMYMONEYFILE pRootElement) {
	currMap = new HashMap<String, KMyMoneyCurrency>();

	for ( CURRENCY jwsdpCurr : pRootElement.getCURRENCIES().getCURRENCY() ) {
	    try {
		KMyMoneyCurrencyImpl curr = createCurrency(jwsdpCurr);
		currMap.put(jwsdpCurr.getId(), curr);
	    } catch (RuntimeException e) {
		LOGGER.error("init: [RuntimeException] Problem in " + getClass().getName() + ".init: "
			+ "ignoring illegal Currency-Entry with id=" + jwsdpCurr.getId(), e);
	    }
	} // for

	LOGGER.debug("init: No. of entries in currency map: " + currMap.size());
    }

    /**
     * @param jwsdpCurr the JWSDP-peer (parsed xml-element) to fill our object with
     * @return the new KMyMoneyCurrency to wrap the given JAXB object.
     */
    protected KMyMoneyCurrencyImpl createCurrency(final CURRENCY jwsdpCurr) {
	KMyMoneyCurrencyImpl curr = new KMyMoneyCurrencyImpl(jwsdpCurr, kmmFile);
	return curr;
    }

    // ---------------------------------------------------------------

    public void addCurrency(KMyMoneyCurrency curr) {
	currMap.put(curr.getId(), curr);
    }

    public void removeCurrency(KMyMoneyCurrency curr) {
	currMap.remove(curr.getId());
    }

    // ---------------------------------------------------------------

    public KMyMoneyCurrency getCurrencyById(String currID) {
	if (currMap == null) {
	    throw new IllegalStateException("no root-element loaded");
	}

	KMyMoneyCurrency retval = currMap.get(currID);
	if (retval == null) {
	    LOGGER.warn("getCurrencyById: No Currency with ID '" + currID + "'. We know " + currMap.size() + " currencies.");
	}
	
	return retval;
    }

    public KMyMoneyCurrency getCurrencyByQualifId(KMMQualifCurrID currID) {
	return getCurrencyById(currID.getCode());
    }

    public Collection<KMyMoneyCurrency> getCurrencies() {
	return currMap.values();
    }

    // ---------------------------------------------------------------
    
    public int getNofEntriesCurrencyMap() {
	return currMap.size();
    }
    
}
