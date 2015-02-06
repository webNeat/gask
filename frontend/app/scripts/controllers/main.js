'use strict';

/**
 * @ngdoc function
 * @name gaskApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the gaskApp
 */
angular.module('gaskApp')
  .controller('MainCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
