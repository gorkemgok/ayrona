angular.module('ayronaApp')
    .config(function($stateProvider){
        $stateProvider
            .state('dashboard', {
                url: '/dashboard', 
                templateUrl: 'views/dashboard.html', 
                controller:'DashboardCtrl',
                resolve:{
                    stat : ["Rest", function (Rest) {
                        return Rest.one('dashboard').get();
                    }]
                }
            });
    })
    .controller('DashboardCtrl', function ($scope, $rootScope, $state, $timeout, $location, stat, Session) {
        $scope.stat = stat;
        console.log("Dashboard stat loaded");
        if (!Session.isActive()){
            $location.path("/login");
        }
    });
