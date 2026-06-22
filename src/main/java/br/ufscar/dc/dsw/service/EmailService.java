package br.ufscar.dc.dsw.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.ufscar.dc.dsw.domain.enums.StatusProposta;

@Service
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	private static final String RESEND_API_URL = "https://api.resend.com/emails";

	private final RestClient restClient;

	@Value("${app.mail.resend-api-key:}")
	private String apiKey;

	@Value("${app.mail.from:onboarding@resend.dev}")
	private String from;

	public EmailService(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.build();
	}

	public boolean notificarDecisaoProposta(String destinatario, String devNome, String projetoTitulo,
			String empresaNome, StatusProposta status, String mensagem, String horarioReuniao,
			String linkVideoconferencia) {
		String assunto = buildAssunto(status, projetoTitulo);
		String corpo = buildCorpo(devNome, projetoTitulo, empresaNome, status, mensagem, horarioReuniao,
				linkVideoconferencia);

		if (!isEnabled()) {
			log.info("=== E-mail (modo demo — RESEND_API_KEY não configurada) ===");
			log.info("Para: {}", destinatario);
			log.info("Assunto: {}", assunto);
			log.info("Corpo:\n{}", corpo);
			log.info("========================================================");
			return true;
		}

		try {
			ResendEmailRequest body = new ResendEmailRequest(from, List.of(destinatario), assunto, corpo);
			restClient.post()
					.uri(RESEND_API_URL)
					.header("Authorization", "Bearer " + apiKey)
					.header("User-Agent", "FreelancersJPA/1.0")
					.contentType(MediaType.APPLICATION_JSON)
					.body(body)
					.retrieve()
					.toBodilessEntity();
			log.info("E-mail enviado via Resend para {} sobre proposta do projeto '{}'", destinatario,
					projetoTitulo);
			return true;
		} catch (Exception ex) {
			log.error("Falha ao enviar e-mail via Resend para {} sobre projeto '{}': {}", destinatario,
					projetoTitulo, ex.getMessage(), ex);
			return false;
		}
	}

	private boolean isEnabled() {
		return apiKey != null && !apiKey.isBlank();
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

	private record ResendEmailRequest(String from, List<String> to, String subject, String text) {
	}
}
