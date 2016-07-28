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
    .controller("StrategyListCtrl", function ($scope, $controller, $location, strategies) {
        $scope.strategies = strategies;
        $controller("StrategyBase", {$scope:$scope});

        $scope.gotoCreateStrategy = function () {
            $location.path("/strategy/create");
        }

        $scope.gotoEditStrategy = function (strategyId) {
            $location.path("/strategy/edit/"+strategyId);
        }
    })
    .controller("StrategyBase", function ($scope, $location, $uibModal, Confirm, Rest, Helper, STRATEGY_STATE) {
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
        };
        $scope.copy = function (strategy) {
            delete strategy.id;
            strategy.state = STRATEGY_STATE.inactive;
            var sname = strategy.name + " Kopya";
            strategy.name += sname;
            Rest.all("strategy").post(strategy).then(
                function (response) {
                    $scope.compilationResult = 0;
                    $scope.compilationMessage = "Kopyalandı";
                    $location.path("/strategy/list");
                },
                function (error) {
                    var msg = error.data.message;
                    $scope.compilationResult = 1;
                    $scope.compilationMessage = msg;
                }
            );
        };
        $scope.delete = function(strategy){
            Confirm.show("Bu stratejiyi silmek istediğinizden emin misiniz? "+strategy.name, function () {
                Rest.one("strategy",strategy.id).remove().then(
                    function (response) {
                        Helper.removeById($scope.strategies, strategy.id);
                        console.log("Deleted strategy "+strategy.name);
                    },
                    function (error) {
                        console.log("Can't delete strategy "+strategy.name);
                    }
                );
            });
        };
        $scope.activate = function (strategy) {
            Confirm.show("Bu Stratejiyi aktive etmek istiyor musunuz? "+strategy.name, function () {
                Rest.one("ate/strategy/"+strategy.id+"/start").post().then(
                    function (response) {
                        angular.forEach($scope.strategies, function (obj) {
                            if (obj.id == strategy.id){
                                strategy.state = "ACTIVE";
                            }
                        });
                        console.log("Strategy activated:"+strategy.name);
                    },
                    function (error) {
                        console.log("Error while activating strategy:"+strategy.name+", "+error);
                    }
                );
            });
        };
        $scope.deactivate = function (strategy) {
            Confirm.show("Bu Stratejiyi deaktive etmek istiyor musunuz? "+strategy.name, function () {
                Rest.one("ate/strategy/"+strategy.id+"/stop").remove().then(
                    function (response) {
                        angular.forEach($scope.strategies, function (obj) {
                            if (obj.id == strategy.id){
                                strategy.state = "INACTIVE";
                            }
                        });
                        console.log("Strategy deactivated:"+strategy.name);
                    },
                    function (error) {
                        console.log("Error while deactivating strategy:"+strategy.name+", "+error);
                    }
                );
            });
        };
        $scope.openBackTestModal = function () {

            var modalInstance = $uibModal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'views/modals/backtest-modal.html',
                controller: 'BackTestCtrl',
                size: 'lg',
                resolve: {
                    symbol: function () {
                        return $scope.strategy.symbol;
                    },
                    period: function () {
                        return $scope.strategy.period;
                    },
                    code: function () {
                        return $scope.strategy.code;
                    }
                }
            });

            modalInstance.result.then(
                function (result) {
                    $scope.data = [
                        result.equitySeries,
                        result.profitSeries,
                        result.mddSeries
                    ];
                    var i = 0;
                    var labels = [];
                    angular.forEach(result.equitySeries, function (value) {
                        labels.push(i++);
                    });
                    $scope.labels = labels;
                    $scope.backTestResult = result;
                }, function () {
                    console.log('Modal dismissed at: ' + new Date());
                }
            );
        };
    })
    .controller("StrategyCreateCtrl", function ($scope, $controller, $location, Rest, Notify, STRATEGY_STATE) {
        $controller("StrategyBase", {$scope:$scope});
        $scope.strategy = {};
        $scope.strategy.symbol = 'VOB30';
        $scope.strategy.period = 'M5';
        $scope.create = function (strategy) {
            strategy.state = STRATEGY_STATE.inactive;
            Rest.all("strategy").post(strategy).then(
                function (response) {
                    $scope.compilationResult = 0;
                    $scope.compilationMessage = "Kaydedildi";
                    $location.path("/strategy/list");
                },
                function (error) {
                    var msg = error.data.message;
                    $scope.compilationResult = 1;
                    $scope.compilationMessage = msg;
                }
            );
        };
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

        $scope.labels = [];
        $scope.series = ['Equity', 'Profit', 'MDD'];
        $scope.data = [];
        $scope.onClick = function (points, evt) {
            console.log(points, evt);
        };
        $scope.datasetOverride = [{ yAxisID: 'y-axis-1' }, { yAxisID: 'y-axis-2' }];
        $scope.options = {
            scales: {
                xAxes: [{
                    display: false
                }],
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'linear',
                        display: true,
                        position: 'left'
                    },
                    {
                        id: 'y-axis-2',
                        type: 'linear',
                        display: true,
                        position: 'right'
                    }
                ]
            }
        };

        
    })
    .controller("BackTestCtrl", function ($scope, symbol, period, code, Rest) {
        $scope.beginDate = new Date(2015, 1, 1);
        $scope.endDate = new Date();
        $scope.dateOptions = {
            formatYear: 'yy',
            maxDate: new Date(2020, 5, 22),
            minDate: new Date(2010, 1, 1),
            startingDay: 1
        };

        $scope.format = "dd-MM-yyyy";

        $scope.openBegin = function () {
            $scope.beginDatePopupOpen = true;
        };

        $scope.openEnd = function () {
            $scope.endDatePopupOpen = true;
        };

        $scope.doBackTest = function (beginDate, endDate) {
            var backtest = {
                symbol: symbol,
                period: period,
                code: code,
                beginDate : beginDate.toISOString(),
                endDate : endDate.toISOString()
            };
            Rest.all("strategy/backtest").post(backtest).then(
                function (response) {
                    console.log(response);
                    $scope.$close(response);
                },
                function (error) {
                    console.log(error);
                    $scope.$close(error);
                }
            );
        }
    });
     