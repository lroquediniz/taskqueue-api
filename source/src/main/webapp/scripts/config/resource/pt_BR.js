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
		'label.percentual.conclusao': 'Percentual de conclusão',
		'label.produtividade': 'Produtividade',
		'label.descricao': 'Descrição',
		'label.esforco': 'Esforço',
		'label.pessoa': 'Pessoa',
		
		
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
		'bt.iniciar': 'Iniciar',
		'bt.mais.minutos': '+ 00:10',
		'bt.menos.minutos': '- 00:10',
		
		
		'menu.processamento': 'Processamento',
		'menu.cadastro': 'Cadastro',
		'sub.menu.pessoa': 'Pessoa',
		'sub.menu.atividades': 'Atividades',

		
		'title.pessoas': 'Pessoas',
		'title.pessoa': 'Pessoa',
		'title.atividades': 'Atividades',
		'title.atividade': 'Atividade',
		'title.dashboard': 'Painel',
		
		
		
		'msg.email.invalido': 'Email inválido.',
		'msg.registro.excluido.sucesso': 'Registro excluído com sucesso.',
		'msg.registro.salvo.sucesso': 'Registro salvo com sucesso.',
		'msg.erro.nao.identificado': 'Ocorreu uma falha de sistema, favor contactar o administrador do sistema.',
		'msg.nenhuma.atividade.cadastrada': 'Nenhuma atividade cadastrada no sistema.',
		'msg.execucao.atividades.iniciada': 'Execução de atividades executa.',
		'msg.tempo.acelerado': 'Tempo acelerado em 10 minutos',
		'msg.tempo.retardado': 'Tempo retardado em 10 minutos',
		
		
		'msg.erro.email.cadastrado': 'Email já cadastrado no sistema',
		'msg.erro.usuario.nao.encontrado': 'Usuário não encontrado.',
	});
}]);