'use strict';
angular.module('taskqueue-api',['ngRoute', 'ngResource', 'ui.mask', 'ngCookies', 'ui.bootstrap', 'ng-currency', 'ng-decimal', 'pascalprecht.translate'])
	.config(['$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {
		$routeProvider
			.when('/Home',{templateUrl:'views/landing.html',controller:'LandingPageController'})
			.when('/Atividades',{templateUrl:'views/Atividade/search.html',controller:'SearchAtividadeController'})
			.when('/Atividades/new',{templateUrl:'views/Atividade/detail.html',controller:'NewAtividadeController'})
			.when('/Atividades/edit/:AtividadeId',{templateUrl:'views/Atividade/detail.html',controller:'EditAtividadeController'})
			.when('/Pessoas',{templateUrl:'views/Pessoa/search.html',controller:'SearchPessoaController'})
			.when('/Pessoas/new',{templateUrl:'views/Pessoa/detail.html',controller:'NewPessoaController'})
			.when('/Pessoas/edit/:PessoaId',{templateUrl:'views/Pessoa/detail.html',controller:'EditPessoaController'})
			.otherwise({
				redirectTo: '/Home'
		});
	}]).run(function ($rootScope, $location, $http, $cookies, $window, flash) {
		
		$rootScope.defaultErrorCallback = function (response) {
			var mensagem = {};
			mensagem.key = 'msg.erro.nao.identificado';
			mensagem.params = [];
			mensagem.type = 'error';
		   	flash.setMessage(mensagem);
		};
		function init() {
			$window.scrollTo(0, 0);
		}
	 })
	.controller('LandingPageController', function LandingPageController() {
	})
	.controller('NavController', function NavController($scope, $location) {
		$scope.matchesRoute = function(route) {
			var path = $location.path();
			return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
		};
	});
