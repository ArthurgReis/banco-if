package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.model.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${app.frontend.url}")
    private String frontendBaseUrl;

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void enviarEmailSimples(String para, String assunto, String texto) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom("nao-responda@bancofeira.com");
            mensagem.setTo(para);
            mensagem.setSubject(assunto);
            mensagem.setText(texto);

            javaMailSender.send(mensagem);
            System.out.println("E-mail de teste enviado para: " + para);
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    public void enviarEmailDeConfirmacao(Usuario usuario, String token) {
        String linkDeConfirmacao = frontendBaseUrl + "/confirmar-conta?token=" + token;

        String assunto = "Confirme seu E-mail - Banco Feira";
        String texto = "Olá, " + usuario.getNome() + "!\n\n"
                + "Obrigado por se cadastrar. Por favor, clique no link abaixo para ativar sua conta:\n\n"
                + linkDeConfirmacao + "\n\n"
                + "O link expira em 15 minutos.";

        enviarEmailSimples(usuario.getEmail(), assunto, texto);
    }
}