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
                    accounts : function(Rest){
                        return Rest.one("account/list").get();
                    }
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
                    account : function($stateParams, Rest){
                        return Rest.one("account/"+$stateParams.accountId).get();
                    },
                    strategies: function ($stateParams, Rest){
                        return Rest.one("account/"+$stateParams.accountId+"/strategies").get();
                    }
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
    .controller('AccountEditCtrl', function ($scope, $timeout, $uibModal, $location, account, strategies, Rest, Notify, Helper) {
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
                function (selectedItem) {
                    $scope.selected = selectedItem;
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
            Rest.all("strategy/"+strategyToBind+"/account").post(accountBinder).then(
                function (response) {
                    $scope.$close(response);
                },
                function (error) {
                    $scope.$close(error);
                }
            );
        }
    });
