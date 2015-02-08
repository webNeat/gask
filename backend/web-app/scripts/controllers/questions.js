'use strict';

app.controller('QuestionsCtrl', function ($scope, $window, $routeParams, db, err){
	var loadAllQuestions = function(){
		console.log('loadAllQuestions');
		$scope.questions = db.Question.all(function(){
			for(var i in $scope.questions)
				qIndexes[$scope.questions[i].id] = i;
		});
	}
	var loadQuestionsOfTag = function(tid){
		console.log('loadQuestionsOfTag ' + tid);
		$scope.questions = db.Tag.questions({id: tid}, function(){
			for(var i in $scope.questions)
				qIndexes[$scope.questions[i].id] = i;
		});
	}
	var tagId = $routeParams.tagId;
	if(isNaN(tagId))
		loadAllQuestions();
	else {
		var tag = db.Tag.get({id: tagId}, function(){
			if(tag.id == -1)
				loadAllQuestions();
			else {
				$scope.tag = tag;
				loadQuestionsOfTag(tag.id);
			}
		});
	}
	var qIndexes = {};
	var tIndexes = {};
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