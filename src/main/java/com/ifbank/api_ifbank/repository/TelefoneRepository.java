package com.ifbank.api_ifbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifbank.api_ifbank.model.Telefone;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
