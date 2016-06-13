/**
 * Created by gorkemgok on 14/06/16.
 */
var filtersModule = angular.module("ayronaFilters",[]);

filtersModule.filter("accountType", function (ACCOUNT_TYPE) {
    return function (input) {
        if (input == ACCOUNT_TYPE.ata_custom){
            return "Ata Online Hesabı";
        }else if (input == ACCOUNT_TYPE.mt4){
            return "MetaTrader4 Hesabı";
        }
    }
    
})
