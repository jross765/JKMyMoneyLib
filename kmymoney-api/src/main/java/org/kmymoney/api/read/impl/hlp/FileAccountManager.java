package org.kmymoney.api.read.impl.hlp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kmymoney.api.generated.ACCOUNT;
import org.kmymoney.api.generated.KMYMONEYFILE;
import org.kmymoney.api.read.KMyMoneyAccount;
import org.kmymoney.api.read.KMyMoneyAccount.Type;
import org.kmymoney.api.read.impl.KMyMoneyAccountImpl;
import org.kmymoney.api.read.impl.KMyMoneyFileImpl;
import org.kmymoney.base.basetypes.complex.KMMComplAcctID;
import org.kmymoney.base.basetypes.complex.KMMComplAcctID.Top;
import org.kmymoney.base.basetypes.simple.KMMAcctID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.schnorxoborx.base.beanbase.NoEntryFoundException;
import xyz.schnorxoborx.base.beanbase.TooManyEntriesFoundException;

public class FileAccountManager {

	protected static final Logger LOGGER = LoggerFactory.getLogger(FileAccountManager.class);

	// ---------------------------------------------------------------

	protected KMyMoneyFileImpl kmmFile;

	private Map<KMMComplAcctID, KMyMoneyAccount> acctMap;

	// ---------------------------------------------------------------

	public FileAccountManager(KMyMoneyFileImpl kmmFile) {
		this.kmmFile = kmmFile;
		init(kmmFile.getRootElement());
	}

	// ---------------------------------------------------------------

	private void init(final KMYMONEYFILE pRootElement) {
		acctMap = new HashMap<KMMComplAcctID, KMyMoneyAccount>();

		for ( ACCOUNT jwsdpAcct : pRootElement.getACCOUNTS().getACCOUNT() ) {
			try {
				KMyMoneyAccount acct = createAccount(jwsdpAcct);
				acctMap.put(acct.getID(), acct);
			} catch (RuntimeException e) {
				LOGGER.error("init: [RuntimeException] Problem in " + getClass().getName() + ".init: "
						+ "ignoring illegal Account-Entry with id=" + jwsdpAcct.getId(), e);
			}
		} // for

		LOGGER.debug("init: No. of entries in account map: " + acctMap.size());
	}

	/**
	 * @param jwsdpAcct the JWSDP-peer (parsed xml-element) to fill our object with
	 * @return the new KMyMoneyAccount to wrap the given jaxb-object.
	 */
	protected KMyMoneyAccountImpl createAccount(final ACCOUNT jwsdpAcct) {
		KMyMoneyAccountImpl acct = new KMyMoneyAccountImpl(jwsdpAcct, kmmFile);
		LOGGER.debug("createAccount: Generated new account: " + acct.getID());
		return acct;
	}

	// ---------------------------------------------------------------

	public void addAccount(KMyMoneyAccount acct) {
		acctMap.put(acct.getID(), acct);
		LOGGER.debug("addAccount: Added account to cache: " + acct.getID());
	}

	public void removeAccount(KMyMoneyAccount acct) {
		acctMap.remove(acct.getID());
		LOGGER.debug("removeAccount: Removed account from cache: " + acct.getID());
	}

	// ---------------------------------------------------------------

	public KMyMoneyAccount getAccountByID(final KMMComplAcctID acctID) {
		if ( acctID == null ) {
			throw new IllegalStateException("null account ID given");
		}

		if ( ! acctID.isSet() ) {
			throw new IllegalStateException("unset account ID given");
		}

		if ( acctMap == null ) {
			throw new IllegalStateException("no root-element loaded");
		}

		KMyMoneyAccount retval = acctMap.get(acctID);
		if ( retval == null ) {
			System.err.println("getAccountById: No Account with ID '" + acctID + "'. We know " + acctMap.size() + " accounts.");
		}

		return retval;
	}

	public KMyMoneyAccount getAccountByID(final KMMAcctID acctID) {
		if ( acctID == null ) {
			throw new IllegalArgumentException("null account ID given"); 
		}
		
		if ( ! acctID.isSet() ) {
			throw new IllegalStateException("unset account ID given");
		}

		return getAccountByID(new KMMComplAcctID(acctID));
	}

	public List<KMyMoneyAccount> getAccountsByParentID(final KMMComplAcctID acctID) {
		if ( acctID == null ) {
			throw new IllegalStateException("null account ID given");
		}

		if ( ! acctID.isSet() ) {
			throw new IllegalStateException("unset account ID given");
		}

		if ( acctMap == null ) {
			throw new IllegalStateException("no root-element loaded");
		}

		List<KMyMoneyAccount> retval = new ArrayList<KMyMoneyAccount>();

		for ( KMyMoneyAccount acct : acctMap.values() ) {
			KMMComplAcctID prntID = acct.getParentAccountID();
			if ( prntID == null ) {
				if ( acctID == null ) {
					retval.add((KMyMoneyAccount) acct);
				} else if ( ! acctID.isSet() ) {
					retval.add((KMyMoneyAccount) acct);
				}
			} else {
				if ( prntID.equals(acctID) ) {
					retval.add((KMyMoneyAccount) acct);
				}
			}
		}
		
		retval.sort(Comparator.naturalOrder()); 

		return retval;
	}

