'use strict';

app.controller('QuestionCtrl', function ($scope, $location, $routeParams, db, err){
	var id = parseInt($routeParams.id);
	if(isNaN(id))
		$location.path('/questions');

	$scope.isConnected = false;
	var c = db.User.current(function(){
		$scope.isConnected = (c.id != -1);
	});

	$scope.q = {content: '#Loading ...'};	

	var q = db.Question.get({id: id}, function(){
		$scope.q = q;
	});

	$scope.votesTotal = 0;

	$scope.tags = db.Question.tags({id: id});
	
	var updateAnswers = function(){
		$scope.answers = db.Question.answers({id: id});	
	}
	
	var updateComments = function(){
		console.log('updating comments !');
		$scope.comments = db.Question.comments({id: id});
	}

	var updateVotes = function(){
		$scope.votes = db.Question.votes({id: id}, function(){
			$scope.votesTotal = 0;
			$scope.votes.forEach(function(v){
				$scope.votesTotal += v.value;
			});
			// $scope.$digest();
		});
	}

	updateVotes();
	updateComments();
	updateAnswers();

	$scope.upVoteQuestion = function() {
		var r = db.Question.upVote({id: $scope.q.id}, function(){
			updateVotes();
		});
	};
	$scope.downVoteQuestion = function() {
		var r = db.Question.downVote({id: $scope.q.id}, function(){
			updateVotes();
		});
	};

	$scope.comment = {content : ''};
	$scope.addComment = function(){
		var r = db.Comment.create({content: $scope.comment.content, qId: $scope.q.id }, function(){
			if(r.done)
				updateComments();
		});
	}

});