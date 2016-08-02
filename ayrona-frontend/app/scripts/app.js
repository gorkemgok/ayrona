'use strict';

angular.module('ayronaApp', [
    'ayronaServices',
    'ayronaRestServices',
    'ayronaConstants',
    'ayronaFilters',
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ui.router',
    'ngAnimate',
    'ui.bootstrap',
    'ui.select',
    'restangular',
    'chart.js',
    'base64',
    'angularFileUpload',
    'ui.ace'
    ])
    .config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/dashboard');
    })
    .run(function ($state, $rootScope, SessionService) {
        $rootScope.uiState = $state;
        $rootScope.logout = function () {
            SessionService.stop();
        };
        var token = window.localStorage.getItem('token');
        if (token != undefined && token != ''){
            SessionService.start(token);
        };
    })




