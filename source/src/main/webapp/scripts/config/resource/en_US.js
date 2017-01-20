'use strict';
angular.module('taskqueue-api').config(['$translateProvider', function ($translateProvider) {
	$translateProvider.translations('en-US', { 
		
		'app.name': 'Task Queue API',
		'app.corporation': 'Sysmap Solutions',
		'app.version': '1.0&#160;',
		
		'label.pagina': 'Page:',
		'label.nome': 'Name',
		'label.email': 'Email',
		'label.prev': '«',
		'label.next': '»',
		
		'option.selecione': 'Select',
		
		'bt.novo': 'New',
		'bt.incluir': 'Add',
		'bt.salvar': 'Save',
		'bt.excluir': 'Delete',
		'bt.cancelar': 'Cancel',
		'bt.voltar': 'Back',
		'bt.pesquisar': 'Search',
		'bt.adicionar': 'Add',
		'bt.close.modal': 'x',
		'bt.editar': 'edit',
		
		'menu.processamento': 'Processing',
		'menu.cadastro': 'Register',
		'sub.menu.pessoa': 'Person',
		'sub.menu.atividades': 'Activities',

		'msg.email.invalido': 'Invalid email.',
		'msg.registro.excluido.sucesso': 'Registry successfully deleted.',
		'msg.registro.salvo.sucesso': 'Registry successfully saved.',
		'msg.erro.nao.identificado': 'A system failure has occurred, please contact your system administrator.',
		'msg.valor.data.invalido': 'Invalid date value',
		'msg.erro.email.cadastrado': 'Email already registered in the system.',
	});
}]);