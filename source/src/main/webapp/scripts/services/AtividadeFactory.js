angular.module('taskqueue-api').factory('AtividadeResource', function($resource){
	var resource = $resource('rest/atividades/:AtividadeId',{AtividadeId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
	return resource;
});