'use strict';
app.factory('db', function($resource, $rootScope){
	var db = {};

	db.User = $resource('/gask-backend/user/:action/:id', {}, {
		all: { method: 'GET', isArray:true, 
			params: { action: 'index', id: '' }
		},
		current: { method: 'GET',
			params: { action: 'current', id: '' }
		},
		login: { method: 'POST',
			params: { action: 'login', id: '' }
		},
		logout: { method: 'POST',
			params: { action: 'logout', id: '' }
		}
	});

	return db;
});