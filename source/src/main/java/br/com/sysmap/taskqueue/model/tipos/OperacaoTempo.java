package br.com.sysmap.taskqueue.model.tipos;

public enum OperacaoTempo {
	SOMA() {
		@Override
		public Integer calcular(Integer tempo) {
			return tempo += 10;
		}
	},
	SUBTRACAO {
		@Override
		public Integer calcular(Integer tempo) {
			return tempo -= 10;
		}
	};
	
	public abstract Integer calcular(Integer tempo);
	
	
	
}
