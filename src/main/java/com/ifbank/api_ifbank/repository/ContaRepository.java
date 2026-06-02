package com.ifbank.api_ifbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifbank.api_ifbank.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
	Conta findByClienteId(Long id);
}
