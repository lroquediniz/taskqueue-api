angular.module('taskqueue-api').factory('ExecucaoResource', function($resource, $http) {
	var resource = $resource('rest/execucao');
	
	resource.recuperaAtividadesPendentes = function (success, error) {
		return	$http.get('rest/execucao/recuperaAtividadesPendentes').then(success, error);
	}
	return resource;
});