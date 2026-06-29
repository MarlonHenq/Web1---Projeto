package br.ufscar.dc.dsw.controller.api.dto;

import br.ufscar.dc.dsw.domain.Empresa;

public class EmpresaResponse {

	private Long id;
	private String email;
	private String nome;
	private String cnpj;
	private String descricao;

	public static EmpresaResponse from(Empresa empresa) {
		EmpresaResponse dto = new EmpresaResponse();
		dto.id = empresa.getId();
		dto.email = empresa.getEmail();
		dto.nome = empresa.getNome();
		dto.cnpj = empresa.getCnpj();
		dto.descricao = empresa.getDescricao();
		return dto;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getNome() {
		return nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public String getDescricao() {
		return descricao;
	}
}
