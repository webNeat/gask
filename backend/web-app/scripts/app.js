'use strict';

var app = angular.module('gask', [
    'ngAnimate',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
     'ngSanitize'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/user', {
        templateUrl: 'views/user.html',
        controller: 'UserCtrl'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'UserCtrl'
      })
      .when('/register', {
        templateUrl: 'views/register.html',
        controller: 'UserCtrl'
      })
      .when('/profile', {
        templateUrl: 'views/profile.html',
        controller: 'UserCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

var Config = {};

Config.appURL = 'http://localhost:8080/gask-backend/';