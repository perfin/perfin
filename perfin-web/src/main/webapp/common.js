var app = angular.module('perfin', ['ngResource', 'ngRoute', 'ngGrid', 'ui.bootstrap', 'ui.bootstrap.datepicker']);

app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/overview', {
            templateUrl: 'view/overview.html'
        }).
        when('/transaction', {
            templateUrl: 'view/transaction.html'
        }).
        when('/category', {
            templateUrl: 'view/category.html'
        }).
        when('/resource', {
            templateUrl: 'view/resource.html'
        }).
        when('/currency', {
            templateUrl: 'view/currency.html'
        }).
        otherwise({
            redirectTo: '/overview'
        });
}]);

// Create a controller with name alertMessagesController to bind to the feedback messages section.
app.controller('alertMessagesController', function ($scope) {
    // Picks up the event to display a saved message.
    $scope.$on('currencySaved', function () {
        $scope.alerts = [
            {type: 'success', msg: 'Record saved successfully!'}
        ];
    });

    // Picks up the event to display a deleted message.
    $scope.$on('currencyDeleted', function () {
        $scope.alerts = [
            {type: 'success', msg: 'Record deleted successfully!'}
        ];
    });

    // Picks up the event to display a server error message.
    $scope.$on('error', function () {
        $scope.alerts = [
            {type: 'danger', msg: 'There was a problem on the server!'}
        ];
    });

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
});
