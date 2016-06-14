/**
 * Created by gorkemgok on 27/01/16.
 */
var servicesModule = angular.module("ayronaServices",[]);

servicesModule.factory('SessionService', function($rootScope, $location, $timeout, Rest){
  var session = {};
  session.token = '';

  session.start = function(token){
    session.token = token;
    window.localStorage.setItem('token',token)
    Rest.setDefaultHeaders(
      {
        "Access-Control-Allow-Origin": "*",
        token:token
      }
    );
    var lastPath = $rootScope.lastPath;
    if (lastPath != undefined && lastPath != ''){
      $timeout(function(){
        $location.path(lastPath);
      },0)
    }
  };

  session.stop = function(){
    session.token = '';
    Rest.setDefaultHeaders(
      {
        "Access-Control-Allow-Origin": "*",
        token:''
      }
    );
    console.log('Loggedout')
    $location.path("/login");
  };

  return session;
});

servicesModule.factory("Rest", function($location, $rootScope, $timeout, Notify, Restangular, REST){
  return Restangular.withConfig(function(RestangularConfigurer) {
    RestangularConfigurer.setBaseUrl(REST.base);
    RestangularConfigurer.setErrorInterceptor(function(response) {
      console.log(response);
      if(response.status === 401) {
        $rootScope.lastPath = $location.path();
        $timeout(function(){$location.path('/login');},0);
        return response; // error handled
      }else if (response.status === -1){
        Notify.globalAlert("Baglanti sorunu...");
      }else if(response.status === 401) {
        Notify.globalAlert("Hata:"+response.status);
      }
      return true; // error not handled
    });
  });
});

servicesModule.factory("Notify", function ($rootScope, $timeout) {
  var globalAlert = function(text){
    global(text,'alert');
  };
    var globalError = function(text){
        global(text,'alert');
    };
  var global = function(text, type){
    if (type == 'alert') {
      $rootScope.showAlert = true;
      $rootScope.textAlert = text;
    }else if (type == 'warning') {
      $rootScope.showWarning = true;
      $rootScope.textWarning = text;
    }else if (type == 'info') {
      $rootScope.showInfo = true;
      $rootScope.textInfo = text;
    }else if (type == 'error') {
        $rootScope.showError = true;
        $rootScope.textError = text;
    }
      $timeout(function(){
        $rootScope.showAlert = false;
        $rootScope.showWarning = false;
        $rootScope.showInfo = false;
        $rootScope.showError = false;
      },4000);
  };
  return {
    global : global,
      globalAlert : globalAlert,
      globalError : globalError
  }
})

