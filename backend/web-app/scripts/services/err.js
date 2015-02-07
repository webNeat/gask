'use strict';
app.factory('err', function($resource, $rootScope){
	var err = {};
	err.get = function(id){
		return id;
	}
	return err;
});