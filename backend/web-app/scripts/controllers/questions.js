'use strict';

app.controller('QuestionsCtrl', function ($scope, $window, db, err){
	var qIndexes = {};
	var tIndexes = {};
	$scope.questions = db.Question.all(function(){
		for(var i in $scope.questions)
			qIndexes[$scope.questions[i].id] = i;
	});
	$scope.tags = db.Tag.all(function(){
		for(var i in $scope.tags)
			tIndexes[$scope.tags[i].id] = i;
	});

	$scope.getTags = function(qid){
		var list = [];
		var q = $scope.questions[qIndexes[qid]];
		q.tags.forEach(function(t){
			list.push($scope.tags[tIndexes[t.id]]);
		});
		return list;
	}
});