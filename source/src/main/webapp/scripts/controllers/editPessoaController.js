angular.module('taskqueue-api').controller('EditPessoaController', function($scope, $routeParams, $timeout, $location, flash, PessoaResource ) {
	var self = this;
	$scope.disabled = false;
	$scope.$location = $location;
	
	$scope.get = function() {
		var successCallback = function(data){
			self.original = data;
			$scope.pessoa = new PessoaResource(self.original);
		};
		var errorCallback = function() {
			mensagem = {};
			mensagem.key = 'msg.erro.usuario.nao.encontrado';
			mensagem.params = [];
			mensagem.type = 'error';
			$timeout(function() {flash.setMessage(mensagem);}, 0);
			$location.path("/Pessoas");
		};
		PessoaResource.get({PessoaId:$routeParams.PessoaId}, successCallback, errorCallback);
	};

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.pessoa);
	};

	$scope.save = function() {
		var successCallback = function(){
			mensagem = {};
			mensagem.key = 'msg.registro.salvo.sucesso';
			mensagem.params = [];
			mensagem.type = 'success';
			flash.setMessage(mensagem);
			$scope.get();
		};
		var errorCallback = function(response) {
			mensagem = {};
			mensagem.key = 'msg.erro.nao.identificado';
			mensagem.params = [];
			mensagem.type = 'error';
			flash.setMessage(mensagem);
		};
		$scope.pessoa.$update(successCallback, errorCallback);
	};

	$scope.cancel = function() {
		$location.path("/Pessoas");
	};

	$scope.remove = function() {
		var successCallback = function() {
			mensagem = {};
			mensagem.key = 'msg.registro.excluido.sucesso';
			mensagem.params = [];
			mensagem.type = 'success';
			$timeout(function() {flash.setMessage(mensagem);}, 0);
			$location.path("/Pessoas");
		};
		var errorCallback = function(response) {
			mensagem = {};
			mensagem.key = 'msg.erro.nao.identificado';
			mensagem.params = [];
			mensagem.type = 'error';
			flash.setMessage(mensagem);
		}; 
		$scope.pessoa.$remove(successCallback, errorCallback);
	};
	
	
	$scope.get();
});