'use strict';

angular.module('ayronaApp')
  .config(function($stateProvider){
    $stateProvider
      .state('dashboard', {
        url: '/dashboard',
        templateUrl: 'views/dashboard.html',
        controller:'DashboardCtrl'
      });
  })
  .controller('DashboardCtrl', function ($scope, $rootScope, $state, $timeout, Restangular) {


  });
