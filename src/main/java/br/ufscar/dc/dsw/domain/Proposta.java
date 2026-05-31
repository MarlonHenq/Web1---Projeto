package br.ufscar.dc.dsw.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.ufscar.dc.dsw.domain.enums.StatusProposta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Proposta", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "desenvolvedor_id", "projeto_id" })
})
public class Proposta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal valor;

	@Column(nullable = false)
	private LocalDate prazoEstimado;

	@Column(nullable = false, length = 2000)
	private String justificativa;

	@Column(nullable = false)
	private LocalDate dataProposta;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 15)
	private StatusProposta status;

	@ManyToOne
	@JoinColumn(name = "desenvolvedor_id", nullable = false)
	private Desenvolvedor desenvolvedor;

	@ManyToOne
	@JoinColumn(name = "projeto_id", nullable = false)
	private Projeto projeto;

	public Proposta() {
	}

	public Proposta(BigDecimal valor, LocalDate prazoEstimado, String justificativa,
			LocalDate dataProposta, StatusProposta status, Desenvolvedor desenvolvedor, Projeto projeto) {
		this.valor = valor;
		this.prazoEstimado = prazoEstimado;
		this.justificativa = justificativa;
		this.dataProposta = dataProposta;
		this.status = status;
		this.desenvolvedor = desenvolvedor;
		this.projeto = projeto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getPrazoEstimado() {
		return prazoEstimado;
	}

	public void setPrazoEstimado(LocalDate prazoEstimado) {
		this.prazoEstimado = prazoEstimado;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public LocalDate getDataProposta() {
		return dataProposta;
	}

	public void setDataProposta(LocalDate dataProposta) {
		this.dataProposta = dataProposta;
	}

	public StatusProposta getStatus() {
		return status;
	}

	public void setStatus(StatusProposta status) {
		this.status = status;
	}

	public Desenvolvedor getDesenvolvedor() {
		return desenvolvedor;
	}

	public void setDesenvolvedor(Desenvolvedor desenvolvedor) {
		this.desenvolvedor = desenvolvedor;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	@Override
	public String toString() {
		return "Proposta [valor=" + valor + ", status=" + status
				+ ", desenvolvedor=" + desenvolvedor.getNome() + "]";
	}
}
