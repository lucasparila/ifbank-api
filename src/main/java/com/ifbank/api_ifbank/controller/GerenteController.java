package com.ifbank.api_ifbank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifbank.api_ifbank.model.DTO.perfil.PerfilClienteCompletoDTO;
import com.ifbank.api_ifbank.model.DTO.perfil.PerfilGerenteCompletoDTO;
import com.ifbank.api_ifbank.service.interfaces.IGerenteService;

@RestController
@RequestMapping("/api/gerente")
@CrossOrigin(origins = "*")
public class GerenteController {
	
	private final IGerenteService gerenteService;
	
	public GerenteController(IGerenteService gerenteService) {
		this.gerenteService = gerenteService;
	}
	
	@GetMapping("/contas-pendentes")
    public ResponseEntity<List<PerfilClienteCompletoDTO>> listarContasPendentes() {
        List<PerfilClienteCompletoDTO> pendentes = gerenteService.buscarContasPendentes();
        return ResponseEntity.ok(pendentes);
    }

    @PutMapping("/aprovar-conta/{idConta}")
    public ResponseEntity<?> aprovarConta(@PathVariable Long idConta) {
        try {
            gerenteService.aprovarContaCliente(idConta);
            return ResponseEntity.ok("Conta aprovada com sucesso e liberada para uso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/reprovar-conta/{idConta}")
    public ResponseEntity<?> reprovarConta(@PathVariable Long idConta) {
        try {
            gerenteService.reprovarContaCliente(idConta);
            return ResponseEntity.ok("Conta reprovada com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obterPerfil(@PathVariable Long idUsuario) {
        try {
            PerfilGerenteCompletoDTO perfilCompleto = gerenteService.obterPerfilCompleto(idUsuario);
           
            return ResponseEntity.ok(perfilCompleto);
            
        } catch (RuntimeException e) {
           
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getLocalizedMessage());
        }
    }
}
