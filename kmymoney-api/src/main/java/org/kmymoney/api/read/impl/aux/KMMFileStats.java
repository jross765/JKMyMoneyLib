package org.kmymoney.api.read.impl.aux;

import org.kmymoney.api.read.impl.KMyMoneyFileImpl;
import org.kmymoney.api.read.impl.hlp.FileStats;
import org.kmymoney.api.read.impl.hlp.FileStats_Cache;
import org.kmymoney.api.read.impl.hlp.FileStats_Counters;
import org.kmymoney.api.read.impl.hlp.FileStats_Raw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KMMFileStats {

	public enum Type {
		RAW, 
		COUNTER, 
		CACHE
	}

	// ---------------------------------------------------------------

	private static final Logger LOGGER = LoggerFactory.getLogger(KMMFileStats.class);

	// ---------------------------------------------------------------

	private FileStats_Raw raw;
	private FileStats_Counters cnt;
	private FileStats_Cache che;

	// ---------------------------------------------------------------

	public KMMFileStats(KMyMoneyFileImpl kmmFile) {
		raw = new FileStats_Raw(kmmFile);
		cnt = new FileStats_Counters(kmmFile);
		che = new FileStats_Cache(kmmFile);
	}

	// ---------------------------------------------------------------

	public int getNofEntriesInstitutions(Type type) {
		if ( type == Type.RAW ) {
			return raw.getNofEntriesInstitutions();
		} else if ( type == Type.COUNTER ) {
			return cnt.getNofEntriesInstitutions();
		} else if ( type == Type.CACHE ) {
			return che.getNofEntriesInstitutions();
		}

		return FileStats.ERROR; // Compiler happy
	}

	public int getNofEntriesAccounts(Type type) {
		if ( type == Type.RAW ) {
			return raw.getNofEntriesAccounts();
		} else if ( type == Type.COUNTER ) {
			return cnt.getNofEntriesAccounts();
		} else if ( type == Type.CACHE ) {
			return che.getNofEntriesAccounts();
		}

		return FileStats.ERROR; // Compiler happy
	}

	public int getNofEntriesTransactions(Type type) {
		if ( type == Type.RAW ) {
			return raw.getNofEntriesTransactions();
		} else if ( type == Type.COUNTER ) {
			return cnt.getNofEntriesTransactions();
		} else if ( type == Type.CACHE ) {
			return che.getNofEntriesTransactions();
		}

		return FileStats.ERROR; // Compiler happy
	}

	public int getNofEntriesTransactionSplits(Type type) {
		if ( type == Type.RAW ) {
			return raw.getNofEntriesTransactionSplits();
		} else if ( type == Type.COUNTER ) {
			return cnt.getNofEntriesTransactionSplits();
		} else if ( type == Type.CACHE ) {
			return che.getNofEntriesTransactionSplits();
		}

		return FileStats.ERROR; // Compiler happy
	}

	// ----------------------------

	public int getNofEntriesPayees(Type type) {
		if ( type == Type.RAW ) {
			return raw.getNofEntriesPayees();
		} else if ( type == Type.COUNTER ) {
			return cnt.getNofEntriesPayees();
		} else if ( type == Type.CACHE ) {
			return che.getNofEntriesPayees();
		}

		return FileStats.ERROR; // Compiler happy
	}

	// ----------------------------

	public int getNofEntriesSecurities(Type type) {
		if ( type == Type.RAW ) {
			return raw.getNofEntriesSecurities();
		} else if ( type == Type.COUNTER ) {
			return cnt.getNofEntriesSecurities();
		} else if ( type == Type.CACHE ) {
			return che.getNofEntriesSecurities();
		}

		return FileStats.ERROR; // Compiler happy
	}

	public int getNofEntriesCurrencies(Type type) {
		if ( type == Type.RAW ) {
			return raw.getNofEntriesCurrencies();
		} else if ( type == Type.COUNTER ) {
			return cnt.getNofEntriesCurrencies();
		} else if ( type == Type.CACHE ) {
			return che.getNofEntriesCurrencies();
		}

		return FileStats.ERROR; // Compiler happy
	}

	// ----------------------------

	public int getNofEntriesPricePairs(Type type) {
		if ( type == Type.RAW ) {
			return raw.getNofEntriesPricePairs();
		} else if ( type == Type.COUNTER ) {
			return cnt.getNofEntriesPricePairs();
		} else if ( type == Type.CACHE ) {
			return che.getNofEntriesPricePairs();
		}

		return FileStats.ERROR; // Compiler happy
	}

	public int getNofEntriesPrices(Type type) {
		if ( type == Type.RAW ) {
			return raw.getNofEntriesPrices();
		} else if ( type == Type.COUNTER ) {
			return cnt.getNofEntriesPrices();
		} else if ( type == Type.CACHE ) {
			return che.getNofEntriesPrices();
		}

		return FileStats.ERROR; // Compiler happy
	}
	
	// ---------------------------------------------------------------
	
	public boolean equals(KMMFileStats other) {
		if ( other.getNofEntriesInstitutions(Type.RAW)     != getNofEntriesInstitutions(Type.RAW) ||
			 other.getNofEntriesInstitutions(Type.COUNTER) != getNofEntriesInstitutions(Type.COUNTER) ||
			 other.getNofEntriesInstitutions(Type.CACHE)   != getNofEntriesInstitutions(Type.CACHE)) {
			return false;
		}
			
		if ( other.getNofEntriesAccounts(Type.RAW)     != getNofEntriesAccounts(Type.RAW) ||
			 other.getNofEntriesAccounts(Type.COUNTER) != getNofEntriesAccounts(Type.COUNTER) ||
			 other.getNofEntriesAccounts(Type.CACHE)   != getNofEntriesAccounts(Type.CACHE)) {
			return false;
		}
		
		if ( other.getNofEntriesTransactions(Type.RAW)     != getNofEntriesTransactions(Type.RAW) ||
			 other.getNofEntriesTransactions(Type.COUNTER) != getNofEntriesTransactions(Type.COUNTER) ||
			 other.getNofEntriesTransactions(Type.CACHE)   != getNofEntriesTransactions(Type.CACHE)) {
			return false;
		}
			
		if ( other.getNofEntriesTransactionSplits(Type.RAW)     != getNofEntriesTransactionSplits(Type.RAW) ||
			 other.getNofEntriesTransactionSplits(Type.COUNTER) != getNofEntriesTransactionSplits(Type.COUNTER) ||
			 other.getNofEntriesTransactionSplits(Type.CACHE)   != getNofEntriesTransactionSplits(Type.CACHE)) {
			return false;
		}
				
		if ( other.getNofEntriesPayees(Type.RAW)     != getNofEntriesPayees(Type.RAW) ||
			 other.getNofEntriesPayees(Type.COUNTER) != getNofEntriesPayees(Type.COUNTER) ||
			 other.getNofEntriesPayees(Type.CACHE)   != getNofEntriesPayees(Type.CACHE)) {
			return false;
		}
					
		if ( other.getNofEntriesSecurities(Type.RAW)     != getNofEntriesSecurities(Type.RAW) ||
			 other.getNofEntriesSecurities(Type.COUNTER) != getNofEntriesSecurities(Type.COUNTER) ||
			 other.getNofEntriesSecurities(Type.CACHE)   != getNofEntriesSecurities(Type.CACHE)) {
			return false;
		}
						
		if ( other.getNofEntriesCurrencies(Type.RAW)     != getNofEntriesCurrencies(Type.RAW) ||
			 other.getNofEntriesCurrencies(Type.COUNTER) != getNofEntriesCurrencies(Type.COUNTER) ||
			 other.getNofEntriesCurrencies(Type.CACHE)   != getNofEntriesCurrencies(Type.CACHE)) {
			return false;
		}
							
		if ( other.getNofEntriesPricePairs(Type.RAW)     != getNofEntriesPricePairs(Type.RAW) ||
			 other.getNofEntriesPricePairs(Type.COUNTER) != getNofEntriesPricePairs(Type.COUNTER) ||
			 other.getNofEntriesPricePairs(Type.CACHE)   != getNofEntriesPricePairs(Type.CACHE)) {
			return false;
		}
								
		if ( other.getNofEntriesPrices(Type.RAW)     != getNofEntriesPrices(Type.RAW) ||
			 other.getNofEntriesPrices(Type.COUNTER) != getNofEntriesPrices(Type.COUNTER) ||
			 other.getNofEntriesPrices(Type.CACHE)   != getNofEntriesPrices(Type.CACHE)) {
			return false;
		}
									
		return true;
	}

}
