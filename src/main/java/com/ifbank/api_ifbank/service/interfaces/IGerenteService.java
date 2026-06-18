package com.ifbank.api_ifbank.service.interfaces;

import java.util.List;

import com.ifbank.api_ifbank.model.DTO.perfil.PerfilClienteCompletoDTO;
import com.ifbank.api_ifbank.model.DTO.perfil.PerfilGerenteCompletoDTO;

public interface IGerenteService {
	 public List<PerfilClienteCompletoDTO> buscarContasPendentes();
	 public void aprovarContaCliente(Long idUsuario);
	 PerfilGerenteCompletoDTO obterPerfilCompleto(Long idUsuario);
}
