package br.com.sysmap.taskqueue.model.tipos;

public enum OperacaoTempo {
	SOMA() {
		@Override
		public Long calcular(Long tempo) {
			return tempo += 54;
		}
	},
	SUBTRACAO {
		@Override
		public Long calcular(Long tempo) {
			return tempo -= 54;
		}
	};
	
	public abstract Long calcular(Long tempo);
	
	
	
}
