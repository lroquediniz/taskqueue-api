angular.module('taskqueue-api').controller('EditAtividadeController', function($scope, $routeParams, $location, flash, AtividadeResource , PessoaResource) {
	var self = this;
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.pessoaList = PessoaResource.queryAll();
	
	$scope.get = function() {
		var successCallback = function(data){
			self.original = data;
			$scope.atividade = new AtividadeResource(self.original);
		};
		var errorCallback = function() {
			mensagem = {};
			mensagem.key = 'msg.erro.usuario.nao.encontrado';
			mensagem.params = [];
			mensagem.type = 'error';
			$timeout(function() {flash.setMessage(mensagem);}, 0);
			$location.path("/Atividades");
		};
		AtividadeResource.get({AtividadeId:$routeParams.AtividadeId}, successCallback, errorCallback);
	};

	$scope.isClean = function() {
		return angular.equals(self.original, $scope.atividade);
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
		$scope.atividade.$update(successCallback, errorCallback);
	};

	$scope.cancel = function() {
		$location.path("/Atividades");
	};

	$scope.remove = function() {
		var successCallback = function() {
			mensagem = {};
			mensagem.key = 'msg.registro.excluido.sucesso';
			mensagem.params = [];
			mensagem.type = 'success';
			$timeout(function() {flash.setMessage(mensagem);}, 0);
			$location.path("/Atividades");
		};
		var errorCallback = function(response) {
			mensagem = {};
			mensagem.key = 'msg.erro.nao.identificado';
			mensagem.params = [];
			mensagem.type = 'error';
			flash.setMessage(mensagem);
		}; 
		$scope.atividade.$remove(successCallback, errorCallback);
	};
	
	$scope.$watch("pessoaSelection", function(selection) {
		if (typeof selection != 'undefined') {
			$scope.atividade.pessoa = {};
			$scope.atividade.pessoa = selection.value;
		}
	});
	$scope.statusList = [
		"PENDENTE",  
		"EM_ANDAMENTO",  
		"CONCLUIDO"  
	];
	
	$scope.get();
});