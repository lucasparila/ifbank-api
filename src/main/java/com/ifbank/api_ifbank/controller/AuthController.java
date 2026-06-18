package com.ifbank.api_ifbank.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ifbank.api_ifbank.model.RecuperacaoSenha;
import com.ifbank.api_ifbank.model.Usuario;
import com.ifbank.api_ifbank.repository.RecuperacaoSenhaRepository;
import com.ifbank.api_ifbank.repository.UsuarioRepository;
import com.ifbank.api_ifbank.service.EmailService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RecuperacaoSenhaRepository recuperacaoSenhaRepository;
    private final EmailService emailService;

    public AuthController(UsuarioRepository usuarioRepository,
                          RecuperacaoSenhaRepository recuperacaoSenhaRepository,
                          EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.recuperacaoSenhaRepository = recuperacaoSenhaRepository;
        this.emailService = emailService;
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<?> esqueciSenha(@RequestBody java.util.Map<String, String> body) {
        try {
            String email = body.get("email");
            Usuario usuario = usuarioRepository.findByEmail(email);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("E-mail não encontrado.");
            }

            String token = UUID.randomUUID().toString();

            RecuperacaoSenha recuperacao = new RecuperacaoSenha();
            recuperacao.setUsuario(usuario);
            recuperacao.setToken(token);
            recuperacao.setDataExpiracao(LocalDate.now().plusDays(1));
            recuperacao.setUsado("N");
            recuperacaoSenhaRepository.save(recuperacao);

            emailService.enviarEmailResetSenha(email, token);

            return ResponseEntity.ok("E-mail de recuperação enviado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    @PostMapping("/resetar-senha")
    public ResponseEntity<?> resetarSenha(@RequestBody java.util.Map<String, String> body) {
        try {
            String token = body.get("token");
            String novaSenha = body.get("novaSenha");

            RecuperacaoSenha recuperacao = recuperacaoSenhaRepository.findByToken(token);

            if (recuperacao == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido.");
            }
            if (recuperacao.getUsado().equals("S")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token já utilizado.");
            }
            if (recuperacao.getDataExpiracao().isBefore(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expirado.");
            }

            Usuario usuario = recuperacao.getUsuario();
            usuario.setSenha(novaSenha);
            usuarioRepository.save(usuario);

            recuperacao.setUsado("S");
            recuperacaoSenhaRepository.save(recuperacao);

            return ResponseEntity.ok("Senha redefinida com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao redefinir senha: " + e.getMessage());
        }
    }
}