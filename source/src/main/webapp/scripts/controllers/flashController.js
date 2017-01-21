'use strict';
angular.module('taskqueue-api').controller('FlashController', ['$scope', '$timeout', 'flash', function ($scope,$timeout, flash) {
		$scope.flash = flash;
		$scope.showAlert = false;
		$scope.$watch('flash.getMessage()', function (newVal) {
			var message = newVal;
			if (message && message.text) {
				$scope.showAlert = message.text.length > 0;
				$timeout(function() {
					$scope.showAlert = false;
				}, 3000);
			} else {
				$scope.showAlert = false;
			}
		});
		$scope.hideAlert = function () {
			$scope.showAlert = false;
		}
	}]);
