package br.ufscar.dc.dsw.controller.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.ufscar.dc.dsw.domain.Projeto;

public class ProjetoResponse {

	private Long id;
	private String titulo;
	private String descricao;
	private String stackTecnologica;
	private BigDecimal orcamentoEstimado;
	private LocalDate prazoEntrega;
	private Long empresaId;
	private String empresaNome;

	public static ProjetoResponse from(Projeto projeto) {
		ProjetoResponse dto = new ProjetoResponse();
		dto.id = projeto.getId();
		dto.titulo = projeto.getTitulo();
		dto.descricao = projeto.getDescricao();
		dto.stackTecnologica = projeto.getStackTecnologica();
		dto.orcamentoEstimado = projeto.getOrcamentoEstimado();
		dto.prazoEntrega = projeto.getPrazoEntrega();
		if (projeto.getEmpresa() != null) {
			dto.empresaId = projeto.getEmpresa().getId();
			dto.empresaNome = projeto.getEmpresa().getNome();
		}
		return dto;
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getStackTecnologica() {
		return stackTecnologica;
	}

	public BigDecimal getOrcamentoEstimado() {
		return orcamentoEstimado;
	}

	public LocalDate getPrazoEntrega() {
		return prazoEntrega;
	}

	public Long getEmpresaId() {
		return empresaId;
	}

	public String getEmpresaNome() {
		return empresaNome;
	}
}
