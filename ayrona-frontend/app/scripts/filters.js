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
    };

});

filtersModule.filter("result_period", function(RESULT_PERIOD){
    return function (input) {
        return RESULT_PERIOD[input];
    }    
});

filtersModule.filter("edr_module_icon", function (EDR) {
    return function (input) {
        var icon;
        switch (input){
            case EDR.strategy.name:
                icon = "glyphicons-signal";
                break;
            case EDR.account.name:
                icon = "glyphicons-parents";
                break;
            default:
                icon = "glyphicons-question-sign"
        }
        return icon;
    };
});

filtersModule.filter("edr_type_icon", function (EDR) {
    return function (input) {
        var icon;
        switch (input){
            case EDR.strategy.types.start:
                icon = "glyphicons-play";
                break;
            case EDR.strategy.types.stop:
                icon = "glyphicons-pause";
                break;
            case EDR.strategy.types.boundAccount:
                icon = "glyphicons-user-add";
                break;
            case EDR.strategy.types.unboundAccount:
                icon = "glyphicons-user-remove";
                break;
            case EDR.strategy.types.newSignal:
                icon = "glyphicons-resize-small";
                break;
            case EDR.account.types.newPosition:
                icon = "glyphicons-parking";
                break;
            case EDR.account.types.start:
                icon = "glyphicons-user-add";
                break;
            case EDR.account.types.start:
                icon = "glyphicons-user-remove";
                break;
            default:
                icon = "glyphicons-question-sign"
        }
        return icon;
    };
});

filtersModule.filter("edr_icon", function (EDR) {
    return function (input) {
        var icon;
        var type = input.type;
        var status = input.status;
        switch (type){
            case EDR.strategy.types.start:
                icon = "glyphicon-play-circle";
                break;
            case EDR.strategy.types.stop:
                icon = "glyphicons-pause";
                break;
            case EDR.strategy.types.boundAccount:
                icon = "glyphicons-user-add";
                break;
            case EDR.strategy.types.unboundAccount:
                icon = "glyphicons-user-remove";
                break;
            case EDR.strategy.types.newSignal:
                icon = "glyphicons-resize-small";
                break;
            case EDR.account.types.newPosition:
                icon = "glyphicons-parking";
                break;
            case EDR.account.types.start:
                icon = "glyphicons-user-add";
                break;
            case EDR.account.types.start:
                icon = "glyphicons-user-remove";
                break;
            default:
                icon = "glyphicons-question-sign"
        }
        switch (status){
            case "SUCCESSFUL":
                icon += " text-success";
                break;
            case "FAILED":
                icon += " text-danger";
                break;
            default:
                icon += " text-default";
        }
        return icon;
    };
});

filtersModule.filter("millisToDate", function () {
    return function (millis) {
        var date = moment(millis);
        return date.format("DD.MM.YYYY HH:mm");
    };
});

filtersModule.filter("ISODateFormat", function () {
    return function (ISODate) {
        var date = moment(ISODate);
        return date.format("DD.MM.YYYY HH:mm");
    };
});

filtersModule.filter("directionIcon", function () {
    return function (direction) {
        var icon;
        switch (direction) {
            case "LONG":
                icon = "glyphicon-chevron-up text-success";
                break;
            case "SHORT":
                icon = "glyphicon-chevron-down text-danger";
                break;
            default:
                icon = "glyphicon-question-sign text-default";
        }
        return icon;
    }
});

filtersModule.filter("profit", function (Helper) {
    return function (position) {
        var profit = Helper.calculateProfit(position);
        return profit;
    };
});

filtersModule.filter("decimalFormat", function () {
   return function (input, toFixed) {
       if (toFixed === undefined){
           toFixed = 4;
       }
       return Number(input).toFixed(toFixed);
   };
});
