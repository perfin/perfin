app.controller('transactionListController', function ($scope, $rootScope, transactionService) {
    // Initialize required information: sorting, the first page to show and the grid options.
    $scope.sortInfo = {fields: ['id'], directions: ['asc']};
    $scope.transactions = {currentPage: 1};

    $scope.gridOptions = {
        data: 'transactions.list',
        useExternalSorting: true,
        sortInfo: $scope.sortInfo,

        columnDefs: [
            { field: 'id', displayName: 'Id' },
            { field: 'resource.name', displayName: 'Resource' },
            { field: 'category.name', displayName: 'Category' },
            { field: 'amount', displayName: 'Amount' },
            { field: 'date', displayName: 'Date' },
            { field: 'note', displayName: 'Note' },
            { field: '', width: 30, cellTemplate: '<span class="glyphicon glyphicon-remove remove" ng-click="deleteRow(row)"></span>' }
        ],

        multiSelect: false,
        selectedItems: [],
        // Broadcasts an event when a row is selected, to signal the form that it needs to load the row data.
        afterSelectionChange: function (rowItem) {
            if (rowItem.selected) {
                $rootScope.$broadcast('transactionSelected', $scope.gridOptions.selectedItems[0].id);
            }
        }
    };

    // Refresh the grid, calling the appropriate rest method.
    $scope.refreshGrid = function () {
        var listTransactionArgs = {
            page: $scope.transactions.currentPage,
            sortFields: $scope.sortInfo.fields[0],
            sortDirections: $scope.sortInfo.directions[0]
        };

        transactionService.get(listTransactionArgs, function (data) {
            $scope.transactions = data;
        })
    };

    // Broadcast an event when an element in the grid is deleted. No real deletion is perfomed at this point.
    $scope.deleteRow = function (row) {
        $rootScope.$broadcast('deleteTransaction', row.entity.id);
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

    // Picks the event broadcasted when a transaction is saved or deleted to refresh the grid elements with the most
    // updated information.
    $scope.$on('refreshGrid', function () {
        $scope.refreshGrid();
    });

    // Picks the event broadcasted when the form is cleared to also clear the grid selection.
    $scope.$on('clear', function () {
        $scope.gridOptions.selectAll(false);
    });
});

// Create a controller with name transactionFormController to bind to the form section.
app.controller('transactionFormController', function ($scope, $rootScope, transactionService, resourceService, categoryService) {
    resourceService.get({all : true}, function (data) {
        $scope.resources = data.list;
    });

    categoryService.get({all : true}, function (data) {
        $scope.categories = data.list;
    });

    $scope.isOpen = false;
    $scope.openCalendar = function(e) {
        e.preventDefault();
        e.stopPropagation();
        $scope.isOpen = true;
    };

    // Clears the form. Either by clicking the 'Clear' button in the form, or when a successfull save is performed.
    $scope.clearForm = function () {
        $scope.transaction = {date: new Date()};
        // Resets the form validation state.
        $scope.transactionForm.$setPristine();
        // Broadcast the event to also clear the grid selection.
        $rootScope.$broadcast('clear');
    };

    // Calls the rest method to save a transaction.
    $scope.updateTransaction = function () {
        $scope.transaction.resource = _.findWhere($scope.resources, {id: $scope.transaction.resource.id});
        $scope.transaction.category = _.findWhere($scope.categories, {id: $scope.transaction.category.id});

        if ($scope.transaction.date instanceof Date) {
            $scope.transaction.date = $scope.transaction.date.toJSON().slice(0, 10);
        }

        transactionService.save($scope.transaction).$promise.then(
            function () {
                // Broadcast the event to refresh the grid.
                $rootScope.$broadcast('refreshGrid');
                // Broadcast the event to display a save message.
                $rootScope.$broadcast('transactionSaved');
                $scope.clearForm();
            },
            function () {
                // Broadcast the event for a server error.
                $rootScope.$broadcast('error');
            });
    };

    // Picks up the event broadcasted when the transaction is selected from the grid and perform the transaction load by
    // the appropiate rest service.
    $scope.$on('transactionSelected', function (event, id) {
        $scope.transaction = transactionService.get({id: id});
    });

    // Picks us the event broadcasted when the transaction is deleted from the grid and perform the actual transaction delete
    // by calling the appropiate rest service.
    $scope.$on('deleteTransaction', function (event, id) {
        transactionService.delete({id: id}).$promise.then(
            function () {
                // Broadcast the event to refresh the grid.
                $rootScope.$broadcast('refreshGrid');
                // Broadcast the event to display a delete message.
                $rootScope.$broadcast('transactionDeleted');
                $scope.clearForm();
            },
            function () {
                // Broadcast the event for a server error.
                $rootScope.$broadcast('error');
            });
    });
});

// Service that provides transactions operations
app.factory('transactionService', function ($resource) {
    return $resource('service/transactions/:id');
});
