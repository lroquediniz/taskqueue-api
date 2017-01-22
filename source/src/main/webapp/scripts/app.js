'use strict';
angular.module('taskqueue-api',['ngRoute', 'ngResource', 'ui.mask', 'ngCookies', 'ui.bootstrap', 'ng-currency', 'ng-decimal', 'pascalprecht.translate', 'ngWebSocket'])
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
	.controller('LandingPageController', function LandingPageController($scope, $rootScope, $location, ExecucaoResource,flash) {
		//{"qtdTarefasPendentes":0,"qtdTarefasAndamento":1,"porcentagem":0,"atualizacaoAtividades":[{"idAtividade":1,"percentualExecucao":0}]}
		
		$scope.init = function() {
			$scope.execucao = {};
			$scope.execucao.qtdTarefasPendentes = 0;
			$scope.execucao.qtdTarefasConcluidas = 0;
			$scope.execucao.porcentagem = 0;
			
			
			
			var successCallback = function(response){
				$scope.tarefasPendentes = response.data;
				if($scope.tarefasPendentes) {
					$scope.execucao.qtdTarefasPendentes = $scope.tarefasPendentes.length;
					$scope.tarefasPendentes = $scope.tarefasPendentes.length;
				}
			};
			var errorCallback = function(response) {
				$scope.execucao.qtdTarefasPendentes = 0;
			};
			ExecucaoResource.recuperaAtividadesPendentes(successCallback, errorCallback);
		}
		
		
		$scope.acelerarTempo = function() {
			var successCallback = function(response){
				var mensagem = {};
				mensagem.key = 'msg.tempo.acelerado';
				mensagem.params = [];
				mensagem.type = 'success';
				flash.setMessage(mensagem);
			};
			var errorCallback = function(response) {
				
			};
			
			ExecucaoResource.acelerarTempo(successCallback, errorCallback);
			
		}
		
		$scope.retardarTempo = function() {
			var successCallback = function(response){
				var mensagem = {};
				mensagem.key = 'msg.tempo.retardado';
				mensagem.params = [];
				mensagem.type = 'success';
				flash.setMessage(mensagem);
			};
			var errorCallback = function(response) {
				
			};
			ExecucaoResource.retardarTempo(successCallback, errorCallback);
		}
		
		$scope.iniciarExecucaoAtividades = function() {
			
			var successCallback = function(response){
				var mensagem = {};
				mensagem.key = 'msg.execucao.atividades.iniciada';
				mensagem.params = [];
				mensagem.type = 'success';
				flash.setMessage(mensagem);
			};
			var errorCallback = function(response) {
				var mensagem = {};
				mensagem.key = response.data.message;
				mensagem.params = [];
				mensagem.type = 'error';
				flash.setMessage(mensagem);
			};
			
			ExecucaoResource.get(successCallback,errorCallback);
		}
		
		$scope.init();
		
		var path = window.location.pathname; 
		var contextoWeb = path.substring(0, path.indexOf('/', 1)); 
		var dataStream = "ws://" + window.location.host + contextoWeb + "/process";
		var taskSocket = new WebSocket(dataStream);
		taskSocket.onmessage = function(message) {
			var data = JSON.parse(message.data);
			$scope.execucao = JSON.parse(message.data);
			if (data.porcentagem >= 100) {
				$scope.execucao.qtdTarefasPendentes = $scope.tarefasPendentes;
			} 
			$scope.$apply();	   
		};
		
	})
	.controller('NavController', function NavController($scope, $location) {
		$scope.matchesRoute = function(route) {
			var path = $location.path();
			return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
		};
	});