servicesModule.factory("Helper", function(METRICS){

    var setTimeToZero  = function(date){
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
    };

    var preparePrerequisite = function(prerequisiteResponse){
        var prerequisite = [];
        angular.forEach(prerequisiteResponse, function(check){
            var message = "";
            if (check.check == "NOT_EMPTY"){
                message = "Doldurulması zorunlu alan"
            }else if (check.check == "SELECTED"){
                message = "Bir değer seçmelisiniz"
            }
            prerequisite[check.field] = message;
        });
        return prerequisite;
    }

    var prepareStrategy = function(strategy){
    var data = {};
    var radarMetrics = METRICS
    var radars = {};
    var performanceSummary = {
      training : {},
      test : {},
      validation : {}
    };
    var performanceSummaryString = {
      training : {},
      test : {},
      validation : {}
    };
    var x = 0;
    angular.forEach(strategy.summaries, function(summary){
      if (summary.summaryType === "com.gorkemgok.tick4j.core.backtest.calculation.EquitySummary"){
        var dataSet = [];
        var dataRow = {};
        var i = 0;
        var a = 0;
        var b = 0;
        var c = 0;
        angular.forEach(summary.results, function(result){
          if (result.metricType == "EQUITY_SERIES_IP"){
            dataRow.profit = result.value;
            i++;
          }else if (result.metricType == "EQUITY_SERIES_EQUITY"){
            dataRow.equity = result.value;
            i++;
          }else if (result.metricType == "EQUITY_SERIES_MDD"){
            dataRow.mdd = result.value;
            i++;
          }
          if (i === 3){
            dataRow.x = new Date(result.date);
            x++;
            dataSet.push(dataRow);
            dataRow = {};
            i=0;
          }
        });
        if (summary.intervalType === "TRAINING"){
          data.training = dataSet;
        }else if (summary.intervalType === "TEST"){
          data.test = dataSet;
        }if (summary.intervalType === "VALIDATION"){
          data.validation = dataSet;
        }

      }else if (summary.summaryType === "com.gorkemgok.tick4j.core.backtest.calculation.PerformanceSummary"){
        angular.forEach(summary.results, function(result){
          if (summary.intervalType === "TRAINING"){
            performanceSummary.training[result.metricType] = result.value;
            performanceSummaryString.training[result.metricType] = Number(result.value).toFixed(2);
          }else if (summary.intervalType === "TEST"){
            performanceSummary.test[result.metricType] = result.value;
            performanceSummaryString.test[result.metricType] = Number(result.value).toFixed(2);
          }if (summary.intervalType === "VALIDATION"){
            performanceSummary.validation[result.metricType] = result.value;
            performanceSummaryString.validation[result.metricType] = Number(result.value).toFixed(2);
          }
        });
      }
    });
    angular.forEach(radarMetrics, function(radarMetric){
      var training = performanceSummary.training[radarMetric.key];
      var test = performanceSummary.test[radarMetric.key];
      var validation = performanceSummary.validation[radarMetric.key];
      var max = Math.max(training, Math.max(test,validation));
      var min = Math.min(training, Math.min(test,validation));
      var diff = 0;
      if (min < 0) diff =  Math.abs(min);
      validation += diff;
      test += diff;
      training += diff;
      radars[radarMetric.key]= {
        metric : radarMetric.key,
        labels: ["D", "T", "E"],
        data: [[validation, test, training]]
      };
    });
    return {
      id : strategy.id,
      formula : strategy.formula,
      options : strategy.strategySessionModel.strategyOptions,
      data : data,
      summary : performanceSummaryString,
      radars : radars,
      favorite : strategy.favorite
    }
  };

    var prepareGEOptions = function(so){
    setTimeToZero(so.trainingStart);
    setTimeToZero(so.trainingEnd);
    setTimeToZero(so.testStart);
    setTimeToZero(so.testEnd);
    setTimeToZero(so.validationStart);
    setTimeToZero(so.validationEnd);
    var to = {
      strategyOptionsBean : {
        symbol:so.symbol,
        period:so.period,
        commissionType:"PERCENTAGE",
        commission:so.commission,
        twoWayPositionsAllowed:so.twoWayPositionsAllowed,
        processHoldAsOpposite:so.processHoldAsOpposite,
        processBothAsOpposite:so.processBothAsOpposite,
        fitnessMetric:so.fitnessMetric,
        trainingStart:so.trainingStart.toISOString(),
        trainingEnd:so.trainingEnd.toISOString(),
        testStart:so.testStart.toISOString(),
        testEnd:so.testEnd.toISOString(),
        validationStart:so.validationStart.toISOString(),
        validationEnd:so.validationEnd.toISOString()
      },
      gpeOptions : {
        maxInitDepth:50,
        maxCrossoverDepth:50,
        maxPopulationSize:100,
        stopCondition:20,
        fitnessTolerancePercentage:10
      }
    };

    return to;
  }

    var preparePages = function(totalPage, currentPage){
        var pages = [];
        for (var i = 1; i <= totalPage;i++){
            pages.push({no:i, active:(i==currentPage)});
        }
        return pages;
        }
    var preparePagesFromBean = function(paginatedBean){
        return preparePages(paginatedBean.totalPage, paginatedBean.currentBean);
    }

    return {
        prepareStrategy : prepareStrategy,
        setTimeToZero : setTimeToZero,
        prepareGEOptions : prepareGEOptions,
        preparePages : preparePages,
        preparePagesFromBean : preparePagesFromBean,
        preparePrerequisite : preparePrerequisite
    }
});

