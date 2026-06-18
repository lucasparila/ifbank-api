package com.ifbank.api_ifbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifbank.api_ifbank.model.Gerente;

public interface GerenteRepository extends JpaRepository<Gerente, Long> {
	Gerente findByUsuarioId(Long id);
}
