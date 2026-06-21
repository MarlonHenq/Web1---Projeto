package br.ufscar.dc.dsw.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.ufscar.dc.dsw.domain.Proposta;
import br.ufscar.dc.dsw.domain.enums.StatusProposta;

@Service
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	private final JavaMailSender mailSender;

	@Value("${app.mail.from:noreply@freelancers.local}")
	private String from;

	@Value("${app.mail.enabled:false}")
	private boolean enabled;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void notificarDecisaoProposta(Proposta proposta, String mensagem, String horarioReuniao,
			String linkVideoconferencia) {
		String destinatario = proposta.getDesenvolvedor().getEmail();
		String projeto = proposta.getProjeto().getTitulo();
		String assunto = buildAssunto(proposta.getStatus(), projeto);
		String corpo = buildCorpo(proposta, mensagem, horarioReuniao, linkVideoconferencia);

		if (enabled) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(destinatario);
			message.setSubject(assunto);
			message.setText(corpo);
			mailSender.send(message);
			log.info("E-mail enviado para {} sobre proposta do projeto '{}'", destinatario, projeto);
		} else {
			log.info("=== E-mail (modo demo — MAIL_ENABLED=false) ===");
			log.info("Para: {}", destinatario);
			log.info("Assunto: {}", assunto);
			log.info("Corpo:\n{}", corpo);
			log.info("=============================================");
		}
	}

	private String buildAssunto(StatusProposta status, String projeto) {
		if (status == StatusProposta.ACEITO) {
			return "Proposta aceita — " + projeto;
		}
		return "Proposta não aceita — " + projeto;
	}

	private String buildCorpo(Proposta proposta, String mensagem, String horarioReuniao,
			String linkVideoconferencia) {
		StringBuilder sb = new StringBuilder();
		sb.append("Olá, ").append(proposta.getDesenvolvedor().getNome()).append("!\n\n");
		sb.append("Projeto: ").append(proposta.getProjeto().getTitulo()).append("\n");
		sb.append("Empresa: ").append(proposta.getProjeto().getEmpresa().getNome()).append("\n\n");

		if (proposta.getStatus() == StatusProposta.ACEITO) {
			sb.append("Sua proposta foi ACEITA.\n\n");
			sb.append("Horário da reunião: ").append(horarioReuniao).append("\n");
			sb.append("Link da videoconferência: ").append(linkVideoconferencia).append("\n");
		} else {
			sb.append("Sua proposta NÃO foi aceita.\n\n");
			if (mensagem != null && !mensagem.isBlank()) {
				sb.append("Contraproposta / mensagem da empresa:\n").append(mensagem).append("\n");
			}
		}

		return sb.toString();
	}
}
