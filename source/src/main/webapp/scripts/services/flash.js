'use strict';
angular.module('taskqueue-api').factory('flash',
		[ '$rootScope',  '$translate', function($rootScope, $translate) {
			var messages = [];
			var currentMessage = {};

			$rootScope.$on('$routeChangeSuccess', function() {
				currentMessage = messages.shift() || {};
			});

			return {
				getMessage : function() {
					return currentMessage;
				},
				setMessage : function(message) {
					
					switch (message.type) {
					case "error" :
						message.cssClass = "danger";
						break;
					case "success" :
						message.cssClass = "success";
						break;
					case "info" :
						message.cssClass = "info";
						break;
					case "warning" :
						message.cssClass = "warning";
						break;
					}
					
					var m = translateMessage(message.key, message.params);
					message.text = m;
					currentMessage = message;
				}
			};
			function translateMessage(key, params) {
				var message = $translate.instant(key);
				if (params && params.length > 0) {
					$.each(params, function(index, param) {
						message = message.replace('{' + index + '}', param);
					});
				}
				return message;
			}
		} ]);

