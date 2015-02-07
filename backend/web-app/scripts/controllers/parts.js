'use strict';

app.controller('PartsCtrl', function ($scope, $window, db){
	$scope.current = {id: -1};
	$scope.isUser = false;
	$scope.isAdmin = false;
	$scope.update = function(){
		$scope.current = db.User.current(function(){
			$scope.isUser = ( $scope.current.id != -1 );
			$scope.isAdmin = $scope.current.isAdmin;
		});
	}
	$scope.update();
	$scope.logout = function(){
		var r = db.User.logout(function(){
			if(r.done){
				$scope.update();
				$window.location.reload();
			}
		});
	}
});