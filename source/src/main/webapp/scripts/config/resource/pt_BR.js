'use strict';
angular.module('taskqueue-api').config(['$translateProvider', function ($translateProvider) {
	$translateProvider.translations('pt-BR', { 
		
		'app.name': 'Task Queue API',
		'app.name.abrv': 'Task Queue',
		'app.description': 'API de acompanhamento de execução de atividades.',
		'app.corporation': 'Sysmap Solutions',
		'app.version': '1.0&#160;',
		
		'label.pagina': 'Página:',
		'label.nome': 'Nome',
		'label.email': 'Email',
		'label.prev': '«',
		'label.next': '»',
		'label.tarefas.concluidas': 'Tarefas concluídas',
		'label.tarefas.pendentes': 'Tarefas pendentes',
		'label.tarefas.andamento': 'Tarefas em andamento',
		
		'option.selecione': 'Selecione',
		
		'bt.novo': 'Novo',
		'bt.incluir': 'Incluir',
		'bt.salvar': 'Salvar',
		'bt.excluir': 'Excluir',
		'bt.cancelar': 'Cancelar',
		'bt.voltar': 'Voltar',
		'bt.pesquisar': 'Pesquisar',
		'bt.adicionar': 'Adicionar',
		'bt.close.modal': 'x',
		'bt.editar': 'Editar',
		
		'menu.processamento': 'Processamento',
		'menu.cadastro': 'Cadastro',
		'sub.menu.pessoa': 'Pessoa',
		'sub.menu.atividades': 'Atividades',

		'msg.email.invalido': 'Email inválido.',
		'msg.registro.excluido.sucesso': 'Registro excluído com sucesso.',
		'msg.registro.salvo.sucesso': 'Registro salvo com sucesso.',
		'msg.erro.nao.identificado': 'Ocorreu uma falha de sistema, favor contactar o administrador do sistema.',
		'msg.valor.data.invalido': 'Valor de data inválido.',
		'msg.erro.email.cadastrado': 'Email já cadastrado no sistema',
		'msg.erro.usuario.nao.encontrado': 'Usuário não encontrado.',
	});
}]);