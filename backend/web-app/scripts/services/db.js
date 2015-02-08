'use strict';
app.factory('db', function($resource, $rootScope){
	var db = {};

	db.User = $resource('/gask-backend/user/:action/:id', {}, {
		all: { method: 'GET', isArray: true, 
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
		},
		create: { method: 'POST',
			params: { action: 'create', id: '' }
		},
		update: { method: 'PUT',
			params: { action: 'update' }
		},
		get: { method: 'GET',
			params: { action: 'get' }
		}
	});

	db.Question = $resource('/gask-backend/question/:action/:id', {}, {
		all: { method: 'GET', isArray: true, 
			params: { action: 'index', id: '' }
		},
		create: { method: 'POST',
			params: { action: 'create', id: '' }
		},
		update: { method: 'PUT',
			params: { action: 'update' }
		},
		get: { method: 'GET',
			params: { action: 'get' }
		},
		tags: { method: 'GET',  isArray: true,
			params: { action: 'tags' }
		},
		comments: { method: 'GET',  isArray: true,
			params: { action: 'comments' }
		},
		answers: { method: 'GET',  isArray: true,
			params: { action: 'answers' }
		},
		votes: { method: 'GET',  isArray: true,
			params: { action: 'votes' }
		}
	});

	db.Tag = $resource('/gask-backend/tag/:action/:id', {}, {
		all: { method: 'GET', isArray: true, 
			params: { action: 'index', id: '' }
		},
		create: { method: 'POST',
			params: { action: 'create', id: '' }
		},
		update: { method: 'PUT',
			params: { action: 'update' }
		},
		get: { method: 'GET',
			params: { action: 'get' }
		}
	});

	return db;
});