'use strict';
angular.module('taskqueue-api').config(['$translateProvider', '$windowProvider', function ($translateProvider, $windowProvider) {
	var language = $windowProvider.$get().navigator.language || $windowProvider.$get().navigator.userLanguage;
	var languageSuported = $.inArray(language, ['pt-BR', 'en-US']) >= 0;
	$translateProvider.preferredLanguage(languageSuported ? language : 'pt-BR');
}]).controller('TranslateController', function TranslateController($scope, $translate, $window, $log) {
	$scope.changeLanguage = function (key) {
		$translate.use(key);
	};
});