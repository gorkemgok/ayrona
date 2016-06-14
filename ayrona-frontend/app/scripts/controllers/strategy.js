/**
 * Created by gorkemgok on 14/06/16.
 */
angular.module('ayronaApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('strategy_list', {
                url: '/strategy/list',
                templateUrl: 'views/strategy-list.html',
                controller: 'StrategyListCtrl',
                resolve: {
                    strategies: function (Rest) {
                        return Rest.one("strategy/list").get();
                    }
                }
            })
            .state('strategy_create', {
                url: '/strategy/create',
                templateUrl: 'views/strategy-form.html',
                controller: 'StrategyCreateCtrl'
            })
            .state('strategy_update', {
                url: '/strategy/edit/:strategyId',
                templateUrl: 'views/strategy-form.html',
                controller: 'StrategyUpdateCtrl',
                resolve : {
                    strategy : function ($stateParams, Rest) {
                        return Rest.one("strategy/"+$stateParams.strategyId).get();
                    }
                }
            })
    })
    .controller("StrategyListCtrl", function ($scope, $location, strategies, Rest) {
        $scope.strategies = strategies;

        $scope.gotoCreateStrategy = function () {
            $location.path("/strategy/create");
        }

        $scope.gotoEditStrategy = function (strategyId) {
            $location.path("/strategy/edit/"+strategyId);
        }
    })
    .controller("StrategyBase", function ($scope, Rest) {
        $scope.compile = function (code) {
            Rest.all("strategy/compile").post(code).then(
                function (response) {
                    $scope.compilationResult = 0;
                    $scope.compilationMessage = "Derleme başarılı";
                },
                function (error) {
                    var msg = error.data.message;
                    $scope.compilationResult = 1;
                    $scope.compilationMessage = msg;
                }
            );
        }
    })
    .controller("StrategyCreateCtrl", function ($scope, $controller, Rest, Notify, STRATEGY_STATE) {
        $controller("StrategyBase", {$scope:$scope});
        $scope.strategy = {};
        $scope.strategy.symbol = 'VOB30';
        $scope.strategy.period = 'M5';
        $scope.create = function (strategy) {
            strategy.state = STRATEGY_STATE.active;
            Rest.all("strategy").post(strategy).then(
                function (response) {
                    $scope.compilationResult = 0;
                    $scope.compilationMessage = "Kaydedildi";
                },
                function (error) {
                    var msg = error.data.message;
                    $scope.compilationResult = 1;
                    $scope.compilationMessage = msg;
                }
            );
        }
    })
    .controller("StrategyUpdateCtrl", function ($scope, $controller, strategy, Rest, Notify, STRATEGY_STATE) {
        $controller("StrategyBase", {$scope:$scope});

        $scope.strategy = strategy;
        $scope.update = function (strategy) {
            strategy.state = STRATEGY_STATE.active;
            Rest.all("strategy").customPUT(strategy).then(
                function (response) {
                    $scope.compilationResult = 0;
                    $scope.compilationMessage = "Kaydedildi";
                },
                function (error) {
                    var msg = error.data.message;
                    $scope.compilationResult = 1;
                    $scope.compilationMessage = msg;
                }
            );
        }
    });;
     