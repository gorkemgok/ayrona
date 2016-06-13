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
                    }
                }
            })
    })
    .controller('AccountListCtrl', function ($scope, $timeout, $location, accounts, Rest, Notify, Helper) {
        $scope.accounts = accounts;
        $scope.delete = function (accountId) {
            Rest.all("account/"+accountId).remove().then(
                function (response) {
                    $location.path("/account/list");
                },
                function (error) {
                    console.log(error);
                }
            );
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
    .controller('AccountEditCtrl', function ($scope, $timeout, $location, account, Rest, Notify, Helper) {
        $scope.account = account;
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
        }
    });
