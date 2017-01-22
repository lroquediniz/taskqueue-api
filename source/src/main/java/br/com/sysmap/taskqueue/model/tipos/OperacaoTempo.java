package br.com.sysmap.taskqueue.model.tipos;

/**
 * Enum com metodo de implementação.
 * @author Luan Roque.
 *
 */
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
	/**
	 * Metodo abstrato para alterar tempo.
	 * @param tempo
	 * @return {@link Long}
	 */
	public abstract Long calcular(Long tempo);
	
	
	
}
