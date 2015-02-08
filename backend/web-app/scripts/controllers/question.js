'use strict';

app.controller('QuestionCtrl', function ($scope, $location, $routeParams, db, err){
	var id = parseInt($routeParams.id);
	if(isNaN(id))
		$location.path('/questions');

	$scope.q = db.Question.get({id: id}, function(){
		$scope.$digest();
	});
	$scope.tags = db.Question.tags({id: id});
	$scope.votes = db.Question.votes({id: id});
	$scope.answers = db.Question.answers({id: id});
	$scope.comments = db.Question.comments({id: id});

});