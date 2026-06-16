package com.ifbank.api_ifbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifbank.api_ifbank.model.AplicacaoInvestimento;

@Repository
public interface AplicacaoInvestimentoRepository extends JpaRepository<AplicacaoInvestimento, Long> {
    List<AplicacaoInvestimento> findByContaId(Long idConta);
    List<AplicacaoInvestimento> findByContaIdAndStatus(Long idConta, String status);
}