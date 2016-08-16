/**
 * Created by gorkemgok on 27/01/16.
 */
'use strict';

angular.module('ayronaApp')
    .config(function($stateProvider){
        $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: 'views/login.html',
                controller:'LoginCtrl'
            });
    })
  .controller('LoginCtrl', function ($scope, $location, $base64, Rest, Session) {
        $scope.credentials = {};
        $scope.login = function(credentials){
            var auth = $base64.encode(credentials.username+":"+credentials.password);
            Rest.one("/login").get('',{auth:auth, broker:''}).then(
                function(response){
                    var token = response.token;
                    Session.start(token);
                    Rest.setDefaultHeaders({"Access-Control-Allow-Origin": "*", token:token});
                    console.log("redirecting to dashboard");
                    $location.path("/dashboard");
                },
                function(response){
                    $scope.error = {message:response.data.message};
                }
            );
        };
  });

