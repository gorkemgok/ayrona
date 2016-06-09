package com.ayronasystems.core.integration.mt4;

import com.ayronasystems.core.account.TORConverter;
import com.ayronasystems.core.definition.TradeOperationResult;

/**
 * Created by gorkemg on 09.06.2016.
 */
public class MT4TORConverter implements TORConverter{

    public static final MT4TORConverter INSTANCE = new MT4TORConverter();

    public TradeOperationResult convert(Exception exception){
        return TradeOperationResult.UNKNOWN;
    }

    /*} catch ( ErrTradeTooManyOrders errTradeTooManyOrders ) {
            errTradeTooManyOrders.printStackTrace ();
        } catch ( ErrUnknownSymbol errUnknownSymbol ) {
            errUnknownSymbol.printStackTrace ();
        } catch ( ErrCustomIndicatorError errCustomIndicatorError ) {
            errCustomIndicatorError.printStackTrace ();
        } catch ( ErrPriceChanged errPriceChanged ) {
            errPriceChanged.printStackTrace ();
        } catch ( ErrTradeNotAllowed errTradeNotAllowed ) {
            errTradeNotAllowed.printStackTrace ();
        } catch ( ErrTooFrequentRequests errTooFrequentRequests ) {
            errTooFrequentRequests.printStackTrace ();
        } catch ( ErrAccountDisabled errAccountDisabled ) {
            errAccountDisabled.printStackTrace ();
        } catch ( ErrNotEnoughMoney errNotEnoughMoney ) {
            errNotEnoughMoney.printStackTrace ();
        } catch ( ErrInvalidAccount errInvalidAccount ) {
            errInvalidAccount.printStackTrace ();
        } catch ( ErrTradeTimeout4 errTradeTimeout4 ) {
            errTradeTimeout4.printStackTrace ();
        } catch ( ErrTradeTimeout3 errTradeTimeout3 ) {
            errTradeTimeout3.printStackTrace ();
        } catch ( ErrOrderLocked errOrderLocked ) {
            errOrderLocked.printStackTrace ();
        } catch ( ErrInvalidTradeParameters errInvalidTradeParameters ) {
            errInvalidTradeParameters.printStackTrace ();
        } catch ( ErrTradeTimeout2 errTradeTimeout2 ) {
            errTradeTimeout2.printStackTrace ();
        } catch ( ErrInvalidTradeVolume errInvalidTradeVolume ) {
            errInvalidTradeVolume.printStackTrace ();
        } catch ( ErrInvalidFunctionParamvalue errInvalidFunctionParamvalue ) {
            errInvalidFunctionParamvalue.printStackTrace ();
        } catch ( ErrOffQuotes errOffQuotes ) {
            errOffQuotes.printStackTrace ();
        } catch ( ErrCommonError errCommonError ) {
            errCommonError.printStackTrace ();
        } catch ( ErrLongsNotAllowed errLongsNotAllowed ) {
            errLongsNotAllowed.printStackTrace ();
        } catch ( ErrStringParameterExpected errStringParameterExpected ) {
            errStringParameterExpected.printStackTrace ();
        } catch ( ErrShortsNotAllowed errShortsNotAllowed ) {
            errShortsNotAllowed.printStackTrace ();
        } catch ( ErrInvalidPrice errInvalidPrice ) {
            errInvalidPrice.printStackTrace ();
        } catch ( ErrIntegerParameterExpected errIntegerParameterExpected ) {
            errIntegerParameterExpected.printStackTrace ();
        } catch ( ErrTradeModifyDenied errTradeModifyDenied ) {
            errTradeModifyDenied.printStackTrace ();
        } catch ( ErrTradeContextBusy errTradeContextBusy ) {
            errTradeContextBusy.printStackTrace ();
        } catch ( ErrRequote errRequote ) {
            errRequote.printStackTrace ();
        } catch ( ErrTradeTimeout errTradeTimeout ) {
            errTradeTimeout.printStackTrace ();
        } catch ( ErrNoConnection errNoConnection ) {
            errNoConnection.printStackTrace ();
        } catch ( ErrInvalidStops errInvalidStops ) {
            errInvalidStops.printStackTrace ();
        } catch ( ErrInvalidPriceParam errInvalidPriceParam ) {
            errInvalidPriceParam.printStackTrace ();
        } catch ( ErrTradeExpirationDenied errTradeExpirationDenied ) {
            errTradeExpirationDenied.printStackTrace ();
        } catch ( ErrTradeDisabled errTradeDisabled ) {
            errTradeDisabled.printStackTrace ();
        } catch ( ErrMarketClosed errMarketClosed ) {
            errMarketClosed.printStackTrace ();
        } catch ( ErrServerBusy errServerBusy ) {
            errServerBusy.printStackTrace ();
        } catch ( ErrOldVersion errOldVersion ) {
            errOldVersion.printStackTrace ();
        } catch ( ErrTooManyRequests errTooManyRequests ) {
            errTooManyRequests.printStackTrace ();
        } catch ( ErrLongPositionsOnlyAllowed errLongPositionsOnlyAllowed ) {
            errLongPositionsOnlyAllowed.printStackTrace ();
        }*/

}
