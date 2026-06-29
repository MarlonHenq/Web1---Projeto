package br.ufscar.dc.dsw.controller.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.ufscar.dc.dsw.domain.Proposta;
import br.ufscar.dc.dsw.domain.enums.StatusProposta;

public class PropostaResponse {

	private Long id;
	private BigDecimal valor;
	private LocalDate prazoEstimado;
	private String justificativa;
	private LocalDate dataProposta;
	private StatusProposta status;
	private Long desenvolvedorId;
	private String desenvolvedorNome;
	private Long projetoId;
	private String projetoTitulo;

	public static PropostaResponse from(Proposta proposta) {
		PropostaResponse dto = new PropostaResponse();
		dto.id = proposta.getId();
		dto.valor = proposta.getValor();
		dto.prazoEstimado = proposta.getPrazoEstimado();
		dto.justificativa = proposta.getJustificativa();
		dto.dataProposta = proposta.getDataProposta();
		dto.status = proposta.getStatus();
		if (proposta.getDesenvolvedor() != null) {
			dto.desenvolvedorId = proposta.getDesenvolvedor().getId();
			dto.desenvolvedorNome = proposta.getDesenvolvedor().getNome();
		}
		if (proposta.getProjeto() != null) {
			dto.projetoId = proposta.getProjeto().getId();
			dto.projetoTitulo = proposta.getProjeto().getTitulo();
		}
		return dto;
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public LocalDate getPrazoEstimado() {
		return prazoEstimado;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public LocalDate getDataProposta() {
		return dataProposta;
	}

	public StatusProposta getStatus() {
		return status;
	}

	public Long getDesenvolvedorId() {
		return desenvolvedorId;
	}

	public String getDesenvolvedorNome() {
		return desenvolvedorNome;
	}

	public Long getProjetoId() {
		return projetoId;
	}

	public String getProjetoTitulo() {
		return projetoTitulo;
	}
}
