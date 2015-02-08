'use strict';

app.controller('ProfileCtrl', function ($scope, $window, $routeParams, db, err){
	var id = parseInt($routeParams.id);
	if(isNaN(id))
		$window.location.href = Config.appURL + '#/';
	$scope.isConnected = false;
	$scope.current = db.User.get({id: id}, function(){
		var c = db.User.current(function(){
			if(c.id == id)
				$scope.isConnected = true;
		});
	});

	$scope.errs = [];
	var submitted = false;
	var validate = function(fields, we){
		if(!submitted) return true;
		if(we === undefined) we = true;
		var i = $scope.current;
		var result = true;
		if(we) $scope.errs = [];
		if(fields.indexOf('name') != -1){
			if(i.name.trim() == ''){
				if(we) $scope.errs.push('Please answer your name !');
				result = false;
			}
		}
		if(fields.indexOf('email') != -1){
			if(i.email.trim() == ''){
				if(we) $scope.errs.push('Please answer your email !');
				result = false;
			}
		}
		if(fields.indexOf('password') != -1){
			if(i.password.trim() == ''){
				if(we) $scope.errs.push('Please answer your password !');
				result = false;
			}
		}
		if(fields.indexOf('rePassword') != -1){
			if(i.rePassword != i.password){
				if(we) $scope.errs.push('The two passwords are not the same !');
				result = false;
			}
		}
		return result;
	}

	$scope.errorClass = function(field){
		if(!validate([field], false))
			return 'has-error';
		return '';
	}

	$scope.update = function(){
		submitted = true;
		if(validate(['email','name'])){
			var r = new db.User.update(JSON.stringify($scope.current), function(){
				if(r.done){
					$window.location.reload();
				} else {
					$scope.err = err.get(r.errs);
				}
			});
		}
	}
});
