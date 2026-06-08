package br.ufscar.dc.dsw.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Empresa")
public class Empresa extends Usuario {

	@Column(nullable = false, unique = true, length = 18)
	private String cnpj;

	@Column(nullable = false, length = 500)
	private String descricao;

	@OneToMany(mappedBy = "empresa")
	private List<Projeto> projetos = new ArrayList<>();

	public Empresa() {
	}

	public Empresa(String email, String senha, String nome, String cnpj, String descricao) {
		super(email, senha, nome);
		this.cnpj = cnpj;
		this.descricao = descricao;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	@Override
	public String toString() {
		return "Empresa [nome=" + getNome() + ", cnpj=" + cnpj + ", email=" + getEmail() + "]";
	}
}
