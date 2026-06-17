package com.ifbank.api_ifbank.repository;

import com.ifbank.api_ifbank.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByContaIdOrderByDataMovimentoDesc(Long idConta);
}