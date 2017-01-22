package br.com.sysmap.taskqueue.model.tipos;

public enum OperacaoTempo {
	SOMA() {
		@Override
		public Long calcular(Long tempo) {
			return tempo += 10;
		}
	},
	SUBTRACAO {
		@Override
		public Long calcular(Long tempo) {
			return tempo -= 10;
		}
	};
	
	public abstract Long calcular(Long tempo);
	
	
	
}
