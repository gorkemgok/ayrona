/**
 * Created by gorkemgok on 01/08/16.
 */
var restServicesModule = angular.module("ayronaRestServices",[]);

restServicesModule.factory("AynRest", function (Rest) {
    var unbindAccount = function (strategyId, accountId, successCallback, errorCallback) {
        Rest.one("/strategy/"+strategyId+"/account/"+accountId).remove().then(
            successCallback, errorCallback
        );
    };

    var updateAccountBinder = function (strategyId, accountId, lot, state, successCallback, errorCallback) {
        var accountBinder = {
            id:accountId,
            lot:lot,
            state:state
        };
        Rest.one("/strategy/"+strategyId+"/account").customPUT(accountBinder).then(
            function (response) {
                console.log(response);
                successCallback(response);
            },
            function (error) {
                errorCallback(error);
            }
        );
    };

    var checkEdr = function (module, type, startDate, successCallback, errorCallback) {
        Rest.one("edr/?module="+module+"&type="+type+"&startDate="+startDate).get().then(
            successCallback, errorCallback
        );
    };
    return {
        unbindAccount : unbindAccount,
        updateAccountBinder : updateAccountBinder,
        checkEdr : checkEdr
    };
});