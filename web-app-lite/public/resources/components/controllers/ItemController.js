app.controller('ItemController', ['$scope','$routeParams','$location','$route','$timeout', 'BlueAPIService','UserInfoService',function($scope,$routeParams, $location,$route,$timeout, BlueAPIService, UserInfoService) {

	console.log("Entering Inventory Controller")
	//$scope.baseimURL = "/image/"
	$scope.loggedIn = UserInfoService.state.authenticated
	$scope.success = false;
	$scope.fail = false;

	angular.element('#stars').starrr();

	$scope.getStars = function () {
			$scope.count = angular.element('#stars').find('.fa-star.fa');
	}

	BlueAPIService.getItemById($routeParams.id, function (response) {
			console.log("Get Item Detail Result" + response)
			$scope.item = response.data
		}, function (error){
			console.log("Get Item Detail Result Error: " + error);
	});

	$scope.buy = function () {
			$scope.payload = {'count':$scope.itemQuantity,
												'itemId':$scope.item.id
											}

			BlueAPIService.buyItems(UserInfoService.state.accessToken, $scope.payload, function (response) {
					console.log("Buy Item Result" + response)
					$scope.result = response.data
					$scope.success = true;
					$scope.fail = false;

				}, function (error){
					console.log("Buy Item Error: " + error);
					$scope.success = false;
					$scope.fail = true;
			});
	}

}]);
