package com.ifbank.api_ifbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifbank.api_ifbank.model.DTO.login.LoginRequestDTO;
import com.ifbank.api_ifbank.model.DTO.login.LoginResponseDTO;
import com.ifbank.api_ifbank.service.UsuarioService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> loginSimples(@RequestBody LoginRequestDTO dadosLogin) {
        try {

            LoginResponseDTO resposta = usuarioService.autenticar(dadosLogin);
            return ResponseEntity.ok(resposta);

        } catch (RuntimeException e) {
            String mensagem = e.getMessage();

            if (mensagem.contains("Senha")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensagem);
            }

            if (mensagem.contains("pendente") || mensagem.contains("inativa") || mensagem.contains("rejeitada")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mensagem);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
        }
    }
}