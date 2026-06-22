package br.ufscar.dc.dsw.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

	public boolean notificarDecisaoProposta(String destinatario, String devNome, String projetoTitulo,
			String empresaNome, StatusProposta status, String mensagem, String horarioReuniao,
			String linkVideoconferencia) {
		String assunto = buildAssunto(status, projetoTitulo);
		String corpo = buildCorpo(devNome, projetoTitulo, empresaNome, status, mensagem, horarioReuniao,
				linkVideoconferencia);

		if (!enabled) {
			log.info("=== E-mail (modo demo — MAIL_ENABLED=false) ===");
			log.info("Para: {}", destinatario);
			log.info("Assunto: {}", assunto);
			log.info("Corpo:\n{}", corpo);
			log.info("=============================================");
			return true;
		}

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(destinatario);
			message.setSubject(assunto);
			message.setText(corpo);
			mailSender.send(message);
			log.info("E-mail enviado para {} sobre proposta do projeto '{}'", destinatario, projetoTitulo);
			return true;
		} catch (Exception ex) {
			log.error("Falha ao enviar e-mail para {} sobre projeto '{}': {}", destinatario, projetoTitulo,
					ex.getMessage(), ex);
			return false;
		}
	}

	private String buildAssunto(StatusProposta status, String projeto) {
		if (status == StatusProposta.ACEITO) {
			return "Proposta aceita — " + projeto;
		}
		return "Proposta não aceita — " + projeto;
	}

	private String buildCorpo(String devNome, String projetoTitulo, String empresaNome, StatusProposta status,
			String mensagem, String horarioReuniao, String linkVideoconferencia) {
		StringBuilder sb = new StringBuilder();
		sb.append("Olá, ").append(devNome).append("!\n\n");
		sb.append("Projeto: ").append(projetoTitulo).append("\n");
		sb.append("Empresa: ").append(empresaNome).append("\n\n");

		if (status == StatusProposta.ACEITO) {
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
