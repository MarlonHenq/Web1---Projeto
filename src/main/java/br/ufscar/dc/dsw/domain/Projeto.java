package br.ufscar.dc.dsw.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Projeto")
public class Projeto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String titulo;

	@NotBlank
	@Size(max = 2000)
	@Column(nullable = false, length = 2000)
	private String descricao;

	@NotBlank
	@Size(max = 200)
	@Column(nullable = false, length = 200)
	private String stackTecnologica;

	@NotNull
	@Column(nullable = false, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal orcamentoEstimado;

	@NotNull
	@Column(nullable = false)
	private LocalDate prazoEntrega;

	@ManyToOne
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

	@OneToMany(mappedBy = "projeto")
	private List<Proposta> propostas = new ArrayList<>();

	@OneToMany(mappedBy = "projeto")
	private List<Anexo> anexos = new ArrayList<>();

	public Projeto() {
	}

	public Projeto(String titulo, String descricao, String stackTecnologica,
			BigDecimal orcamentoEstimado, LocalDate prazoEntrega, Empresa empresa) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.stackTecnologica = stackTecnologica;
		this.orcamentoEstimado = orcamentoEstimado;
		this.prazoEntrega = prazoEntrega;
		this.empresa = empresa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Proposta> getPropostas() {
		return propostas;
	}

	public void setPropostas(List<Proposta> propostas) {
		this.propostas = propostas;
	}

	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	@Override
	public String toString() {
		return "Projeto [titulo=" + titulo + ", stack=" + stackTecnologica
				+ ", orcamento=" + orcamentoEstimado + "]";
	}
}
