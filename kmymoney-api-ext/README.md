# Notes on the Module "API Extensions"

This module provides simplified, high-level access functions to a KMyMoney file 
via the "API" module (sort of "macros") for specialized, complex tasks.

## Sub-Modules
Currently, the module consists of just one single sub-module: "SecAcct".

### SecAcct
Currently, this sub-module contains just one single class: `SecuritiesAccountTransactionManager`, 
which provides a simplified, high-level interface for generating buy- and dividend transactions 
in a securities account (brokerage account).

## Major Changes
### V. 0.4 &rarr; 0.5
Created module.

## Planned
* Sub-module SecAcct: 
	* More variants of buy/sell/dividend/etc. transactions, including wrappers
          which you provide account names to instead of account IDs.
	* Support for stock splits / reverse splits.
	* Possibly new class for high-level consistency checks of existing transactions, e.g.: All dividends of domestic shares are actually posted to the domestic dividend account.

* New sub-module for management of securities and currencies (esp. bulk quote import).

## Known Issues
(None)

