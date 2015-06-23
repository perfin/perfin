app.controller('statisticsController', function ($scope, $rootScope, statisticsService) {
    statisticsService.get([], function (data) {
        $scope.statistics = data;
    })
});

app.factory('statisticsService', function ($resource) {
    return $resource('service/statistics');
});

app.controller('balanceController', function ($scope, ATP) {
    $scope.resources = ATP.send('BALANCES');
});

app.factory('ATP', function ($websocket) {
    // Open a WebSocket connection
    var ws = $websocket("ws://" + document.location.host + document.location.pathname + "balances");
    var atp = [];

    ws.onMessage(function (event) {
        console.log('message: ', event.data);
        var response;
        try {
            response = angular.fromJson(event.data);
        } catch (e) {
            console.log(e)
        }

        console.log(response)
    });
    ws.onError(function (event) {
        console.log('connection Error', event);
    });
    ws.onClose(function (event) {
        console.log('connection closed', event);
    });
    ws.onOpen(function () {
        console.log('connection open');
    });

    return {
        atp: atp,
        status: function () {
            return ws.readyState;
        },
        send: function (message) {
            if (angular.isString(message)) {
                ws.send(message);
            }
            else if (angular.isObject(message)) {
                ws.send(JSON.stringify(message));
            }
        }
    };
});
