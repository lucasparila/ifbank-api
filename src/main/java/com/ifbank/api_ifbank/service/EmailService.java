package com.ifbank.api_ifbank.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ifbank.api_ifbank.model.Cliente;
import com.ifbank.api_ifbank.model.Conta;
import com.ifbank.api_ifbank.service.interfaces.IEmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService implements IEmailService{

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
    
    @Async
    public void enviarEmailContaAprovada(Cliente cliente, Conta conta, String emailDestinatario) {
    	try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailDestinatario);
            helper.setSubject("IFBank — Conta Aprovada");
            helper.setText(construirHtmlEmailContaAprovada(cliente.getNome(),conta.getNumeroConta()), true);

            mailSender.send(message);

            log.info("E-mail de aprovação de conta enviado para {}", emailDestinatario);

        } catch (MessagingException e) {
            log.error("Erro ao montar o e-mail de aprovação de conta para {}", emailDestinatario, e);
        } catch (MailException e) {
            log.error("Erro ao enviar o e-mail de aprovação de conta para {}", emailDestinatario, e);
        }
    }
    

    @Async
    public void enviarEmailContaReprovada(Cliente cliente,String emailDestinatario) {
    	try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailDestinatario);
            helper.setSubject("IFBank — Conta Reprovada");
            helper.setText(contruirHtmlEmailContaReprovada(cliente.getNome()), true);

            mailSender.send(message);

            log.info("E-mail de reprovação de conta enviado para {}", emailDestinatario);

        } catch (MessagingException e) {
            log.error("Erro ao montar o e-mail de reprovação de conta para {}", emailDestinatario, e);
        } catch (MailException e) {
            log.error("Erro ao enviar o e-mail de reprovação de conta para {}", emailDestinatario, e);
        }
    }
    @Async
    public void enviarEmailCadastro(Cliente cliente, String emailDestinatario) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailDestinatario);
            helper.setSubject("IFBank — Pedido de abertura de conta em análise");
            helper.setText(construirHtmlEmailNovoCadastro(cliente.getNome()), true);

            mailSender.send(message);

            log.info("E-mail de abertura de conta enviado para {}", emailDestinatario);

        } catch (MessagingException e) {
            log.error("Erro ao montar o e-mail de abertura de conta para {}", emailDestinatario, e);
        } catch (MailException e) {
            log.error("Erro ao enviar o e-mail de abertura de conta para {}", emailDestinatario, e);
        }
    }
    private String construirHtmlEmailNovoCadastro(String nome) {
    	return """
                <!DOCTYPE html>
				<html lang="pt-BR">
				<head>
				  <meta charset="UTF-8">
				  <meta name="viewport" content="width=device-width, initial-scale=1.0">
				  <title>Bem-vindo ao IFBank</title>
				</head>
				
				<body style="margin:0;padding:0;background-color:#f0f4ff;font-family:'Segoe UI',Arial,sans-serif;">
				
				<table width="100%%" cellpadding="0" cellspacing="0" style="background:#f0f4ff;padding:40px 16px;">
				<tr>
				<td align="center">
				
				<table width="100%%" cellpadding="0" cellspacing="0" style="max-width:520px;">
				
				    <!-- HEADER -->
				    <tr>
				        <td style="background:linear-gradient(135deg,#1a6dff,#001b4d);border-radius:18px 18px 0 0;padding:36px 40px;text-align:center;">
				            <div style="font-size:36px;margin-bottom:10px;">🏦</div>
				
				            <div style="font-size:26px;font-weight:900;color:#ffffff;letter-spacing:-0.5px;">
				                IFBank
				            </div>
				
				            <div style="font-size:13px;color:rgba(255,255,255,0.7);margin-top:4px;letter-spacing:0.05em;text-transform:uppercase;">
				                Cadastro Recebido
				            </div>
				        </td>
				    </tr>
				
				    <!-- CORPO -->
				    <tr>
				        <td style="background:#ffffff;padding:40px;border-left:1px solid #e0e8ff;border-right:1px solid #e0e8ff;">
				
				            <p style="font-size:20px;font-weight:700;color:#0a2a57;margin:0 0 18px;">
				                Olá, %s! 👋
				            </p>
				
				            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 24px;">
				                Seja bem-vindo ao <strong>IFBank </strong>!
				            </p>
				
				            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 28px;">
				                Recebemos sua solicitação de abertura de conta e ela está sendo analisada pela nossa equipe. Esse processo é necessário para validar as informações fornecidas durante o cadastro.
				            </p>
				
				            <!-- CARD -->
				            <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:28px;">
				                <tr>
				                    <td style="background:#f0f7ff;border:1px solid #cfe2ff;border-radius:16px;padding:26px;text-align:center;">
				
				                        <div style="font-size:42px;margin-bottom:12px;">
				                            ⏳
				                        </div>
				
				                        <div style="font-size:18px;font-weight:700;color:#0d6efd;margin-bottom:10px;">
				                            Conta em análise
				                        </div>
				
				                        <div style="font-size:14px;color:#495057;line-height:1.7;">
				                            Nossa equipe está verificando as informações enviadas durante o seu cadastro.
				                        </div>
				
				                    </td>
				                </tr>
				            </table>
				
				            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 18px;">
				                Assim que a análise for concluída, enviaremos um novo e-mail informando o resultado da solicitação.
				            </p>
				
				            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0;">
				                Enquanto isso, não é necessário realizar nenhuma ação. Basta aguardar nosso retorno.
				            </p>
				
				            <!-- AVISO -->
				            <table width="100%%" cellpadding="0" cellspacing="0" style="margin-top:28px;">
				                <tr>
				                    <td style="background:#fff8e1;border:1px solid #ffe08a;border-radius:12px;padding:14px 16px;">
				
				                        <p style="font-size:12px;color:#664d03;margin:0;line-height:1.6;">
				                            📧 Você receberá uma nova mensagem informando se sua conta foi aprovada ou se será necessária alguma informação complementar.
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
                """.formatted(nome);
    }
    
    private String construirHtmlEmailContaAprovada(String nomeTitularConta, String numeroConta) {
    	
    	return """
              <!DOCTYPE html>
				<html lang="pt-BR">
				<head>
				  <meta charset="UTF-8">
				  <meta name="viewport" content="width=device-width, initial-scale=1.0">
				  <title>Conta Aprovada — IFBank</title>
				</head>
				
				<body style="margin:0;padding:0;background-color:#f0f4ff;font-family:'Segoe UI',Arial,sans-serif;">
				
				<table width="100%%" cellpadding="0" cellspacing="0" style="background:#f0f4ff;padding:40px 16px;">
				<tr>
				<td align="center">
				
				<table width="100%%" cellpadding="0" cellspacing="0" style="max-width:520px;">
				
				    <!-- HEADER -->
				    <tr>
				        <td style="background:linear-gradient(135deg,#1a6dff,#001b4d);border-radius:18px 18px 0 0;padding:36px 40px;text-align:center;">
				
				            <div style="font-size:36px;margin-bottom:10px;">
				                🏦
				            </div>
				
				            <div style="font-size:26px;font-weight:900;color:#ffffff;letter-spacing:-0.5px;">
				                IFBank
				            </div>
				
				            <div style="font-size:13px;color:rgba(255,255,255,0.7);margin-top:4px;letter-spacing:0.05em;text-transform:uppercase;">
				                Conta Aprovada
				            </div>
				
				        </td>
				    </tr>
				
				    <!-- CORPO -->
				    <tr>
				        <td style="background:#ffffff;padding:40px;border-left:1px solid #e0e8ff;border-right:1px solid #e0e8ff;">
				
				            <p style="font-size:20px;font-weight:700;color:#0a2a57;margin:0 0 18px;">
				                Olá, %s! 🎉
				            </p>
				
				            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 16px;">
				                Temos uma ótima notícia!
				            </p>
				
				            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 28px;">
				                Sua solicitação de abertura de conta foi analisada e <strong>aprovada</strong>.
				                Agora você faz parte do <strong>IFBank</strong> e já pode utilizar todos os nossos serviços.
				            </p>
				
				            <!-- CARD DE SUCESSO -->
				            <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:28px;">
				                <tr>
				                    <td style="background:#f0fff5;border:1px solid #b7ebc6;border-radius:16px;padding:26px;text-align:center;">
				
				                        <div style="font-size:42px;margin-bottom:12px;">
				                            ✅
				                        </div>
				
				                        <div style="font-size:20px;font-weight:700;color:#198754;margin-bottom:10px;">
				                            Conta aprovada
				                        </div>
				
				                        <div style="font-size:14px;color:#495057;line-height:1.7;">
				                            Sua conta foi criada com sucesso e já está pronta para utilização.
				                        </div>
				
				                    </td>
				                </tr>
				            </table>
				
				            <!-- NÚMERO DA CONTA -->
				            <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:28px;">
				                <tr>
				                    <td style="background:#f8f9ff;border:1px solid #dbe4ff;border-radius:16px;padding:24px;text-align:center;">
				
				                        <div style="font-size:12px;font-weight:700;color:#7b8094;letter-spacing:0.08em;text-transform:uppercase;margin-bottom:10px;">
				                            Número da sua conta
				                        </div>
				
				                        <div style="font-size:34px;font-weight:900;color:#0d6efd;font-family:'Courier New',monospace;">
				                            %s
				                        </div>
				
				                    </td>
				                </tr>
				            </table>
				
				            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 18px;">
				                A partir deste momento você já pode acessar sua conta utilizando o e-mail e a senha cadastrados durante a abertura da conta.
				            </p>
				
				            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 24px;">
				                Estamos felizes em tê-lo conosco e esperamos oferecer a melhor experiência possível em nossos serviços bancários.
				            </p>
				
				            <!-- PRÓXIMOS PASSOS -->
				            <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:28px;">
				                <tr>
				                    <td style="background:#eef5ff;border:1px solid #cfe2ff;border-radius:12px;padding:18px;">
				
				                        <div style="font-size:13px;font-weight:700;color:#0d6efd;margin-bottom:10px;">
				                            Próximos passos
				                        </div>
				
				                        <ul style="padding-left:18px;margin:0;font-size:14px;color:#495057;line-height:1.8;">
				                            <li>Acesse sua conta utilizando seu e-mail e senha.</li>
				                            <li>Consulte seu saldo e seus dados bancários.</li>
				                            <li>Comece a utilizar os serviços disponíveis no IFBank.</li>
				                        </ul>
				
				                    </td>
				                </tr>
				            </table>
				
				            <!-- AVISO -->
				            <table width="100%%" cellpadding="0" cellspacing="0">
				                <tr>
				                    <td style="background:#e8f5e9;border:1px solid #c8e6c9;border-radius:12px;padding:14px 16px;">
				
				                        <p style="font-size:12px;color:#2e7d32;margin:0;line-height:1.6;">
				                            🎉 <strong>Sua conta já está ativa!</strong>
				                            Caso tenha qualquer dúvida, nossa equipe estará pronta para ajudá-lo.
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
                """.formatted(nomeTitularConta,numeroConta);
    	
    }
    
    private String contruirHtmlEmailContaReprovada(String nomeTitularContaReprovada) {
    	return """
          <!DOCTYPE html>
			<html lang="pt-BR">
			<head>
			<meta charset="UTF-8">
			<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<title>Atualização da análise da conta — IFBank</title>
			</head>
			
			<body style="margin:0;padding:0;background-color:#f0f4ff;font-family:'Segoe UI',Arial,sans-serif;">
			
			<table width="100%%" cellpadding="0" cellspacing="0" style="background:#f0f4ff;padding:40px 16px;">
			<tr>
			<td align="center">
			
			<table width="100%%" cellpadding="0" cellspacing="0" style="max-width:520px;">
			
			    <!-- HEADER -->
			    <tr>
			        <td style="background:linear-gradient(135deg,#1a6dff,#001b4d);border-radius:18px 18px 0 0;padding:36px 40px;text-align:center;">
			
			            <div style="font-size:36px;margin-bottom:10px;">
			                🏦
			            </div>
			
			            <div style="font-size:26px;font-weight:900;color:#ffffff;letter-spacing:-0.5px;">
			                IFBank
			            </div>
			
			            <div style="font-size:13px;color:rgba(255,255,255,0.7);margin-top:4px;letter-spacing:0.05em;text-transform:uppercase;">
			                Resultado da análise
			            </div>
			
			        </td>
			    </tr>
			
			    <!-- CORPO -->
			    <tr>
			        <td style="background:#ffffff;padding:40px;border-left:1px solid #e0e8ff;border-right:1px solid #e0e8ff;">
			
			            <p style="font-size:20px;font-weight:700;color:#0a2a57;margin:0 0 18px;">
			                Olá, %s.
			            </p>
			
			            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 24px;">
			                Agradecemos por escolher o <strong>IFBank</strong>.
			            </p>
			
			            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 28px;">
			                Concluímos a análise da sua solicitação de abertura de conta.
			                Neste momento, infelizmente, <strong>não foi possível aprovar o seu cadastro</strong>.
			            </p>
			
			            <!-- CARD -->
			            <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:28px;">
			                <tr>
			                    <td style="background:#fff8e6;border:1px solid #ffd591;border-radius:16px;padding:26px;text-align:center;">
			
			                        <div style="font-size:42px;margin-bottom:12px;">
			                            ⚠️
			                        </div>
			
			                        <div style="font-size:20px;font-weight:700;color:#d97706;margin-bottom:10px;">
			                            Conta não aprovada
			                        </div>
			
			                        <div style="font-size:14px;color:#495057;line-height:1.7;">
			                            Após a análise das informações fornecidas, sua solicitação não pôde ser aprovada nesta etapa.
			                        </div>
			
			                    </td>
			                </tr>
			            </table>
			
			            <p style="font-size:14px;color:#495057;line-height:1.8;margin:0 0 20px;">
			                Essa decisão faz parte dos procedimentos de validação adotados pelo IFBank para garantir a segurança dos clientes e a conformidade com nossos critérios internos.
			            </p>
			
			            <!-- IMPORTANTE -->
			            <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom:28px;">
			                <tr>
			                    <td style="background:#f8f9ff;border:1px solid #dbe4ff;border-radius:12px;padding:18px;">
			
			                        <div style="font-size:13px;font-weight:700;color:#0d6efd;margin-bottom:10px;">
			                            O que fazer agora?
			                        </div>
			
			                        <p style="font-size:14px;color:#495057;line-height:1.7;margin:0;">
			                            Caso considere necessário, você poderá realizar um novo cadastro futuramente com informações atualizadas.
			                        </p>
			
			                    </td>
			                </tr>
			            </table>
			
			            <!-- AVISO -->
			            <table width="100%%" cellpadding="0" cellspacing="0">
			                <tr>
			                    <td style="background:#fff3cd;border:1px solid #ffe69c;border-radius:12px;padding:14px 16px;">
			
			                        <p style="font-size:12px;color:#856404;margin:0;line-height:1.6;">
			                            ℹ️ Por motivos de segurança e política de privacidade, o IFBank não informa por e-mail os critérios específicos utilizados durante a análise cadastral.
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
                  """.formatted(nomeTitularContaReprovada);
    }
}