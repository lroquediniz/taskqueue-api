
angular.module('taskqueue-api').controller('NewAtividadeController', function ($scope, $location, locationParser, flash, AtividadeResource , PessoaResource) {
	$scope.disabled = false;
	$scope.$location = $location;
	$scope.atividade = $scope.atividade || {};
	$scope.atividade.pessoa = {};
	$scope.pessoaList = PessoaResource.queryAll();
	
	$scope.statusList = [
		"PENDENTE",
		"EM_ANDAMENTO",
		"CONCLUIDO"
	];
	

	$scope.save = function() {
		var successCallback = function(data,responseHeaders){
			var id = locationParser(responseHeaders);
			flash.setMessage({'type':'success','text':'The atividade was created successfully.'});
			$location.path('/Atividades');
		};
		var errorCallback = function(response) {
			if(response && response.data) {
				flash.setMessage({'type': 'error', 'text': response.data.message || response.data}, true);
			} else {
				flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
			}
		};
		AtividadeResource.save($scope.atividade, successCallback, errorCallback);
	};
	
	$scope.cancel = function() {
		$location.path("/Atividades");
	};
});