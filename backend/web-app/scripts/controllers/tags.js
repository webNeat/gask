'use strict';

app.controller('TagsCtrl', function ($scope, $window, $routeParams, db, err){
	$scope.tags = db.Tag.all(function(){
		for(var i in $scope.tags)
			tIndexes[$scope.tags[i].id] = i;
	});
});