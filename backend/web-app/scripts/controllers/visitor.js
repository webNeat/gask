'use strict';

app.controller('VisitorCtrl', function ($scope, $window, $routeParams, db, err){
	var c = db.User.current(function(){
		if(c.id != -1)
			$window.location.href = Config.appURL + '#/';
	});
	$scope.users = db.User.all();
	$scope.current = {name: '', email: '', password: '', rePassword: ''};
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

	$scope.login = function(){
		submitted = true;
		if(validate(['email','password'])){
			var r = new db.User.login($scope.current, function(){
				if(r.done){
					$window.location.href = Config.appURL + '#/';
					$window.location.reload();
				} else {
					$scope.err = err.get(r.errs);
				}
			});
		}
	}
	$scope.register = function(){
		submitted = true;
		if(validate(['email','password','rePassword','name'])){
			var r = new db.User.create($scope.current, function(){
				if(r.done){
					$window.location.href = Config.appURL +'#/login';
					$window.location.reload();
				} else {
					$scope.err = err.get(r.errs);
				}
			});
		}		
	}

});
