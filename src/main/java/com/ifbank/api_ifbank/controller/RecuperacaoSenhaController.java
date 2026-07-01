package com.ifbank.api_ifbank.controller;

import com.ifbank.api_ifbank.service.RecuperacaoSenhaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public RecuperacaoSenhaController(RecuperacaoSenhaService recuperacaoSenhaService) {
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    /**
     * POST /api/auth/esqueci-senha
     * Body: { "email": "usuario@email.com" }
     *
     * Sempre retorna 200 OK (não revela se o e-mail existe).
     */
    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> esqueciSenha(@RequestBody EsqueciSenhaRequest request) {
        try {
            recuperacaoSenhaService.solicitarRecuperacao(request.email());
        } catch (RuntimeException e) {
            // Só propaga se for erro de infra (ex: falha no envio de e-mail)
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok("Se este e-mail estiver cadastrado, você receberá o código em instantes.");
    }

    /**
     * POST /api/auth/resetar-senha
     * Body: { "token": "123456", "novaSenha": "minhasenha" }
     */
    @PostMapping("/resetar-senha")
    public ResponseEntity<String> resetarSenha(@RequestBody ResetarSenhaRequest request) {
        try {
            recuperacaoSenhaService.resetarSenha(request.token(), request.novaSenha());
            return ResponseEntity.ok("Senha redefinida com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Records para o body das requisições
    public record EsqueciSenhaRequest(String email) {}
    public record ResetarSenhaRequest(String token, String novaSenha) {}
}