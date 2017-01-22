'use strict';

angular.module('taskqueue-api').config(function ($httpProvider) {

	$httpProvider.interceptors.push(function ($location, $cookies, $q, $log, flash, $timeout) {
		return {
			'request': function (request) {
				return request;
			},
			'responseError': function (response) {
				if (response.status == 409) {
					var mensagem = {};
					mensagem.key = response.data.message;
					mensagem.params = [];
					mensagem.type = 'error';
					$timeout(function() {flash.setMessage(mensagem);}, 0);
					$location.path('/Home');
				}
			}
		};
	});
});