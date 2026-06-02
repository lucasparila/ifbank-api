package com.ifbank.api_ifbank.model.enums;

import lombok.Getter;

@Getter
public enum TipoUsuario {
	CLIENTE(1L),
    GERENTE(2L);

    private final Long id;

    TipoUsuario(Long id) {
        this.id = id;
    }

    public Long getId() {
    	return this.id;
    }
    public static TipoUsuario fromId(Long id) {
        for (TipoUsuario tipo : values()) {
            if (tipo.getId().equals(id)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("ID de Tipo de Usuário inválido: " + id);
    }
}
