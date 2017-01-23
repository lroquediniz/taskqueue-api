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
		
		$scope.processando = false;
		
		$scope.atividadesSearch = {};
		$scope.exibeAtividadesPendentes = false;
		$scope.exibeAtividadesConcluidas = false;
		
		
		$scope.pesquisarAtividadesPendentes = function () {
			$scope.exibeAtividadesConcluidas = false;
			$scope.exibeAtividadesPendentes = !$scope.exibeAtividadesPendentes;
			if($scope.exibeAtividadesPendentes){
				var successCallback = function(response){
					$scope.atividadesSearch =  response.data;
				};
				ExecucaoResource.recuperaAtividadesPendentes(successCallback, $rootScope.defaultErrorCallback);
			}
		}
		
		$scope.pesquisarAtividadesConcluidas = function () {
			$scope.exibeAtividadesPendentes = false;
			$scope.exibeAtividadesConcluidas = !$scope.exibeAtividadesConcluidas;
			if($scope.exibeAtividadesConcluidas){
				var successCallback = function(response){
					$scope.atividadesSearch =  response.data;
				};
				ExecucaoResource.recuperaAtividadesProcessadas(successCallback, $rootScope.defaultErrorCallback);
			}

		}

		
		$scope.recuperaTaferasPendentes = function() {
			var successCallback = function(response){
				var tarefasPendentes = response.data;
				if(tarefasPendentes) {
					$scope.execucao.qtdTarefasPendentes = tarefasPendentes.length;
				}else{
					$scope.execucao.qtdTarefasPendentes = 0;
				}
			};
			ExecucaoResource.recuperaAtividadesPendentes(successCallback, $rootScope.defaultErrorCallback);
		}
		
		$scope.init = function() {
			$scope.execucao = {};
			$scope.execucao.qtdTarefasConcluidas = 0;
			$scope.execucao.porcentagem = 0;
			$scope.recuperaTaferasPendentes();
		}
		
		$scope.acelerarTempo = function() {
			var successCallback = function(response){
				var mensagem = {};
				mensagem.key = 'msg.tempo.acelerado';
				mensagem.params = [];
				mensagem.type = 'success';
				flash.setMessage(mensagem);
			};
			ExecucaoResource.acelerarTempo(successCallback, $rootScope.defaultErrorCallback);
			
		}
		
		$scope.retardarTempo = function() {
			var successCallback = function(response){
				var mensagem = {};
				mensagem.key = 'msg.tempo.retardado';
				mensagem.params = [];
				mensagem.type = 'success';
				flash.setMessage(mensagem);
			};
			ExecucaoResource.retardarTempo(successCallback, $rootScope.defaultErrorCallback);
		}
		
		$scope.connectWebSocket = function() {
			var path = window.location.pathname; 
			var contextoWeb = path.substring(0, path.indexOf('/', 1)); 
			var dataStream = "ws://" + window.location.host + contextoWeb + "/process";
			$scope.taskSocket = new WebSocket(dataStream);
			$scope.taskSocket.onmessage = function(message) {
				var data = JSON.parse(message.data);
				if(data.porcentagem <= 100){
					$scope.execucao = JSON.parse(message.data);
					$scope.processando = data.porcentagem < 100;
					$scope.$apply();	   
				}
			};
			$scope.taskSocket.onclose = function(evt) {
				$scope.processando = false;
				$scope.recuperaTaferasPendentes();
			};
		}
		
		$scope.iniciarExecucaoAtividades = function() {
			$scope.processando = true;
			var successCallback = function(response){
				if(response.status == 204){
					$scope.init();
					var mensagem = {};
					mensagem.key = 'msg.nenhuma.atividade.cadastrada';
					mensagem.params = [];
					mensagem.type = 'error';
					flash.setMessage(mensagem);
					$scope.init()
				}else{
					var mensagem = {};
					mensagem.key = 'msg.execucao.atividades.iniciada';
					mensagem.params = [];
					mensagem.type = 'success';
					flash.setMessage(mensagem);
				}
				
			};
			ExecucaoResource.iniciarExecucao(successCallback,$rootScope.defaultErrorCallback);
			
		}
		
		$scope.connectWebSocket();
		$scope.init();
		
	})
	.controller('NavController', function NavController($scope, $location) {
		$scope.matchesRoute = function(route) {
			var path = $location.path();
			return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
		};
	});
