'use strict';

/**
 * @ngdoc function
 * @name gaskApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the gaskApp
 */
angular.module('gaskApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
