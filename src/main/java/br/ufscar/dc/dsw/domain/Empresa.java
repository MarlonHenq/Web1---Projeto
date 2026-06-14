package br.ufscar.dc.dsw.domain;

import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw.domain.enums.Papel;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@DiscriminatorValue("EMPRESA")
public class Empresa extends Usuario {

	@NotBlank
	@Column(unique = true, length = 18)
	private String cnpj;

	@Size(max = 500)
	@Column(length = 500)
	private String descricao;

	@OneToMany(mappedBy = "empresa")
	private List<Projeto> projetos = new ArrayList<>();

	public Empresa() {
		setPapel(Papel.EMPRESA);
	}

	public Empresa(String email, String senha, String nome, String cnpj, String descricao) {
		super(email, senha, nome);
		setPapel(Papel.EMPRESA);
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
}
