package com.ifbank.api_ifbank.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailResetSenha(String destinatario, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("IFBank - Redefinição de Senha");
        message.setText(
            "Olá!\n\n" +
            "Recebemos uma solicitação para redefinir a senha da sua conta IFBank.\n\n" +
            "Clique no link abaixo para redefinir sua senha:\n" +
            "http://localhost:4200/resetar-senha?token=" + token + "\n\n" +
            "Este link expira em 30 minutos.\n\n" +
            "Se você não solicitou a redefinição, ignore este email.\n\n" +
            "Atenciosamente,\nEquipe IFBank"
        );
        mailSender.send(message);
    }
}