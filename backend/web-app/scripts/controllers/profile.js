'use strict';

app.controller('ProfileCtrl', function ($scope, $window, $routeParams, db, err){
	var id = parseInt($routeParams.id);
	if(isNaN(id))
		$window.location.href = Config.appURL + '#/';
	$scope.isConnected = false;
	$scope.current = db.User.get({id: id}, function(){
		if($scope.current.id == -1)
			$window.location.href = Config.appURL + '#/';
		var c = db.User.current(function(){
			if(c.id == id)
				$scope.isConnected = true;
		});
	});
	$scope.msg = '';
	$scope.questions = db.User.questions({id: id});
	var tagIds = [];
	var currentTagIds = [];
	var tagsToRemove = [];
	var tagsToAdd = [];
	$scope.tags = db.User.tags({id: id}, function(){
		for( var i in $scope.tags ){
			$scope.tags[i].text = $scope.tags[i].name;
			currentTagIds.push($scope.tags[i].id);
		}
	});
	$scope.allTags = db.Tag.all();

	$scope.loadTags = function(q){
		var list = [];
		$scope.allTags.forEach(function(t){
			if(t.name.substring(0, q.length) == q)
				list.push({id: t.id, text: t.name});
		});
		return {
			then: function(callback){
				callback(list);
			}
		};
	}

	var submitFavorites = function(){
		if(tagsToRemove.length > 0){
			var id = tagsToRemove.pop();
			var t = db.Tag.removeFromFav({id: id}, function(){
				submitFavorites();
			});
		} else if (tagsToAdd.length > 0){
			var id = tagsToAdd.pop();
			var t = db.Tag.addToFav({id: id}, function(){
				submitFavorites();
			});
		} else {
			$scope.msg = 'Saved Successfuly';
			$window.location.reload();
		}
	}

	var tempTags = [];
	var submitTags = function(){
		if(tempTags.length == 0){
			currentTagIds.forEach(function(t){
				if(tagIds.indexOf(t) == -1 && !isNaN(t))
					tagsToRemove.push(t);
			});
			tagIds.forEach(function(t){
				if(currentTagIds.indexOf(t) == -1 && !isNaN(t))
					tagsToAdd.push(t);
			});
			console.log('to add : ' + tagsToAdd);
			console.log('to remove : ' + tagsToRemove);
			submitFavorites();
		} else {
			var p = tempTags.pop();
			var found = false;
			for(var k in $scope.allTags){
				if($scope.allTags[k].name == p.text){
					tagIds.push($scope.allTags[k].id);
					found = true;
					break;
				}
			}
			if( ! found ){
				var t = db.Tag.create({name: p.text, content: 'Nothing yet !'}, function(){
					tagIds.push(t.id);
					submitTags();
				});
			}
		}
	}

	$scope.updateTags = function(){
		tagIds = [];
		for(var i in $scope.tags ){
			if(! $scope.tags[i].hasOwnProperty('id') && $scope.tags[i].text !== undefined){
				tempTags.push($scope.tags[i]);
			} else if ($scope.tags[i].hasOwnProperty('id'))
				tagIds.push($scope.tags[i].id);
		}
		$scope.allTags = db.Tag.all(function(){
			submitTags();
		});
	}


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
