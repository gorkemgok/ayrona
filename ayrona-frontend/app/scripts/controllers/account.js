/**
 * Created by gorkemgok on 28/02/16.
 */
'use strict';

angular.module('ayronaApp')
    .config(function($stateProvider){
        $stateProvider
            .state('accounts', {
                url: '/account/list',
                templateUrl: 'views/account-list.html',
                controller:'AccountListCtrl',
                resolve : {
                    accounts : ["Rest", function(Rest){
                        return Rest.one("account/list").get();
                    }]
                }
            })
            .state('create_account', {
                url: '/account/create',
                templateUrl: 'views/account-form.html',
                controller:'AccountCreateCtrl'
            })
            .state('create_edit', {
                url: '/account/edit/:accountId',
                templateUrl: 'views/account-form.html',
                controller:'AccountEditCtrl',
                resolve : {
                    account : ["$stateParams","Rest", function($stateParams, Rest){
                        return Rest.one("account/"+$stateParams.accountId).get();
                    }],
                    strategies: ["$stateParams","Rest", function ($stateParams, Rest){
                        return Rest.one("account/"+$stateParams.accountId+"/strategies").get();
                    }]
                }
            })
    })
    .controller('AccountListCtrl', function ($scope, $timeout, $location, accounts, Rest, Confirm, Notify, Helper) {
        $scope.accounts = accounts;
        $scope.delete = function (account) {
            Confirm.show("Bu hesabı silmek istiyor musunuz? "+account.name,function () {
                Rest.all("account/" + account.id).remove().then(
                    function (response) {
                        Helper.removeById($scope.accounts, account.id);
                    },
                    function (error) {
                        console.log(error);
                    }
                );
            });
        };
        $scope.gotoCreateAccount = function () {
            $location.path("/account/create");
        }

        $scope.gotoEditAccount = function (accountId) {
            $location.path("/account/edit/"+accountId);
        }
    
    })
    .controller('AccountCreateCtrl', function ($scope, $timeout, $location, Rest, Notify, Helper) {
        $scope.account = {};
        $scope.create = function (account) {
            console.log(account);
            Rest.all("account").post(account).then(
                function (response) {
                    $location.path("/account/list");
                },
                function (error) {
                    $scope.prerequisite = Helper.preparePrerequisite(error.data);
                }
            );
        }

    })
    .controller('AccountEditCtrl', function ($scope, $timeout, $uibModal, $location, account, strategies, Confirm, AynRest, Rest, Notify, Helper) {
        $scope.account = account;
        $scope.strategies = strategies;
        $scope.update = function (account) {
            console.log(account);
            Rest.all("account",account.id).customPUT(account).then(
                function (response) {
                    $location.path("/account/list");
                },
                function (error) {
                    $scope.prerequisite = Helper.preparePrerequisite(error.data);
                }
            );
        };
        $scope.changeAccountBinderState = function (boundStrategy, isActive) {
            var strategy = boundStrategy.strategy;
            var state = isActive?"ACTIVE":"INACTIVE";
            Confirm.show(strategy.name+(isActive?" ACTIVE":" INACTIVE"),function () {
                    AynRest.updateAccountBinder(strategy.id, account.id, boundStrategy.lot, state,
                        function (response) {
                            boundStrategy.state = state;
                        },
                        function (error) {
                            console.log(error);
                        }
                    );
                }
            );
        };
        $scope.unbindAccount = function(strategy){
            Confirm.show("Bu stratejiyi bu hesaptan silmek istiyor musunuz? "+strategy.name,
                function () {
                    AynRest.unbindAccount(strategy.id, account.id, function (response) {
                        console.log(strategy.name+" is unbound from "+account.name);
                        var i = 0;
                        angular.forEach($scope.strategies, function (obj) {
                            if (obj.strategy.id === strategy.id){
                                $scope.strategies.splice(i ,1);
                            }
                            i++;
                        });
                    },function (error) {

                    });
                }
            );    
        };
        $scope.openBindModal = function () {

            var modalInstance = $uibModal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'views/modals/bind-account-modal.html',
                controller: 'AccountBindCtrl',
                size: 'lg',
                resolve: {
                    strategies: function () {
                        return Rest.one("strategy/list").get()
                    },
                    accountId: function () {
                        return account.id;
                    }
                }
            });

            modalInstance.result.then(
                function (strategyToBind) {
                    console.log("Strategy added to account"+ strategyToBind.name);
                    $scope.strategies.push(strategyToBind);
                }, function () {
                    console.log('Modal dismissed at: ' + new Date());
                }
            );
        };
    })
    .controller('AccountBindCtrl', function ($scope, strategies, accountId, Rest) {
        $scope.strategies = strategies;
        $scope.accountBinder = {};
        $scope.accountBinder.state = 'INACTIVE';
        $scope.accountBinder.id = accountId;
        $scope.accountBinder.lot = 1;

        $scope.bind = function (strategyToBind, accountBinder) {
            console.log("Strategy bound" + strategyToBind.name);
            Rest.all("strategy/"+strategyToBind.id+"/account").post(accountBinder).then(
                function (response) {
                    var boundStrategy = {
                        strategy : strategyToBind,
                        lot : accountBinder.lot,
                        state : accountBinder.state
                    }
                    $scope.$close(boundStrategy);
                },
                function (error) {
                    $scope.$close(error);
                }
            );
        }
    });
