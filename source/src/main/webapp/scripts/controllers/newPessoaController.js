
angular.module('taskqueue-api').controller('NewPessoaController', function ($scope,$timeout, $location, locationParser, flash, PessoaResource ) {
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.pessoa = $scope.pessoa || {};

	$scope.save = function() {
		var successCallback = function(data,responseHeaders){
			var id = locationParser(responseHeaders);
			var mensagem = {};
			mensagem.key = 'msg.registro.salvo.sucesso';
			mensagem.params = [];
			mensagem.type = 'success';
			$timeout(function() {flash.setMessage(mensagem);}, 0);
			
			$location.path('/Pessoas');
		};
		var errorCallback = function(response) {
			mensagem = {};
			mensagem.key = 'msg.erro.nao.identificado';
			mensagem.params = [];
			mensagem.type = 'error';
			flash.setMessage(mensagem);
		};
		PessoaResource.save($scope.pessoa, successCallback, errorCallback);
	};
	
	$scope.cancel = function() {
		$location.path("/Pessoas");
	};
});