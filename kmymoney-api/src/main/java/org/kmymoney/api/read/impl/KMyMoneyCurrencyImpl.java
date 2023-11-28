package org.kmymoney.api.read.impl;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.kmymoney.api.basetypes.complex.InvalidQualifSecCurrIDException;
import org.kmymoney.api.basetypes.complex.InvalidQualifSecCurrTypeException;
import org.kmymoney.api.basetypes.complex.KMMQualifCurrID;
import org.kmymoney.api.read.KMMSecCurr;
import org.kmymoney.api.read.KMyMoneyCurrency;
import org.kmymoney.api.read.KMyMoneyFile;
import org.kmymoney.api.read.UnknownRoundingMethodException;
import org.kmymoney.api.read.UnknownSecurityTypeException;
import org.kmymoney.api.read.aux.KMMPrice;
import org.kmymoney.api.generated.CURRENCY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KMyMoneyCurrencyImpl implements KMyMoneyCurrency {

    private static final Logger LOGGER = LoggerFactory.getLogger(KMyMoneyCurrencyImpl.class);

    // ---------------------------------------------------------------
    
    /**
     * the JWSDP-object we are facading.
     */
    private CURRENCY jwsdpPeer;

    /**
     * The file we belong to.
     */
    private final KMyMoneyFile file;

    // ---------------------------------------------------------------

    @SuppressWarnings("exports")
    public KMyMoneyCurrencyImpl(
	    final CURRENCY peer, 
	    final KMyMoneyFile gncFile) {

	jwsdpPeer = peer;
	file = gncFile;
    }

    // ---------------------------------------------------------------

    public KMyMoneyFile getKMyMoneyFile() {
	return file;
    }

    // ---------------------------------------------------------------

    @Override
    public String getId() {
	return jwsdpPeer.getId();
    }

    @Override
    public KMMQualifCurrID getQualifId() throws InvalidQualifSecCurrTypeException, InvalidQualifSecCurrIDException {
	return new KMMQualifCurrID(getId());
    }

    @Override
    public String getSymbol() {
	return jwsdpPeer.getSymbol();
    }

    // ---------------------------------------------------------------

    @Override
    public KMMSecCurr.Type getType() throws UnknownSecurityTypeException {
	BigInteger typeVal = jwsdpPeer.getType(); 
	return KMMSecCurrImpl.getType(typeVal.intValue());
    }

    @Override
    public String getName() {
	return jwsdpPeer.getName();
    }

    @Override
    public BigInteger getPP() {
	return jwsdpPeer.getPp();
    }

    @Override
    public KMMSecCurr.RoundingMethod getRoundingMethod() throws UnknownRoundingMethodException {
	BigInteger methodVal = jwsdpPeer.getRoundingMethod(); 
	return KMMSecCurrImpl.getRoundingMethod(methodVal.intValue());
    }

    @Override
    public BigInteger getSAF() {
	return jwsdpPeer.getSaf();
    }

    @Override
    public BigInteger getSCF() {
	return jwsdpPeer.getScf();
    }

    // ---------------------------------------------------------------

    @Override
    public Collection<KMMPrice> getQuotes() throws InvalidQualifSecCurrTypeException, InvalidQualifSecCurrIDException {
	Collection<KMMPrice> result = new ArrayList<KMMPrice>();
	
	Collection<KMMPrice> prices = getKMyMoneyFile().getPrices();
	for ( KMMPrice price : prices ) {
	    try {
		if ( price.getFromSecCurrQualifId().toString().equals(getQualifId().toString()) ) {
		    result.add(price);
		} 
	    } catch ( Exception exc ) {
		LOGGER.error("getQuotes: Could not check price " + price.toString());
	    }
	}

  	return result;
    }

    @Override
    public KMMPrice getYoungestQuote() throws InvalidQualifSecCurrTypeException, InvalidQualifSecCurrIDException {
	KMMPrice result = null;

	LocalDate youngestDate = LocalDate.of(1970, 1, 1); // ::MAGIC
	for ( KMMPrice price : getQuotes() ) {
	    if ( price.getDate().isAfter(youngestDate) ) {
		result = price;
		youngestDate = price.getDate();
	    }
	}

	return result;
    }

    // ---------------------------------------------------------------

    @Override
    public String toString() {
	String result = "KMyMoneyCurrencyImpl ";
	
	result += "[id=" + getId();
	result += ", symbol='" + getSymbol() + "'";
	
	try {
	    result += ", type=" + getType();
	} catch (UnknownSecurityTypeException e) {
	    result += ", type=" + "ERROR";
	}
	
	result += ", name='" + getName() + "'";
	result += ", pp=" + getPP();
	result += ", saf=" + getSAF();
	result += ", scf=" + getSCF();
	
	try {
	    result += ", rounding-method=" + getRoundingMethod();
	} catch (UnknownRoundingMethodException e) {
	    result += ", rounding-method=" + "ERROR";
	}
	
	result += "]";
	
	return result;
    }

}
