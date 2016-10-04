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
    .controller("OptBaseCtrl", function ($scope, $location, AynRest, STRATEGY_STATE) {
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

        $scope.saveAsStrategy = function (session, code) {
            var name = session.name+"["+code.generationCount+"-"+Number(code.fitness).toFixed(4)+"]";
            var strategy = {
                name : name,
                code : code.code,
                state : STRATEGY_STATE.inactive,
                symbol : session.symbol,
                period : session.period,
                initialBarCount : 500
            };
            AynRest.createStrategy(strategy, function (response) {
                $location.path("/strategy/edit/"+response.id);
            },
            function (error) {
                console.error(error);
            });
        };
    })
    .controller("OptCreateCtrl", function ($scope, $controller, AynRest, Helper, SYMBOLS, PERIODS, METRICS) {
        $controller("StartEndDatePickerCtrl", {$scope:$scope});
        $scope.METRICS = METRICS;
        $scope.opt = {};
        $scope.opt.name = Helper.generateRandomName();
        $scope.opt.symbol = SYMBOLS[1].value;
        $scope.opt.period = PERIODS[1].value;
        $scope.opt.populationSize = 1000;
        $scope.opt.mutationProbability = 0.7;
        $scope.opt.eliteCount = 10;
        $scope.opt.threadCount = 2;
        $scope.opt.scoreEquation = "np";
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
    .controller("OptDetailCtrl", function ($scope, $controller, $timeout, opt, AynRest, BackTestService, METRICS) {
        $controller("StartEndDatePickerCtrl", {$scope:$scope});
        $controller("OptBaseCtrl", {$scope:$scope});
        $controller("GraphCtrl", {$scope:$scope});
        $scope.metrics = METRICS;
        $scope.btrList = [];
        $scope.session = opt;
        $scope.activeTab = -1;
        $scope.doDetailedBackTest = function (code, session, startDate, endDate){
            var sYear = startDate.getFullYear();
            var eYear = endDate.getFullYear();
            var name = code.generationCount+".gen - "+Number(code.fitness).toFixed(4)+" ["+sYear+"-"+eYear+"]";
            var backtest = {
                symbol: session.symbol,
                period: session.period,
                code: code.code,
                beginDate : startDate.toISOString(),
                endDate : endDate.toISOString()
            };
            AynRest.doBackTest(backtest, true,
                function (response) {
                    var btr = BackTestService.processDetailed(response);
                    $scope.btrList.push({
                        name : name,
                        tabs : btr.btrTabs,
                        results : btr.backTestResult.results
                    });
                    $timeout(function(){$scope.activeTab = $scope.activeTab + 1;}, 0);
                },
                function (error) {
                    console.log(error);
                }
            );
        };
        
        $scope.showCodeDetail = function (code) {
                
        };

        $scope.closeBtrTab = function (index) {
            $scope.btrList.splice(index, 1);
            $scope.activeTab = $scope.activeTab - 1;
        };
    });