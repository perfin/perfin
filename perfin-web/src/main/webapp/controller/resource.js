app.controller('resourceListController', function ($scope, $rootScope, resourceService) {
    // Initialize required information: sorting, the first page to show and the grid options.
    $scope.sortInfo = {fields: ['id'], directions: ['asc']};
    $scope.resources = {currentPage: 1};

    $scope.gridOptions = {
        data: 'resources.list',
        useExternalSorting: true,
        sortInfo: $scope.sortInfo,

        columnDefs: [
            { field: 'id', displayName: 'Id' },
            { field: 'name', displayName: 'Name' },
            { field: 'currency.code', displayName: 'Currency' },
            { field: 'balance', displayName: 'Balance' },
            { field: '', width: 30, cellTemplate: '<span class="glyphicon glyphicon-remove remove" ng-click="deleteRow(row)"></span>' }
        ],

        multiSelect: false,
        selectedItems: [],
        // Broadcasts an event when a row is selected, to signal the form that it needs to load the row data.
        afterSelectionChange: function (rowItem) {
            if (rowItem.selected) {
                $rootScope.$broadcast('resourceSelected', $scope.gridOptions.selectedItems[0].id);
            }
        }
    };

    // Refresh the grid, calling the appropriate rest method.
    $scope.refreshGrid = function () {
        var listResourceArgs = {
            page: $scope.resources.currentPage,
            sortFields: $scope.sortInfo.fields[0],
            sortDirections: $scope.sortInfo.directions[0]
        };

        resourceService.get(listResourceArgs, function (data) {
            $scope.resources = data;
        })
    };

    // Broadcast an event when an element in the grid is deleted. No real deletion is perfomed at this point.
    $scope.deleteRow = function (row) {
        $rootScope.$broadcast('deleteResource', row.entity.id);
    };

    // Watch the sortInfo variable. If changes are detected than we need to refresh the grid.
    // This also works for the first page access, since we assign the initial sorting in the initialize section.
    $scope.$watch('sortInfo.fields[0]', function () {
        $scope.refreshGrid();
    }, true);

    // Do something when the grid is sorted.
    // The grid throws the ngGridEventSorted that gets picked up here and assigns the sortInfo to the scope.
    // This will allow to watch the sortInfo in the scope for changed and refresh the grid.
    $scope.$on('ngGridEventSorted', function (event, sortInfo) {
        $scope.sortInfo = sortInfo;
    });

    // Picks the event broadcasted when a resource is saved or deleted to refresh the grid elements with the most
    // updated information.
    $scope.$on('refreshGrid', function () {
        $scope.refreshGrid();
    });

    // Picks the event broadcasted when the form is cleared to also clear the grid selection.
    $scope.$on('clear', function () {
        $scope.gridOptions.selectAll(false);
    });
});

// Create a controller with name resourceFormController to bind to the form section.
app.controller('resourceFormController', function ($scope, $rootScope, resourceService, currencyService) {
    $scope.currencies = [
        { id: 1, code: 'EUR' },
        { id: 2, code: 'USD' }
    ];
    currencyService.get({all : true}, function (data) {
        $scope.currencies = data.list;
    });
    $scope.selectedCurrency = null;

    // Clears the form. Either by clicking the 'Clear' button in the form, or when a successfull save is performed.
    $scope.clearForm = function () {
        $scope.resource = null;
        // For some reason, I was unable to clear field values with type 'url' if the value is invalid.
        // This is a workaroud. Needs proper investigation.
        document.getElementById('imageUrl').value = null;
        // Resets the form validation state.
        $scope.resourceForm.$setPristine();
        // Broadcast the event to also clear the grid selection.
        $rootScope.$broadcast('clear');
    };

    // Calls the rest method to save a resource.
    $scope.updateResource = function () {
        $scope.resource.currency = _.findWhere($scope.currencies, {id: $scope.selectedCurrency});

        resourceService.save($scope.resource).$promise.then(
            function () {
                // Broadcast the event to refresh the grid.
                $rootScope.$broadcast('refreshGrid');
                // Broadcast the event to display a save message.
                $rootScope.$broadcast('resourceSaved');
                $scope.clearForm();
            },
            function () {
                // Broadcast the event for a server error.
                $rootScope.$broadcast('error');
            });
    };

    // Picks up the event broadcasted when the resource is selected from the grid and perform the resource load by
    // the appropiate rest service.
    $scope.$on('resourceSelected', function (event, id) {
        $scope.resource = resourceService.get({id: id});
    });

    // Picks us the event broadcasted when the resource is deleted from the grid and perform the actual resource delete
    // by calling the appropiate rest service.
    $scope.$on('deleteResource', function (event, id) {
        resourceService.delete({id: id}).$promise.then(
            function () {
                // Broadcast the event to refresh the grid.
                $rootScope.$broadcast('refreshGrid');
                // Broadcast the event to display a delete message.
                $rootScope.$broadcast('resourceDeleted');
                $scope.clearForm();
            },
            function () {
                // Broadcast the event for a server error.
                $rootScope.$broadcast('error');
            });
    });
});

// Service that provides resources operations
app.factory('resourceService', function ($resource) {
    return $resource('service/resources/:id');
});
