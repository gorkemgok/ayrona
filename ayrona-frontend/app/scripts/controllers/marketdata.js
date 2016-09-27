/**
 * Created by gorkemgok on 04/06/16.
 */
'use strict';

angular.module('ayronaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('import-marketdata', {
                url: '/import-marketdata',
                templateUrl: 'views/import-marketdata.html',
                controller: 'ImportMarketDataCtrl'
            });
    })
    .controller("ImportMarketDataCtrl", function ($scope, Session, FileUploader, REST) {
        var prepareForUpload = function (symbol) {
            var uploader = new FileUploader({
                url: REST.base + '/marketdata/import?symbol='+symbol,
                headers: {
                    token: Session.token
                }
            });

            $scope.uploader = uploader;

            uploader.onProgressAll = function (progress) {
                console.info('onProgressAll', progress);
            };
            uploader.onCompleteAll = function () {
                console.info('onCompleteAll');
                delete $scope.uploader;
                delete $scope.symbol;
            };
        };

        $scope.$watch('symbol', function (newValue) {
            if (newValue != undefined) {
                console.log(newValue);
                prepareForUpload(newValue);
            }
        });
    });
