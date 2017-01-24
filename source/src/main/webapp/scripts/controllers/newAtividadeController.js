
angular.module('taskqueue-api').controller('NewAtividadeController', function ($scope,$timeout , $location, locationParser, flash, AtividadeResource , PessoaResource) {
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.atividade = $scope.atividade || {};
	$scope.atividade.pessoa = {};
	$scope.pessoaList = PessoaResource.queryAll();

	$scope.save = function() {
		var successCallback = function(data,responseHeaders){
			var id = locationParser(responseHeaders);
			var mensagem = {};
			mensagem.key = 'msg.registro.salvo.sucesso';
			mensagem.params = [];
			mensagem.type = 'success';
			$timeout(function() {flash.setMessage(mensagem);}, 0);
			$location.path('/Atividades');
		};
		var errorCallback = function(response) {
			mensagem = {};
			mensagem.key = 'msg.erro.nao.identificado';
			mensagem.params = [];
			mensagem.type = 'error';
			flash.setMessage(mensagem);
		};
		AtividadeResource.save($scope.atividade, successCallback, errorCallback);
	};
	
	$scope.cancel = function() {
		$location.path("/Atividades");
	};
});