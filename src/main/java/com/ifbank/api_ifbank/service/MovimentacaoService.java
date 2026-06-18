package com.ifbank.api_ifbank.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ifbank.api_ifbank.model.Conta;
import com.ifbank.api_ifbank.model.Movimentacao;
import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoRequestDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoResponseDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.TransferenciaRequestDTO;
import com.ifbank.api_ifbank.model.enums.StatusConta;
import com.ifbank.api_ifbank.model.TipoMovimento;
import com.ifbank.api_ifbank.repository.ContaRepository;
import com.ifbank.api_ifbank.repository.MovimentacaoRepository;
import com.ifbank.api_ifbank.repository.TipoMovimentoRepository;
import com.ifbank.api_ifbank.service.interfaces.IMovimentacaoService;

import jakarta.transaction.Transactional;

@Service
public class MovimentacaoService implements IMovimentacaoService {

    private final ContaRepository contaRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final TipoMovimentoRepository tipoMovimentoRepository;

    public MovimentacaoService(ContaRepository contaRepository,
                               MovimentacaoRepository movimentacaoRepository,
                               TipoMovimentoRepository tipoMovimentoRepository) {
        this.contaRepository = contaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.tipoMovimentoRepository = tipoMovimentoRepository;
    }

    @Override
    @Transactional
    public MovimentacaoResponseDTO depositar(MovimentacaoRequestDTO dto) {
        Conta conta = contaRepository.findById(dto.getIdConta())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        if (!conta.getStatusConta().equals(StatusConta.ATIVA)) {
            throw new RuntimeException("Operação não permitida. A conta não está ativa. Status atual: "
                    + conta.getStatusConta().name());
        }

        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do depósito deve ser maior que zero.");
        }

        conta.setSaldo(conta.getSaldo().add(dto.getValor()));
        contaRepository.save(conta);

        TipoMovimento tipo = tipoMovimentoRepository.findByTipoMovimento("DEPOSITO");
        if (tipo == null) {
            throw new RuntimeException("Tipo de movimento 'DEPOSITO' não configurado no banco.");
        }

        Movimentacao mov = new Movimentacao();
        mov.setConta(conta);
        mov.setValor(dto.getValor());
        mov.setDataMovimento(LocalDate.now());
        mov.setTipoMovimento(tipo);
        mov = movimentacaoRepository.save(mov);

        MovimentacaoResponseDTO resposta = new MovimentacaoResponseDTO();
        resposta.setId(mov.getId());
        resposta.setTipoMovimento(tipo.getTipoMovimento());
        resposta.setValor(mov.getValor());
        resposta.setSaldoAtualizado(conta.getSaldo());
        resposta.setDataMovimento(mov.getDataMovimento());

