package com.ifbank.api_ifbank.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ifbank.api_ifbank.model.DTO.perfil.PerfilClienteCompletoDTO;
import com.ifbank.api_ifbank.model.DTO.perfil.PerfilGerenteCompletoDTO;
import com.ifbank.api_ifbank.model.enums.StatusConta;

public interface IGerenteService {

	Page<PerfilClienteCompletoDTO> buscarContasPorStatus(StatusConta status, Pageable pageable);

	void aprovarContaCliente(Long idConta);

	void reprovarContaCliente(Long idConta);

	PerfilGerenteCompletoDTO obterPerfilCompleto(Long idUsuario);
}