package com.ifbank.api_ifbank.service.interfaces;

import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoRequestDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoResponseDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.TransferenciaRequestDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface IMovimentacaoService {
    MovimentacaoResponseDTO depositar(MovimentacaoRequestDTO dto);
    MovimentacaoResponseDTO sacar(MovimentacaoRequestDTO dto);
    MovimentacaoResponseDTO transferir(TransferenciaRequestDTO dto);
    List<MovimentacaoResponseDTO> listarPorConta(Long idConta);
    Page<MovimentacaoResponseDTO> buscarExtrato(Long idConta, String nome, BigDecimal valor, int pagina, int tamanho, String ordenacao, String direcao);
}