package com.ifbank.api_ifbank.repository;

import com.ifbank.api_ifbank.model.Movimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByContaIdOrderByDataMovimentoDesc(Long idConta);

    @Query("SELECT m FROM Movimentacao m " +
           "JOIN m.conta c " +
           "JOIN c.cliente cli " +
           "JOIN cli.usuario u " +
           "JOIN m.tipoMovimento tm " +
           "WHERE c.id = :idConta " +
           "AND (:nome IS NULL OR :nome = '' OR LOWER(cli.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :nome, '%')) " +
           "OR LOWER(tm.tipoMovimento) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
           "AND (:valor IS NULL OR m.valor = :valor)")
    Page<Movimentacao> buscarPorContaNomeOuValor(@Param("idConta") Long idConta,
                                                 @Param("nome") String nome,
                                                 @Param("valor") BigDecimal valor,
                                                 Pageable pageable);
}