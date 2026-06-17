package com.ifbank.api_ifbank.service.interfaces;

import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoRequestDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoResponseDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.TransferenciaRequestDTO;

import java.util.List;

public interface IMovimentacaoService {
    MovimentacaoResponseDTO depositar(MovimentacaoRequestDTO dto);
    MovimentacaoResponseDTO sacar(MovimentacaoRequestDTO dto);
    MovimentacaoResponseDTO transferir(TransferenciaRequestDTO dto);
    List<MovimentacaoResponseDTO> listarPorConta(Long idConta);
}