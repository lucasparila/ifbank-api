package com.ifbank.api_ifbank.repository;

import com.ifbank.api_ifbank.model.TipoMovimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMovimentoRepository extends JpaRepository<TipoMovimento, Long> {
    TipoMovimento findByTipoMovimento(String tipoMovimento);
}