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
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider) {
        $urlRouterProvider.otherwise('/dashboard');
        $httpProvider.defaults.cache = false;
    })
    .run(function ($state, $rootScope, $timeout, Session, AynRest, Rest, SYMBOLS, PERIODS) {
        $rootScope.SYMBOLS = SYMBOLS;
        $rootScope.PERIODS = PERIODS;
        $rootScope.uiState = $state;
        $rootScope.logout = function () {
            Session.stop();
            Rest.setDefaultHeaders({"Access-Control-Allow-Origin": "*", token:''});
        };
        var token = window.localStorage.getItem('token');
        if (token != undefined && token != ''){
            console.log("Session started with stored token");
            Session.start(token);
            Rest.setDefaultHeaders({"Access-Control-Allow-Origin": "*", token:token});
        };

        var updateEdrList = function (newEdrList) {
            var edrList = $rootScope.edrList;
            if (edrList){
                var del = edrList.length - newEdrList.length;
                if (del > 0) {
                    for (i = 0; i < del; i++) {
                        edrList.splice(i, 1);
                    }
                }else{
                    $rootScope.edrList = newEdrList;
                }
            }else{
                $rootScope.edrList = newEdrList;
            }
        };

        var checkEdr = function (lastDate) {
            var date = lastDate;
            if (!lastDate){
                date = new Date();
                date.setTime(0);
            }
            if (Session.isActive()) {
                console.log("Checked edr since "+date);
                AynRest.checkEdr('','', date.toISOString(),
                    function (response) {
                        updateEdrList(response);
                    },
                    function (error) {
                        console.log(response);
                    }
                );
            }
            $timeout(function () {
                //checkEdr(date);
            }, 10000);
        };

        checkEdr();
    });




