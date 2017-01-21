angular.module('taskqueue-api').factory('SocketResource', function($websocket) 	{
	
	var path = window.location.pathname; 
	var contextoWeb = path.substring(0, path.indexOf('/', 1)); 
	var dataStream = "ws://" + window.location.host + contextoWeb + "/process";

	var execucao = {};

	dataStream.onMessage(function(message) {
		execucao = JSON.parse(message.data);
	});

	var methods = {
		execucao: execucao,
		get: function() {
			dataStream.send(JSON.stringify({ action: 'get' }));
		}
	};

	return methods;
});