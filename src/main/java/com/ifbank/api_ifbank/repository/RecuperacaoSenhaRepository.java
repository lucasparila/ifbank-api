package com.ifbank.api_ifbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ifbank.api_ifbank.model.RecuperacaoSenha;

@Repository
public interface RecuperacaoSenhaRepository extends JpaRepository<RecuperacaoSenha, Long> {
    RecuperacaoSenha findByToken(String token);
}