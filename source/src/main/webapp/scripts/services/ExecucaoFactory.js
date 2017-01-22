angular.module('taskqueue-api').factory('ExecucaoResource', function($resource, $http) {
	var resource = $resource('rest/execucao');
	
	resource.recuperaAtividadesPendentes = function (success, error) {
		return	$http.get('rest/execucao/recuperaAtividadesPendentes').then(success, error);
	}
	
	resource.acelerarTempo = function (success, error) {
		return	$http.get('rest/execucao/alterarTempo/SUBTRACAO').then(success, error);
	}
	
	resource.retardarTempo = function (success, error) {
		return	$http.get('rest/execucao/alterarTempo/SOMA').then(success, error);
	}
	
	return resource;
});