package br.com.cotiinformatica.domain.enums;

public enum StatusPedido {
	NOVO(0),
	PROCESSANDO(1),
	ENVIADO(2),
	ENTREGUE(3),
	CANCELADO(4);
	
	private final int code;
	
	private StatusPedido(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static StatusPedido fromInt(int code) {
		for(var status : StatusPedido.values()) {
			if(status.getCode() == code) {
				return status;
			}
		}
		
		throw new IllegalArgumentException("Código inválido para Status de Pedido: " + code);
	}
}