        return resposta;
    }

    @Override
    @Transactional
    public MovimentacaoResponseDTO sacar(MovimentacaoRequestDTO dto) {
        Conta conta = contaRepository.findById(dto.getIdConta())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        if (!conta.getStatusConta().equals(StatusConta.ATIVA)) {
            throw new RuntimeException("Operação não permitida. A conta não está ativa. Status atual: "
                    + conta.getStatusConta().name());
        }

        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do saque deve ser maior que zero.");
        }

        if (conta.getSaldo().compareTo(dto.getValor()) < 0) {
            throw new RuntimeException("Saldo insuficiente. Saldo atual: R$ " + conta.getSaldo());
        }

        conta.setSaldo(conta.getSaldo().subtract(dto.getValor()));
        contaRepository.save(conta);

        TipoMovimento tipo = tipoMovimentoRepository.findByTipoMovimento("SAQUE");
        if (tipo == null) {
            throw new RuntimeException("Tipo de movimento 'SAQUE' não configurado no banco.");
        }

        Movimentacao mov = new Movimentacao();
        mov.setConta(conta);
        mov.setValor(dto.getValor());
        mov.setDataMovimento(LocalDate.now());
        mov.setTipoMovimento(tipo);
        mov = movimentacaoRepository.save(mov);

        MovimentacaoResponseDTO resposta = new MovimentacaoResponseDTO();
        resposta.setId(mov.getId());
        resposta.setTipoMovimento(tipo.getTipoMovimento());
        resposta.setValor(mov.getValor());
        resposta.setSaldoAtualizado(conta.getSaldo());
        resposta.setDataMovimento(mov.getDataMovimento());

        return resposta;
    }
    
    @Override
    @Transactional
    public MovimentacaoResponseDTO transferir(TransferenciaRequestDTO dto) {
        // Valida valor
        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor da transferência deve ser maior que zero.");
        }

        // Busca e valida conta origem
        Conta contaOrigem = contaRepository.findById(dto.getIdConta())
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada."));

        if (!contaOrigem.getStatusConta().equals(StatusConta.ATIVA)) {
            throw new RuntimeException("Sua conta não está ativa.");
        }

        // Busca e valida conta destino
        Conta contaDestino = contaRepository.findByNumeroConta(dto.getNumeroContaDestino());
        if (contaDestino == null) {
            throw new RuntimeException("Conta destino não encontrada: " + dto.getNumeroContaDestino());
        }

        if (contaDestino.getId().equals(contaOrigem.getId())) {
            throw new RuntimeException("Não é possível transferir para a própria conta.");
        }

        if (!contaDestino.getStatusConta().equals(StatusConta.ATIVA)) {
            throw new RuntimeException("A conta destino não está ativa.");
        }

        // Valida saldo
        if (contaOrigem.getSaldo().compareTo(dto.getValor()) < 0) {
            throw new RuntimeException("Saldo insuficiente. Saldo atual: R$ " + contaOrigem.getSaldo());
        }

        // Movimenta saldos em memória
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(dto.getValor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(dto.getValor()));
        
        // Salva as atualizações de saldo no banco
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        // Busca os dois tipos de movimento configurados no seu banco de dados
        TipoMovimento tipoEnviada = tipoMovimentoRepository.findByTipoMovimento("TRANSFERENCIA_ENVIADA");
        if (tipoEnviada == null) {
            throw new RuntimeException("Tipo de movimento 'TRANSFERENCIA_ENVIADA' não configurado no banco.");
        }

        TipoMovimento tipoRecebida = tipoMovimentoRepository.findByTipoMovimento("TRANSFERENCIA_RECEBIDA");
        if (tipoRecebida == null) {
            throw new RuntimeException("Tipo de movimento 'TRANSFERENCIA_RECEBIDA' não configurado no banco.");
        }

        // 1. Registra o histórico de débito na conta de origem (QUEM ENVIA)
        Movimentacao movOrigem = new Movimentacao();
        movOrigem.setConta(contaOrigem);
        movOrigem.setContaDestino(contaDestino);
        movOrigem.setValor(dto.getValor());
        movOrigem.setDataMovimento(LocalDate.now());
        movOrigem.setTipoMovimento(tipoEnviada);
        movimentacaoRepository.save(movOrigem);

        // 2. Registra o histórico de crédito na conta de destino (QUEM RECEBE)
        Movimentacao movDestino = new Movimentacao();
        movDestino.setConta(contaDestino); // No extrato do destino, a conta principal é ela mesma
        movDestino.setContaDestino(contaOrigem); // E a conta vinculada é quem enviou o dinheiro
        movDestino.setValor(dto.getValor());
        movDestino.setDataMovimento(LocalDate.now());
        movDestino.setTipoMovimento(tipoRecebida);
        movimentacaoRepository.save(movDestino);

        // Retorna a resposta para quem executou a ação no Angular (Origem)
        MovimentacaoResponseDTO resposta = new MovimentacaoResponseDTO();
        resposta.setId(movOrigem.getId());
        resposta.setTipoMovimento("TRANSFERENCIA_ENVIADA");
        resposta.setValor(dto.getValor());
        resposta.setSaldoAtualizado(contaOrigem.getSaldo());
        resposta.setDataMovimento(movOrigem.getDataMovimento());

        return resposta;
    }

    @Override
    public List<MovimentacaoResponseDTO> listarPorConta(Long idConta) {
        contaRepository.findById(idConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        return movimentacaoRepository.findByContaIdOrderByDataMovimentoDesc(idConta)
                .stream()
                .map(mov -> {
                    MovimentacaoResponseDTO dto = new MovimentacaoResponseDTO();
                    dto.setId(mov.getId());
                    dto.setTipoMovimento(mov.getTipoMovimento().getTipoMovimento());
                    dto.setValor(mov.getValor());
                    dto.setDataMovimento(mov.getDataMovimento());
                    // saldoAtualizado fica null no histórico — não temos snapshot por movimentação
                    return dto;
                })
                .collect(Collectors.toList());
    }
}