package com.ifbank.api_ifbank.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ifbank.api_ifbank.model.AplicacaoInvestimento;
import com.ifbank.api_ifbank.model.Conta;
import com.ifbank.api_ifbank.model.Movimentacao;
import com.ifbank.api_ifbank.model.TipoInvestimento;
import com.ifbank.api_ifbank.model.TipoMovimento;
import com.ifbank.api_ifbank.model.DTO.investimento.AplicarInvestimentoRequestDTO;
import com.ifbank.api_ifbank.model.DTO.investimento.InvestimentoDTO;
import com.ifbank.api_ifbank.model.DTO.investimento.ResumoInvestimentoDTO;
import com.ifbank.api_ifbank.repository.AplicacaoInvestimentoRepository;
import com.ifbank.api_ifbank.repository.ContaRepository;
import com.ifbank.api_ifbank.repository.MovimentacaoRepository;
import com.ifbank.api_ifbank.repository.TipoInvestimentoRepository;
import com.ifbank.api_ifbank.repository.TipoMovimentoRepository;

import jakarta.transaction.Transactional;

@Service
public class InvestimentoService {

    private final AplicacaoInvestimentoRepository aplicacaoRepository;
    private final ContaRepository contaRepository;
    private final TipoInvestimentoRepository tipoInvestimentoRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final TipoMovimentoRepository tipoMovimentoRepository;

    public InvestimentoService(AplicacaoInvestimentoRepository aplicacaoRepository,
                               ContaRepository contaRepository,
                               TipoInvestimentoRepository tipoInvestimentoRepository,
                               MovimentacaoRepository movimentacaoRepository,
                               TipoMovimentoRepository tipoMovimentoRepository) {
        this.aplicacaoRepository = aplicacaoRepository;
        this.contaRepository = contaRepository;
        this.tipoInvestimentoRepository = tipoInvestimentoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.tipoMovimentoRepository= tipoMovimentoRepository;
    }

    public List<TipoInvestimento> listarTipos() {
    List<TipoInvestimento> tipos = tipoInvestimentoRepository.findAll();

    System.out.println("=================================");
    System.out.println("TIPOS ENCONTRADOS: " + tipos.size());

    for (TipoInvestimento t : tipos) {
        System.out.println(
            t.getId() + " | " +
            t.getNome() + " | " +
            t.getValorMinimo()
        );
    }

    System.out.println("=================================");

    return tipos;
}

