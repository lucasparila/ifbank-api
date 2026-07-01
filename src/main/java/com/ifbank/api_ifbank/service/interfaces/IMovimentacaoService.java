package com.ifbank.api_ifbank.service.interfaces;

import com.ifbank.api_ifbank.model.DTO.extrato.ExtratoPdfResponseDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoRequestDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoResponseDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.TransferenciaRequestDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IMovimentacaoService {
    MovimentacaoResponseDTO depositar(MovimentacaoRequestDTO dto);
    MovimentacaoResponseDTO sacar(MovimentacaoRequestDTO dto);
    MovimentacaoResponseDTO transferir(TransferenciaRequestDTO dto);
    List<MovimentacaoResponseDTO> listarPorConta(Long idConta);
    Page<MovimentacaoResponseDTO> buscarExtrato(Long idConta, String nome, BigDecimal valor, LocalDate dataInicio,LocalDate dataFim, int pagina, int tamanho, String ordenacao, String direcao);
    ExtratoPdfResponseDTO montarExtratoPdf(Long idConta,String nome,BigDecimal valor,LocalDate dataInicio,LocalDate dataFim, String ordenacao,String direcao);
}