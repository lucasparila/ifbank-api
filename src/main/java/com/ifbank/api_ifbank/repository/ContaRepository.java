package com.ifbank.api_ifbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifbank.api_ifbank.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
	Conta findByClienteId(Long id);
	@Query("SELECT c FROM Conta c JOIN FETCH c.cliente cli JOIN FETCH cli.usuario u WHERE c.idStatusConta = :statusId")
    List<Conta> findByStatusContaFetch(@Param("statusId") Integer statusId);
}
