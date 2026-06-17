package com.ifbank.api_ifbank.model.enums;

public enum StatusConta {
	PENDENTE(1),
    ATIVA(2),
    INATIVA(3),
    REJEITADA(4);

    private final Integer id;

    StatusConta(Integer id) {
        this.id = id;
    }

    public Integer getId() {
    	return this.id;
    }
    public static StatusConta fromId(Integer id) {
        for (StatusConta tipo : values()) {
            if (tipo.getId().equals(id)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("ID de status de conta inválido: " + id);
    }
    
}
