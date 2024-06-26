package org.kmymoney.base.basetypes.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A technical entry ID for a transaction
 * (primarily defined for type safety).
 */
public class KMMTrxID extends KMMID {
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(KMMTrxID.class);

    // T 000 000 000 000 000 001
    //   1   1   1   9   6   3
    //   8   5   2
    private final static int  STANDARD_LENGTH = 19;
    private final static char PREFIX = 'T';

    // -----------------------------------------------------------------

    public KMMTrxID() {
    	super();
    }

    public KMMTrxID(String idStr) throws InvalidKMMIDException {
    	super(idStr);
    	set(idStr);
    }

    public KMMTrxID(long counter) throws InvalidKMMIDException {
    	super(counter);
    	set(counter);
    }

    // -----------------------------------------------------------------

    public void set(long counter) throws InvalidKMMIDException {
    	int coreLength = STANDARD_LENGTH - PREFIX_LENGTH;

    	if ( counter < 1 || 
    		 counter > Math.pow(10, coreLength) - 1 )
    		throw new InvalidKMMIDException("Cannot generate KMM transaction ID from long " + counter + ": range error");

    	String fmtStr = "%0" + coreLength + "d";
    	String coreStr = String.format(fmtStr, counter);
    	set(PREFIX + coreStr);
    }

    // -----------------------------------------------------------------

    public void validate() throws InvalidKMMIDException {
    	if ( kmmID.length() != STANDARD_LENGTH )
    		throw new InvalidKMMIDException("No valid KMM transaction ID string: '" + kmmID + "': wrong string length");

    	if ( kmmID.charAt(0) != PREFIX )
    		throw new InvalidKMMIDException("No valid KMM transaction ID string: '" + kmmID + "': wrong prefix");

    	for ( int i = PREFIX_LENGTH; i < STANDARD_LENGTH; i++ ) {
    		if ( ! Character.isDigit(kmmID.charAt(i)) ) {
    			logger.error("Char '" + kmmID.charAt(i) + "' is invalid in KMMTrxID '" + kmmID + "'");
    			throw new InvalidKMMIDException("No valid KMM transaction ID string: '" + kmmID + "': wrong character at pos " + i);
    		}
    	}
    }

    // -----------------------------------------------------------------

    public String getPrefix() throws KMMIDNotSetException {
    	if (!isSet)
    		throw new KMMIDNotSetException();

    	return kmmID.substring(0, PREFIX_LENGTH);
    }

}
