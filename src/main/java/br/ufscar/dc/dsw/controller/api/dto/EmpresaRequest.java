package br.ufscar.dc.dsw.controller.api.dto;

import br.ufscar.dc.dsw.domain.Empresa;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmpresaRequest {

	@NotBlank
	@Email
	@Size(max = 60)
	private String email;

	private String senha;

	@NotBlank
	@Size(max = 18)
	private String cnpj;

	@NotBlank
	@Size(max = 100)
	private String nome;

	@Size(max = 500)
	private String descricao;

	public Empresa toEntity() {
		Empresa empresa = new Empresa();
		empresa.setEmail(email);
		empresa.setNome(nome);
		empresa.setCnpj(cnpj);
		empresa.setDescricao(descricao);
		return empresa;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
