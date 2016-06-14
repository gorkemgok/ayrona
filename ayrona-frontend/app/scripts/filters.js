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
    
});

filtersModule.filter("period", function (PERIODS) {
    return function (input) {
        var result;
        angular.forEach(PERIODS, function (period) {
            if (period.value == input){
                result = period.text;
            }
        });
        return result;
    }

})
