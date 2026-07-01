package com.ifbank.api_ifbank.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ifbank.api_ifbank.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

	Conta findByClienteId(Long id);

	Conta findByNumeroConta(String numeroConta);

	@Query("SELECT c FROM Conta c JOIN FETCH c.cliente cli JOIN FETCH cli.usuario u WHERE c.idStatusConta = :statusId")
	List<Conta> findByStatusContaFetch(@Param("statusId") Integer statusId);

	//(Pendente/Ativa/Inativa/Rejeitada)
	@Query(value = "SELECT c FROM Conta c JOIN FETCH c.cliente cli JOIN FETCH cli.usuario u WHERE c.idStatusConta = :statusId",
		   countQuery = "SELECT COUNT(c) FROM Conta c WHERE c.idStatusConta = :statusId")
	Page<Conta> findByStatusContaFetchPaginado(@Param("statusId") Integer statusId, Pageable pageable);
}