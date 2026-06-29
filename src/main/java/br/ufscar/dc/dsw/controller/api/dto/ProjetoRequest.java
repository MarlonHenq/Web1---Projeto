package br.ufscar.dc.dsw.controller.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.ufscar.dc.dsw.domain.Projeto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProjetoRequest {

	@NotBlank
	@Size(max = 100)
	private String titulo;

	@NotBlank
	@Size(max = 2000)
	private String descricao;

	@NotBlank
	@Size(max = 200)
	private String stackTecnologica;

	@NotNull
	@Positive
	private BigDecimal orcamentoEstimado;

	@NotNull
	@FutureOrPresent
	private LocalDate prazoEntrega;

	public Projeto toEntity() {
		Projeto projeto = new Projeto();
		projeto.setTitulo(titulo);
		projeto.setDescricao(descricao);
		projeto.setStackTecnologica(stackTecnologica);
		projeto.setOrcamentoEstimado(orcamentoEstimado);
		projeto.setPrazoEntrega(prazoEntrega);
		return projeto;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getStackTecnologica() {
		return stackTecnologica;
	}

	public void setStackTecnologica(String stackTecnologica) {
		this.stackTecnologica = stackTecnologica;
	}

	public BigDecimal getOrcamentoEstimado() {
		return orcamentoEstimado;
	}

	public void setOrcamentoEstimado(BigDecimal orcamentoEstimado) {
		this.orcamentoEstimado = orcamentoEstimado;
	}

	public LocalDate getPrazoEntrega() {
		return prazoEntrega;
	}

	public void setPrazoEntrega(LocalDate prazoEntrega) {
		this.prazoEntrega = prazoEntrega;
	}
}
