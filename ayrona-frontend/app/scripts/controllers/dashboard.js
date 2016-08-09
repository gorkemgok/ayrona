'use strict';

angular.module('ayronaApp')
    .config(function($stateProvider){
        $stateProvider
            .state('dashboard', {
                url: '/dashboard', 
                templateUrl: 'views/dashboard.html', 
                controller:'DashboardCtrl',
                resolve:{
                    stat : function (Rest) {
                        return Rest.one('dashboard').get();
                    }
                }
            });
    })
    .controller('DashboardCtrl', function ($scope, $rootScope, $state, $timeout, stat, Rest) {
        $scope.stat = stat;
        console.log("Dashboard stat loaded");
    });
