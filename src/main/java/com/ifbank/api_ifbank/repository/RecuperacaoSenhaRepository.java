package com.ifbank.api_ifbank.repository;

import com.ifbank.api_ifbank.model.RecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecuperacaoSenhaRepository extends JpaRepository<RecuperacaoSenha, Long> {

    RecuperacaoSenha findByToken(String token);

    // Usado pelo reset de senha: pega todos com esse token, ordenados do mais recente
    List<RecuperacaoSenha> findByTokenOrderByIdDesc(String token);

    // Retorna TODOS os registros do usuário (pode haver mais de um por testes/tentativas)
    List<RecuperacaoSenha> findByUsuarioId(Long usuarioId);
}