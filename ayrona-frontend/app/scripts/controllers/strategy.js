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
                    strategies: ["Rest", function (Rest) {
                        return Rest.one("strategy/list").get();
                    }]
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
                    strategy : ["$stateParams","Rest", function ($stateParams, Rest) {
                        return Rest.one("strategy/"+$stateParams.strategyId).get();
                    }]
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
        
        var graphData = [];
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
                    var equitySeries = [];
                    var profitSeries = [];
                    var mddSeries = [];
                    angular.forEach(result.series[0].map.EQUITY, function (value) {
                        equitySeries.push(Number(value).toFixed(4));
                    });
                    angular.forEach(result.series[0].map.NET_PROFIT, function (value) {
                        profitSeries.push(Number(value).toFixed(4));
                    });
                    angular.forEach(result.series[0].map.MDD, function (value) {
                        mddSeries.push(Number(value).toFixed(4));
                    });
                    graphData = [
                        equitySeries,
                        profitSeries,
                        mddSeries
                    ];
                    $scope.data = [
                        equitySeries,
                        profitSeries,
                        mddSeries
                    ];
                    var i = 0;
                    var labels = [];
                    angular.forEach(result.series[0].dateList, function (value) {
                        labels.push(moment(value).format("DD-MM-YYYY"));
                    });
                    $scope.labels = labels;
                    $scope.backTestResult = result;
                }, function () {
                    console.log('Modal dismissed at: ' + new Date());
                }
            );
        };
        $scope.calculateProfit = function (position) {
            return Helper.calculateProfit(position);
        };
        $scope.isSeriesOpen = [true, true, true];
        $scope.toggleSeries = function (seriesIdx) {
            if ($scope.isSeriesOpen[seriesIdx]){
                $scope.data[seriesIdx] = [];
                $scope.isSeriesOpen[seriesIdx] = false;
            }else{
                $scope.data[seriesIdx] = graphData[seriesIdx];
                $scope.isSeriesOpen[seriesIdx] = true;
            }
        };
    })
    .controller("StrategyCreateCtrl", function ($scope, $controller, $location, AynRest, Notify, STRATEGY_STATE) {
        $controller("StrategyBase", {$scope:$scope});
        $scope.strategy = {};
        $scope.strategy.symbol = 'VOB30';
        $scope.strategy.period = 'M5';
        $scope.create = function (strategy) {
            strategy.state = STRATEGY_STATE.inactive;
            AynRest.createStrategy(strategy,
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
    .controller("StrategyUpdateCtrl", function ($scope, $controller, strategy, AynRest, Notify, STRATEGY_STATE) {
        $controller("StrategyBase", {$scope:$scope});

        $scope.strategy = strategy;
        $scope.update = function (strategy) {
            AynRest.updateStrategy(strategy,
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
        };

        $scope.labels = [];
        $scope.series = ['Equity', 'Profit', 'MDD'];
        $scope.data = [];
        $scope.onClick = function (points, evt) {
            console.log(points, evt);
        };
        $scope.datasetOverride = [
            {
                yAxisID: 'y-axis-1',
                pointRadius : 0
            },
            {
                yAxisID: 'y-axis-2',
                pointRadius : 0,
            }
        ];
        $scope.options = {
            scales: {
                xAxes: [{
                    display: true
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
    .controller("BackTestCtrl", function ($scope, symbol, period, code, AynRest) {
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
            AynRest.doBackTest(backtest, false,
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
     