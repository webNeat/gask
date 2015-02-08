'use strict';

app.controller('AskCtrl', function ($scope, $window, db, err){
	var c = db.User.current(function(){
		if(c.id == -1)
			$window.location.href = Config.appURL + '#/login';
	});

	$scope.question = {title: '', content: '', tags: []};
	$scope.tags = [];
	$scope.allTags = db.Tag.all();
	$scope.errs = [];
	var submitted = false;
	var validate = function(fields, we){
		if(!submitted) return true;
		if(we === undefined) we = true;
		var result = true;
		if(we) $scope.errs = [];
		if(fields.indexOf('title') != -1){
			if($scope.question.title.trim() == ''){
				if(we) $scope.errs.push('Please give a title !');
				result = false;
			}
		}
		if(fields.indexOf('content') != -1){
			if($scope.question.content.trim() == ''){
				if(we) $scope.errs.push('Please describe your problem !');
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

	var submitTags = function(){
		var flag = true;
		for(var k = 0; k < $scope.question.tags.length; k ++){
			if($scope.question.tags[k].id == -1){
				var tt = $scope.question.tags[k];
				flag = false;
				var t = db.Tag.create({name: tt.name, content: 'Nothing yet !'}, function(){
					if(t.done){
						tt.id = t.id;
					}
					submitTags();
				});
			}
		}
		if(flag)
			submitQuestion();
	}

	var submitQuestion = function(){
		var r = db.Question.create(JSON.stringify($scope.question), function(){
			if(r.done){
				$window.location.href = Config.appURL + '#/questions';
			} else {
				$scope.err = err.get(r.errs);
			}
		});
	}

	$scope.create = function(){
		submitted = true;
		$scope.question.tags = [];
		$scope.tags.forEach(function(t){
			if(t.hasOwnProperty('id')){
				$scope.question.tags.push({id: t.id, name: t.text});
			} else {
				$scope.question.tags.push({id: -1, name: t.text});
			}
		});
		if(validate(['title','content'])){
			submitTags();			
		}
	}
});
