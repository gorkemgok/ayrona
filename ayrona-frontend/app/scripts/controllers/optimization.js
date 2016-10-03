/**
 * Created by gorkemgok on 17/09/16.
 */
angular.module('ayronaApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('opt_list', {
                url: '/opt/list',
                templateUrl: 'views/opt-list.html',
                controller: 'OptListCtrl',
                resolve: {
                    sessions: ["Rest", function (Rest) {
                        return Rest.one("session/list?page=1&item=100").get();
                    }]
                }
            })
            .state('opt_create', {
                url: '/opt/create',
                templateUrl: 'views/opt-form.html',
                controller: 'OptCreateCtrl'
            })
            .state('opt_detail', {
                url: '/opt/:optId',
                templateUrl: 'views/opt-detail.html',
                controller: 'OptDetailCtrl',
                resolve : {
                    opt : ["$stateParams","Rest", function ($stateParams, Rest) {
                        return Rest.one("session/"+$stateParams.optId).get();
                    }]
                }
            })
    })
    .controller("OptBaseCtrl", function ($scope, $location, AynRest) {
        $scope.gotoCreateSession = function () {
            $location.path("/opt/create");
        };
        $scope.cancelSession = function(session){
            AynRest.cancelSession(session.id, function (response) {
                console.log(response);
                session.state = "CANCELED";
            },function (error) {
                console.error(error);
            });
        };
        
        $scope.restartSession = function (session) {
            var state = session.state;
            session.state = "WAITING";
            AynRest.updateSession(session, function () {

            }, function () {
                session.state = state;
            });
        };
    })
    .controller("OptCreateCtrl", function ($scope, $controller, AynRest, Helper, SYMBOLS, PERIODS) {
        $controller("StartEndDatePickerCtrl", {$scope:$scope});
        $scope.opt = {};
        $scope.opt.name = Helper.generateRandomName();
        $scope.opt.symbol = SYMBOLS[1].value;
        $scope.opt.period = PERIODS[1].value;
        $scope.opt.populationSize = 1000;
        $scope.opt.mutationProbability = 0.7;
        $scope.opt.eliteCount = 10;
        $scope.opt.threadCount = 2;
        $scope.create = function (session) {
            session.startDate = $scope.startDate.toISOString();
            session.endDate = $scope.endDate.toISOString();
            session.state = "WAITING";
            AynRest.createSession(session, function (response) {
                console.log(response);
            },function (error) {
                $scope.prerequisite = Helper.preparePrerequisite(error.data);
                console.log($scope.prerequisite);
            });
        }
    })
    .controller("OptListCtrl", function ($scope, $controller, $location, sessions) {
        $controller("OptBaseCtrl", {$scope:$scope});
        $scope.sessions = sessions.list;
        $scope.sessionCount = sessions.count;
        $scope.gotoDetail = function (sessionId) {
            $location.path("/opt/"+sessionId);
        };
    })
    .controller("OptDetailCtrl", function ($scope, $controller, opt, Rest) {
        $controller("StartEndDatePickerCtrl", {$scope:$scope});
        $controller("OptBaseCtrl", {$scope:$scope});
        $controller("GraphCtrl", {$scope:$scope});
        $scope.session = opt;
        var processBtr = function (btr) {
            var btrTabs = [];
            console.log(btr.series.length);
            for (var i = 0; i < btr.series.length; i++) {
                var series = btr.series[i];
                var tab = {};
                tab.title = series.period;
                var equitySeries = [];
                var profitSeries = [];
                var mddSeries = [];
                angular.forEach(series.map.EQUITY, function (value) {
                    equitySeries.push(Number(value).toFixed(4));
                });
                angular.forEach(series.map.NET_PROFIT, function (value) {
                    profitSeries.push(Number(value).toFixed(4));
                });
                angular.forEach(series.map.MDD, function (value) {
                    mddSeries.push(Number(value).toFixed(4));
                });
                tab.data = [
                    equitySeries,
                    profitSeries,
                    mddSeries
                ];
                $scope.data = [
                    equitySeries,
                    profitSeries,
                    mddSeries
                ];
                var labels = [];
                angular.forEach(series.dateList, function (value) {
                    labels.push(moment(value).format("DD-MM-YYYY"));
                });
                tab.labels = labels;
                btrTabs.push(tab);
            }
            return {
                btrTabs : btrTabs,
                backTestResult : btr
            }
        };
        $scope.doDetailedBackTest = function (code, symbol, period, startDate, endDate){
            var backtest = {
                symbol: symbol,
                period: period,
                code: code,
                beginDate : startDate.toISOString(),
                endDate : endDate.toISOString()
            };
            Rest.all("strategy/backtest?detailed=true").post(backtest).then(
                function (response) {
                    var btr = processBtr(response);
                    $scope.btrTabs = btr.btrTabs;
                    $scope.backTestResult = btr.backTestResult;
                },
                function (error) {
                    console.log(error);
                }
            );
        };
    });