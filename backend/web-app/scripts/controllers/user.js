'use strict';

app.controller('UserCtrl', function ($scope, $window, db, err){
	$scope.users = db.User.all();
	$scope.current = {name: '', email: '', password: ''};
	$scope.err = '';

	$scope.login = function(){
		var r = new db.User.login($scope.current, function(data){
			if(r.done){
				$window.location.href = Config.appURL +'#/profile';
				$window.location.reload();
			} else {
				$scope.err = err.get(r.errs);
			}
		});
	}
});
