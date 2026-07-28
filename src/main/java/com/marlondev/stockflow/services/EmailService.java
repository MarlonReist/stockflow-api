package com.marlondev.stockflow.services;

import com.marlondev.stockflow.services.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Value;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${stockflow.mail.from}")
    private String remetente;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void enviarConvite(String destino, String nome, String linkAtivacao) {
        String assunto = "Ativação de conta - StockFlow";

        String mensagem = montarTemplateEmail(
                "Convite de acesso",
                nome,
                "Você foi convidado para acessar o StockFlow.",
                "Para ativar sua conta e definir sua senha, clique no botão abaixo.",
                "Ativar minha conta",
                linkAtivacao,
                "Este convite expira em 2 dias."
        );

        enviarEmail(destino, assunto, mensagem);
    }

    public void enviarRecuperacaoSenha(String destino, String nome, String linkRedefinicao) {
        String assunto = "Recuperação de senha - StockFlow";

        String mensagem = montarTemplateEmail(
                "Recuperação de senha",
                nome,
                "Recebemos uma solicitação para redefinir sua senha no StockFlow.",
                "Para criar uma nova senha, clique no botão abaixo.",
                "Redefinir senha",
                linkRedefinicao,
                "Este link expira em 1 hora."
        );

        enviarEmail(destino, assunto, mensagem);
    }
    private void enviarEmail(String destino, String assunto, String mensagem) {
        try {
            MimeMessage email = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destino);
            helper.setSubject(assunto);
            helper.setText(mensagem, true);

            javaMailSender.send(email);
        } catch (MailException | MessagingException ex) {
            throw new DatabaseException("Erro ao enviar e-mail!");
        }
    }

    private String montarTemplateEmail(
            String titulo,
            String nome,
            String mensagemPrincipal,
            String mensagemAcao,
            String textoBotao,
            String link,
            String avisoExpiracao
    ) {
        String nomeSeguro = HtmlUtils.htmlEscape(nome);
        String tituloSeguro = HtmlUtils.htmlEscape(titulo);
        String mensagemPrincipalSegura = HtmlUtils.htmlEscape(mensagemPrincipal);
        String mensagemAcaoSegura = HtmlUtils.htmlEscape(mensagemAcao);
        String textoBotaoSeguro = HtmlUtils.htmlEscape(textoBotao);
        String linkSeguro = HtmlUtils.htmlEscape(link);
        String avisoExpiracaoSeguro = HtmlUtils.htmlEscape(avisoExpiracao);

        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>%s</title>
            </head>
            <body style="margin:0; padding:0; background:#f3f4f6; font-family:Arial, Helvetica, sans-serif; color:#111827;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f3f4f6; padding:32px 16px;">
                    <tr>
                        <td align="center">
                            <table width="100%%" cellpadding="0" cellspacing="0" style="max-width:560px; background:#ffffff; border-radius:8px; overflow:hidden; border:1px solid #e5e7eb;">
                                <tr>
                                    <td style="background:#0f172a; padding:24px 28px;">
                                        <div style="font-size:24px; font-weight:700; color:#ffffff;">StockFlow</div>
                                        <div style="font-size:13px; color:#cbd5e1; margin-top:4px;">Controle de estoque e ordens de serviço</div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding:28px;">
                                        <h1 style="margin:0 0 16px; font-size:22px; line-height:1.3; color:#111827;">%s</h1>
                                        <p style="margin:0 0 12px; font-size:15px; line-height:1.6;">Olá, <strong>%s</strong>.</p>
                                        <p style="margin:0 0 12px; font-size:15px; line-height:1.6;">%s</p>
                                        <p style="margin:0 0 24px; font-size:15px; line-height:1.6;">%s</p>

                                        <table cellpadding="0" cellspacing="0" style="margin:0 0 24px;">
                                            <tr>
                                                <td style="background:#2563eb; border-radius:6px;">
                                                    <a href="%s" style="display:inline-block; padding:13px 20px; color:#ffffff; text-decoration:none; font-size:15px; font-weight:700;">%s</a>
                                                </td>
                                            </tr>
                                        </table>

                                        <p style="margin:0 0 12px; font-size:14px; line-height:1.6; color:#4b5563;">%s</p>
                                        <p style="margin:0 0 8px; font-size:13px; line-height:1.6; color:#6b7280;">Se o botão não funcionar, copie e cole este link no navegador:</p>
                                        <p style="margin:0; font-size:13px; line-height:1.6; word-break:break-all;">
                                            <a href="%s" style="color:#2563eb;">%s</a>
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding:18px 28px; background:#f9fafb; border-top:1px solid #e5e7eb;">
                                        <p style="margin:0; font-size:12px; line-height:1.5; color:#6b7280;">Se você não solicitou ou não esperava este e-mail, pode ignorá-lo com segurança.</p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(
                tituloSeguro,
                tituloSeguro,
                nomeSeguro,
                mensagemPrincipalSegura,
                mensagemAcaoSegura,
                linkSeguro,
                textoBotaoSeguro,
                avisoExpiracaoSeguro,
                linkSeguro,
                linkSeguro
        );
    }
}