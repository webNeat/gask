'use strict';

var app = angular.module('gask', [
    'ngAnimate',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ui.bootstrap',
    'ui.pagedown',
    'ngTagsInput'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'VisitorCtrl'
      })
      .when('/register', {
        templateUrl: 'views/register.html',
        controller: 'VisitorCtrl'
      })
      .when('/users', {
        templateUrl: 'views/users.html',
        controller: 'UserCtrl'
      })
      .when('/profile/:id', {
        templateUrl: 'views/profile.html',
        controller: 'ProfileCtrl'
      })
      .when('/ask', {
        templateUrl: 'views/ask.html',
        controller: 'AskCtrl'
      })
      .when('/questions', {
        templateUrl: 'views/questions.html',
        controller: 'QuestionsCtrl'
      })
      .when('/question/:id', {
        templateUrl: 'views/question.html',
        controller: 'QuestionCtrl'
      })
      .otherwise({
        redirectTo: '/questions'
      });
  });

var Config = {};

Config.appURL = 'http://localhost:8080/gask-backend/';