	public List<KMyMoneyAccount> getAccountsByParentID(final KMMAcctID acctID) {
		if ( acctID == null ) {
			throw new IllegalStateException("null account ID given");
		}

		if ( ! acctID.isSet() ) {
			throw new IllegalStateException("unset account ID given");
		}

		return getAccountsByParentID(new KMMComplAcctID(acctID));
	}
	
	public List<KMyMoneyAccount> getAccountsByName(final String name) {
		if ( name == null ) {
			throw new IllegalStateException("null name given");
		}

		if ( name.trim().equals("") ) {
			throw new IllegalStateException("empty name given");
		}

		return getAccountsByName(name, true, true);
	}

	public List<KMyMoneyAccount> getAccountsByName(final String expr, boolean qualif, boolean relaxed) {
		if ( expr == null ) {
			throw new IllegalStateException("null expression given");
		}

		if ( expr.trim().equals("") ) {
			throw new IllegalStateException("empty expression given");
		}

		if ( acctMap == null ) {
			throw new IllegalStateException("no root-element loaded");
		}

		List<KMyMoneyAccount> result = new ArrayList<KMyMoneyAccount>();

		for ( KMyMoneyAccount acct : acctMap.values() ) {
			if ( relaxed ) {
				if ( qualif ) {
					if ( acct.getQualifiedName().toLowerCase().contains(expr.trim().toLowerCase()) ) {
						result.add(acct);
					}
				} else {
					if ( acct.getName().toLowerCase().contains(expr.trim().toLowerCase()) ) {
						result.add(acct);
					}
				}
			} else {
				if ( qualif ) {
					if ( acct.getQualifiedName().equals(expr) ) {
						result.add(acct);
					}
				} else {
					if ( acct.getName().equals(expr) ) {
						result.add(acct);
					}
				}
			}
		}

		return result;
	}

	public KMyMoneyAccount getAccountByNameUniq(final String name, final boolean qualif)
			throws NoEntryFoundException, TooManyEntriesFoundException {
		if ( name == null ) {
			throw new IllegalStateException("null name given");
		}

		if ( name.trim().equals("") ) {
			throw new IllegalStateException("empty name given");
		}

		List<KMyMoneyAccount> acctList = getAccountsByName(name, qualif, false);
		if ( acctList.size() == 0 )
			throw new NoEntryFoundException();
		else if ( acctList.size() > 1 )
			throw new TooManyEntriesFoundException();
		else
			return acctList.get(0);
	}

	/*
	 * warning: this function has to traverse all accounts. If it much faster to try
	 * getAccountByID first and only call this method if the returned account does
	 * not have the right name.
	 */
	public KMyMoneyAccount getAccountByNameEx(final String nameRegEx)
			throws NoEntryFoundException, TooManyEntriesFoundException {
		if ( nameRegEx == null ) {
			throw new IllegalStateException("null regular expression given");
		}

		if ( nameRegEx.trim().equals("") ) {
			throw new IllegalStateException("empty regular expression given");
		}

		if ( acctMap == null ) {
			throw new IllegalStateException("no root-element loaded");
		}

		KMyMoneyAccount foundAccount = getAccountByNameUniq(nameRegEx, true);
		if ( foundAccount != null ) {
			return foundAccount;
		}
		Pattern pattern = Pattern.compile(nameRegEx);

		for ( KMyMoneyAccount acct : acctMap.values() ) {
			Matcher matcher = pattern.matcher(acct.getName());
			if ( matcher.matches() ) {
				return acct;
			}
		}

		return null;
	}

	/*
	 * First try to fetch the account by id, then fall back to traversing all
	 * accounts to get if by it's name.
	 */
	public KMyMoneyAccount getAccountByIDorName(final KMMComplAcctID acctID, final String name)
			throws NoEntryFoundException, TooManyEntriesFoundException {
		if ( acctID == null ) {
			throw new IllegalStateException("null account ID given");
		}

		if ( ! acctID.isSet() ) {
			throw new IllegalStateException("unset account ID given");
		}

		if ( name == null ) {
			throw new IllegalStateException("null name given");
		}

		if ( name.trim().equals("") ) {
			throw new IllegalStateException("empty name given");
		}

		KMyMoneyAccount retval = getAccountByID(acctID);
		if ( retval == null ) {
			retval = getAccountByNameUniq(name, true);
		}

		return retval;
	}

