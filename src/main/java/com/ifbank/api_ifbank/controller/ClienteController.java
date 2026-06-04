package com.ifbank.api_ifbank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifbank.api_ifbank.model.DTO.cadastro.CadastroClienteRequestDTO;
import com.ifbank.api_ifbank.model.DTO.perfil.PerfilCompletoDTO;
import com.ifbank.api_ifbank.service.interfaces.IUsuarioService;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private IUsuarioService usuarioService;
	
	public ClienteController(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> cadastrar(@ModelAttribute CadastroClienteRequestDTO dadosCadastro) {
        try {
            String mensagem = usuarioService.cadastrarCliente(dadosCadastro);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obterPerfil(@PathVariable Long idUsuario) {
        try {
            PerfilCompletoDTO perfilCompleto = usuarioService.obterPerfilCompleto(idUsuario);
           
            return ResponseEntity.ok(perfilCompleto);
            
        } catch (RuntimeException e) {
           
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getLocalizedMessage());
        }
    }
}
