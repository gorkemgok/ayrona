/**
 * Created by gorkemgok on 25/09/2016.
 */
angular.module('ayronaApp')
    .controller("StartEndDatePickerCtrl", function ($scope) {
        $scope.startDate = new Date(2015, 1, 1);
        $scope.endDate = new Date();
        $scope.dateOptions = {
            formatYear: 'yy',
            maxDate: new Date(2020, 5, 22),
            minDate: new Date(2010, 1, 1),
            startingDay: 1
        };

        $scope.format = "dd-MM-yyyy";

        $scope.openStart = function () {
            $scope.startDatePopupOpen = true;
        };

        $scope.openEnd = function () {
            $scope.endDatePopupOpen = true;
        };
    })
    .controller("GraphCtrl", function ($scope) {
        $scope.series = ['Equity', 'Profit', 'MDD'];
        $scope.datasetOverride = [
            {
                yAxisID: 'y-axis-1',
                tension : 0,
                stepped:true,
                pointRadius : 0,
                borderColor : 'black'
            },
            {
                yAxisID: 'y-axis-2',
                tension : 0,
                pointRadius : 0
            },
            {
                yAxisID: 'y-axis-3',
                tension : 0,
                pointRadius : 0,
                borderWidth : 1
            }
        ];
        $scope.lineOptions = {
            scales: {
                xAxes: [{
                    display: true
                }],
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'linear',
                        display: true,
                        position: 'left',
                    },
                    {
                        id: 'y-axis-2',
                        type: 'linear',
                        display: true,
                        position: 'right'
                    },
                    {
                        id: 'y-axis-3',
                        type: 'linear',
                        display: true,
                        position: 'left'
                    }
                ]
            }
        };
        $scope.barOptions = {
            scales: {
                xAxes: [{
                    display: true
                }],
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'bar',
                        display: true,
                        position: 'left'
                    },
                    {
                        id: 'y-axis-2',
                        type: 'bar',
                        display: true,
                        position: 'right'
                    }
                ]
            }
        };
    });