	public KMyMoneyAccount getAccountByIDorName(final KMMAcctID acctID, final String name)
			throws NoEntryFoundException, TooManyEntriesFoundException {
		if ( acctID == null ) {
			throw new IllegalStateException("null account ID given");
		}

		if ( ! acctID.isSet() ) {
			throw new IllegalStateException("unset account ID given");
		}

		if ( name == null ) {
			throw new IllegalStateException("null name given");
		}

		if ( name.trim().equals("") ) {
			throw new IllegalStateException("empty name given");
		}

		return getAccountByIDorName(new KMMComplAcctID(acctID), name);
	}
	
	/*
	 * First try to fetch the account by id, then fall back to traversing all
	 * accounts to get if by it's name.
	 */
	public KMyMoneyAccount getAccountByIDorNameEx(final KMMComplAcctID acctID, final String name)
			throws NoEntryFoundException, TooManyEntriesFoundException {
		if ( acctID == null ) {
			throw new IllegalStateException("null account ID given");
		}

		if ( ! acctID.isSet() ) {
			throw new IllegalStateException("unset account ID given");
		}

		if ( name == null ) {
			throw new IllegalStateException("null name given");
		}

		if ( name.trim().equals("") ) {
			throw new IllegalStateException("empty name given");
		}

		KMyMoneyAccount retval = getAccountByID(acctID);
		if ( retval == null ) {
			retval = getAccountByNameEx(name);
		}

		return retval;
	}

	public KMyMoneyAccount getAccountByIDorNameEx(final KMMAcctID acctID, final String name)
			throws NoEntryFoundException, TooManyEntriesFoundException {
		if ( acctID == null ) {
			throw new IllegalStateException("null account ID given");
		}

		if ( ! acctID.isSet() ) {
			throw new IllegalStateException("unset account ID given");
		}

		if ( name == null ) {
			throw new IllegalStateException("null name given");
		}

		if ( name.trim().equals("") ) {
			throw new IllegalStateException("empty name given");
		}

		return getAccountByIDorNameEx(new KMMComplAcctID(acctID), name);
	}
	
	public List<KMyMoneyAccount> getAccountsByType(Type type) {
		List<KMyMoneyAccount> result = new ArrayList<KMyMoneyAccount>();

		for ( KMyMoneyAccount acct : getAccounts() ) {
			if ( acct.getType() == type ) {
				result.add(acct);
			}
		}

		return result;
	}

	public List<KMyMoneyAccount> getAccountsByTypeAndName(Type type, String expr, boolean qualif, boolean relaxed) {
		if ( expr == null ) {
			throw new IllegalStateException("null expression given");
		}

		if ( expr.trim().equals("") ) {
			throw new IllegalStateException("empty name given");
		}

		List<KMyMoneyAccount> result = new ArrayList<KMyMoneyAccount>();

		for ( KMyMoneyAccount acct : getAccountsByName(expr, qualif, relaxed) ) {
			if ( acct.getType() == type ) {
				result.add(acct);
			}
		}

		return result;
	}

	// ---------------------------------------------------------------

	public Collection<KMyMoneyAccount> getAccounts() {
		if ( acctMap == null ) {
			throw new IllegalStateException("no root-element loaded");
		}

		return Collections.unmodifiableCollection(acctMap.values());
	}

	public List<? extends KMyMoneyAccount> getParentlessAccounts() {
		try {
			List<KMyMoneyAccount> retval = new ArrayList<KMyMoneyAccount>();

			for ( KMyMoneyAccount acct : getAccounts() ) {
				if ( acct.getParentAccountID() == null ) {
					retval.add(acct);
				}

			}

			return retval;
		} catch (RuntimeException e) {
			LOGGER.error("getParentlessAccounts: Problem getting all root-account", e);
			throw e;
		} catch (Throwable e) {
			LOGGER.error("getParentlessAccounts: SERIOUS Problem getting all root-account", e);
			return new ArrayList<KMyMoneyAccount>();
		}
	}

	public List<KMMComplAcctID> getTopAccountIDs() {
		List<KMMComplAcctID> result = new ArrayList<KMMComplAcctID>();

		result.add(KMMComplAcctID.get(Top.ASSET));
		result.add(KMMComplAcctID.get(Top.LIABILITY));
		result.add(KMMComplAcctID.get(Top.INCOME));
		result.add(KMMComplAcctID.get(Top.EXPENSE));
		result.add(KMMComplAcctID.get(Top.EQUITY));

		return result;
	}

	public List<KMyMoneyAccount> getTopAccounts() {
		List<KMyMoneyAccount> result = new ArrayList<KMyMoneyAccount>();

		for ( KMMComplAcctID acctID : getTopAccountIDs() ) {
			KMyMoneyAccount acct = getAccountByID(acctID);
			result.add(acct);
		}

		return result;
	}

	// ---------------------------------------------------------------

	public int getNofEntriesAccountMap() {
		return acctMap.size();
	}

}
