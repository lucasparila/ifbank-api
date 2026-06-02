package com.ifbank.api_ifbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifbank.api_ifbank.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	Cliente findByUsuarioId(Long id);
}
