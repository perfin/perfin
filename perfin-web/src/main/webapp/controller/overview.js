app.controller('statisticsController', function ($scope, $rootScope, statisticsService) {
    statisticsService.get([], function (data) {
        $scope.statistics = data;
    })
});

app.factory('statisticsService', function ($resource) {
    return $resource('service/statistics');
});