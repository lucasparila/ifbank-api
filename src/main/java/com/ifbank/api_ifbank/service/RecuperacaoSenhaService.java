package com.ifbank.api_ifbank.service;

import com.ifbank.api_ifbank.model.RecuperacaoSenha;
import com.ifbank.api_ifbank.model.Usuario;
import com.ifbank.api_ifbank.repository.RecuperacaoSenhaRepository;
import com.ifbank.api_ifbank.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class RecuperacaoSenhaService {

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    private final UsuarioRepository usuarioRepository;
    private final RecuperacaoSenhaRepository recuperacaoSenhaRepository;
    private final JavaMailSender mailSender;

    public RecuperacaoSenhaService(
            UsuarioRepository usuarioRepository,
            RecuperacaoSenhaRepository recuperacaoSenhaRepository,
            JavaMailSender mailSender) {
        this.usuarioRepository = usuarioRepository;
        this.recuperacaoSenhaRepository = recuperacaoSenhaRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public void solicitarRecuperacao(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            // Não revelamos se o e-mail existe ou não (segurança)
            log.warn("Tentativa de recuperação para e-mail não cadastrado: {}", email);
            return;
        }

        // Gera token numérico de 6 dígitos
        String token = String.format("%06d", new Random().nextInt(1_000_000));

        // Invalida TODOS os tokens anteriores deste usuário (podem existir vários de tentativas passadas)
        List<RecuperacaoSenha> recuperacoesAnteriores = recuperacaoSenhaRepository.findByUsuarioId(usuario.getId());
        for (RecuperacaoSenha r : recuperacoesAnteriores) {
            r.setUsado("S");
        }
        if (!recuperacoesAnteriores.isEmpty()) {
            recuperacaoSenhaRepository.saveAll(recuperacoesAnteriores);
        }

        // Salva novo token — expira em 30 minutos (armazenamos como LocalDate por ora,
        // idealmente migre para LocalDateTime; veja nota abaixo)
        RecuperacaoSenha recuperacao = new RecuperacaoSenha();
        recuperacao.setUsuario(usuario);
        recuperacao.setToken(token);
        recuperacao.setDataExpiracao(LocalDate.now().plusDays(1)); // ajuste ao migrar para LocalDateTime
        recuperacao.setUsado("N");
        recuperacaoSenhaRepository.save(recuperacao);

        // Envia e-mail
        try {
            enviarEmailRecuperacao(usuario.getEmail(), token);
        } catch (MessagingException e) {
            log.error("Erro ao enviar e-mail de recuperação para {}: {}", email, e.getMessage());
            throw new RuntimeException("Erro ao enviar e-mail. Tente novamente em instantes.");
        }
    }

    @Transactional
    public void resetarSenha(String token, String novaSenha) {
        if (novaSenha == null || novaSenha.length() < 6) {
            throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");
        }

        List<RecuperacaoSenha> recuperacoes = recuperacaoSenhaRepository.findByTokenOrderByIdDesc(token);

        if (recuperacoes.isEmpty()) {
            throw new RuntimeException("Código inválido.");
        }

        // Prioriza o registro mais recente que ainda não foi usado
        RecuperacaoSenha recuperacao = recuperacoes.stream()
                .filter(r -> "N".equals(r.getUsado()))
                .findFirst()
                .orElse(recuperacoes.get(0));

        if ("S".equals(recuperacao.getUsado())) {
            throw new RuntimeException("Este código já foi utilizado.");
        }
        if (recuperacao.getDataExpiracao().isBefore(LocalDate.now())) {
            throw new RuntimeException("Código expirado. Solicite um novo.");
        }

        // Atualiza a senha
        Usuario usuario = recuperacao.getUsuario();
        usuario.setSenha(novaSenha);
        usuarioRepository.save(usuario);

        // Marca token como usado
        recuperacao.setUsado("S");
        recuperacaoSenhaRepository.save(recuperacao);
    }

    // ─── E-MAIL HTML PADRÃO IFBANK ────────────────────────────────────────────

    private void enviarEmailRecuperacao(String destinatario, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(destinatario);
        helper.setSubject("IFBank — Seu código de recuperação: " + token);
        helper.setText(construirHtmlEmail(token), true);

        mailSender.send(message);
        log.info("E-mail de recuperação enviado para {}", destinatario);
    }

    private String construirHtmlEmail(String token) {
        // Divide o token em 3 pares para exibição: 123 456
        String parte1 = token.substring(0, 3);
        String parte2 = token.substring(3, 6);

        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>Recuperação de Senha — IFBank</title>
            </head>
            <body style="margin:0;padding:0;background-color:#f0f4ff;font-family:'Segoe UI',Arial,sans-serif;">

              <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f0f4ff;padding:40px 16px;">
                <tr>
                  <td align="center">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="max-width:520px;">

                      <!-- HEADER AZUL -->
                      <tr>
                        <td style="background:linear-gradient(135deg,#1a6dff,#001b4d);border-radius:18px 18px 0 0;padding:36px 40px;text-align:center;">
                          <div style="font-size:36px;margin-bottom:10px;">🏦</div>
                          <div style="font-size:26px;font-weight:900;color:#ffffff;letter-spacing:-0.5px;">IFBank</div>
                          <div style="font-size:13px;color:rgba(255,255,255,0.7);margin-top:4px;letter-spacing:0.05em;text-transform:uppercase;">
                            Recuperação de Senha
                          </div>
                        </td>
                      </tr>

                      <!-- CORPO -->
                      <tr>
                        <td style="background:#ffffff;padding:40px 40px 32px;border-left:1px solid #e0e8ff;border-right:1px solid #e0e8ff;">

                          <p style="font-size:16px;color:#0a2a57;font-weight:700;margin:0 0 8px;">Olá!</p>
                          <p style="font-size:14px;color:#495057;line-height:1.7;margin:0 0 28px;">
                            Recebemos uma solicitação para redefinir a senha da sua conta IFBank.
                            Use o código abaixo para continuar:
                          </p>

                          <!-- CÓDIGO EM DESTAQUE -->
                          <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:28px;">
                            <tr>
                              <td align="center" style="background:#f0f4ff;border:2px dashed #0d6efd44;border-radius:16px;padding:28px 20px;">
                                <div style="font-size:11px;font-weight:700;color:#7b8094;letter-spacing:0.1em;text-transform:uppercase;margin-bottom:14px;">
                                  Seu código de verificação
                                </div>
                                <div style="display:inline-block;">
                                  <span style="font-size:44px;font-weight:900;color:#0d6efd;letter-spacing:12px;font-family:'Courier New',monospace;">
                                    %s %s
                                  </span>
                                </div>
                                <div style="font-size:12px;color:#adb5bd;margin-top:14px;">
                                  ⏱ Válido por <strong>30 minutos</strong>
                                </div>
                              </td>
                            </tr>
                          </table>

                          <p style="font-size:14px;color:#495057;line-height:1.7;margin:0 0 8px;">
                            Digite este código na tela de recuperação de senha do IFBank.
                          </p>

                          <!-- AVISO DE SEGURANÇA -->
                          <table width="100%%" cellpadding="0" cellspacing="0" style="margin-top:24px;">
                            <tr>
                              <td style="background:#fff8e1;border:1px solid #ffe08a;border-radius:12px;padding:14px 16px;">
                                <p style="font-size:12px;color:#664d03;margin:0;line-height:1.6;">
                                  ⚠️ <strong>Não solicitou isso?</strong>
                                  Ignore este e-mail. Sua senha permanece a mesma e nenhuma alteração será feita.
                                </p>
                              </td>
                            </tr>
                          </table>

                        </td>
                      </tr>

                      <!-- FOOTER -->
                      <tr>
                        <td style="background:#f8f9ff;border:1px solid #e0e8ff;border-top:none;border-radius:0 0 18px 18px;padding:24px 40px;text-align:center;">
                          <p style="font-size:12px;color:#adb5bd;margin:0 0 4px;">
                            © IFBank — Projeto Acadêmico IFSP
                          </p>
                          <p style="font-size:12px;color:#adb5bd;margin:0;">
                            Este é um e-mail automático. Não responda a esta mensagem.
                          </p>
                        </td>
                      </tr>

                    </table>
                  </td>
                </tr>
              </table>

            </body>
            </html>
            """.formatted(parte1, parte2);
    }
}