    public List<InvestimentoDTO> listarPorConta(Long idConta) {
        List<AplicacaoInvestimento> aplicacoes = aplicacaoRepository.findByContaId(idConta);
        return aplicacoes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ResumoInvestimentoDTO obterResumo(Long idConta) {
        List<AplicacaoInvestimento> ativas = aplicacaoRepository.findByContaIdAndStatus(idConta, "ATIVA");

        BigDecimal totalInvestido = ativas.stream()
                .map(AplicacaoInvestimento::getValorAplicado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal rendimentoAcumulado = ativas.stream()
                .map(a -> calcularRendimento(a))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResumoInvestimentoDTO(totalInvestido, rendimentoAcumulado, ativas.size());
    }


    @Transactional
    public InvestimentoDTO aplicar(AplicarInvestimentoRequestDTO dto) {
        Conta conta = contaRepository.findById(dto.getIdConta())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        TipoInvestimento tipo = tipoInvestimentoRepository.findById(dto.getIdTipoInvestimento())
                .orElseThrow(() -> new RuntimeException("Tipo de investimento não encontrado."));

        if (dto.getValorAplicado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor de aplicação deve ser maior que zero.");
        }

        if (dto.getValorAplicado().compareTo(tipo.getValorMinimo()) < 0) {
            throw new RuntimeException("Valor abaixo do mínimo para este tipo de investimento: R$ " + tipo.getValorMinimo());
        }

        if (conta.getSaldo().compareTo(dto.getValorAplicado()) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar esta aplicação.");
        }

        conta.setSaldo(conta.getSaldo().subtract(dto.getValorAplicado()));
        contaRepository.save(conta);

        AplicacaoInvestimento aplicacao = new AplicacaoInvestimento();
        aplicacao.setConta(conta);
        aplicacao.setTipoInvestimento(tipo);
        aplicacao.setValorAplicado(dto.getValorAplicado());
        aplicacao.setDataAplicacao(LocalDate.now());
        aplicacao.setStatus("ATIVA");

        aplicacao = aplicacaoRepository.save(aplicacao);
        
        TipoMovimento tipoMovimento = tipoMovimentoRepository.findByTipoMovimento("APLICACAO_INVESTIMENTO");
        if (tipoMovimento == null) {
            throw new RuntimeException("Tipo de movimento 'APLICACAO_INVESTIMENTO' não configurado no banco.");
        }

        Movimentacao mov = new Movimentacao();
        mov.setConta(conta);
        mov.setValor(dto.getValorAplicado());
        mov.setDataMovimento(LocalDate.now());
        mov.setTipoMovimento(tipoMovimento);
        mov = movimentacaoRepository.save(mov);
        
        return toDTO(aplicacao);
    }

    @Transactional
    public String resgatar(Long idAplicacao, Long idConta) {
        AplicacaoInvestimento aplicacao = aplicacaoRepository.findById(idAplicacao)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado."));

        if (!aplicacao.getConta().getId().equals(idConta)) {
            throw new RuntimeException("Este investimento não pertence à conta informada.");
        }

        if (!"ATIVA".equals(aplicacao.getStatus())) {
            throw new RuntimeException("Este investimento já foi resgatado.");
        }

        BigDecimal rendimento = calcularRendimento(aplicacao);
        BigDecimal valorTotal = aplicacao.getValorAplicado().add(rendimento);

        Conta conta = aplicacao.getConta();
        conta.setSaldo(conta.getSaldo().add(valorTotal));
        contaRepository.save(conta);

        aplicacao.setStatus("RESGATADA");
        aplicacaoRepository.save(aplicacao);
        
        TipoMovimento tipoMovimento = tipoMovimentoRepository.findByTipoMovimento("RESGATE_INVESTIMENTO");
        if (tipoMovimento == null) {
            throw new RuntimeException("Tipo de movimento 'RESGATE_INVESTIMENTO' não configurado no banco.");
        }

        Movimentacao mov = new Movimentacao();
        mov.setConta(conta);
        mov.setValor(valorTotal);
        mov.setDataMovimento(LocalDate.now());
        mov.setTipoMovimento(tipoMovimento);
        mov = movimentacaoRepository.save(mov);

        return "Resgate realizado com sucesso. Valor creditado: R$ " + valorTotal.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularRendimento(AplicacaoInvestimento aplicacao) {
        if (aplicacao.getTipoInvestimento().getRentabilidadeMes() == null) {
            return BigDecimal.ZERO;
        }
        long dias = ChronoUnit.DAYS.between(aplicacao.getDataAplicacao(), LocalDate.now());
        if (dias <= 0) return BigDecimal.ZERO;

        BigDecimal taxaMes = aplicacao.getTipoInvestimento().getRentabilidadeMes();
        BigDecimal taxaDia = taxaMes.divide(BigDecimal.valueOf(30), 10, RoundingMode.HALF_UP);
        BigDecimal fator = BigDecimal.ONE.add(taxaDia).pow((int) dias);
        BigDecimal valorFinal = aplicacao.getValorAplicado().multiply(fator);
        return valorFinal.subtract(aplicacao.getValorAplicado()).setScale(2, RoundingMode.HALF_UP);
    }
    

    private InvestimentoDTO toDTO(AplicacaoInvestimento aplicacao) {
        BigDecimal rendimento = calcularRendimento(aplicacao);
        BigDecimal taxa = aplicacao.getTipoInvestimento().getRentabilidadeMes() != null
                ? aplicacao.getTipoInvestimento().getRentabilidadeMes()
                : BigDecimal.ZERO;
        return new InvestimentoDTO(
                aplicacao.getId(),
                aplicacao.getTipoInvestimento().getNome(),
                aplicacao.getValorAplicado(),
                rendimento,
                taxa,
                aplicacao.getDataAplicacao(),
                aplicacao.getStatus()
        );
    }
}