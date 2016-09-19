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
            .state('opt_update', {
                url: '/opt/edit/:sessionId',
                templateUrl: 'views/opt-form.html',
                controller: 'OptUpdateCtrl',
                resolve : {
                    strategy : ["$stateParams","Rest", function ($stateParams, Rest) {
                        return Rest.one("strategy/"+$stateParams.strategyId).get();
                    }]
                }
            })
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
        $scope.sessions = sessions.list;
        $scope.sessionCount = sessions.count;

        $scope.gotoCreateSession = function () {
            $location.path("/opt/create");
        }
    });