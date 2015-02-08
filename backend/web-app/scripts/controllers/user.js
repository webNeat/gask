'use strict';

app.controller('UserCtrl', function ($scope, $window, $routeParams, db, err){
	$scope.users = db.User.all();
	$scope.current = db.User.current(function(){
		if($scope.current.id == -1)
			$window.location.href = Config.appURL + '#/login';
	});
	
});
