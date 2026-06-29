package br.ufscar.dc.dsw.controller.api.dto;

import java.time.LocalDate;

import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.enums.Sexo;

public class DesenvolvedorResponse {

	private Long id;
	private String email;
	private String nome;
	private String cpf;
	private String telefone;
	private Sexo sexo;
	private LocalDate dataNascimento;

	public static DesenvolvedorResponse from(Desenvolvedor dev) {
		DesenvolvedorResponse dto = new DesenvolvedorResponse();
		dto.id = dev.getId();
		dto.email = dev.getEmail();
		dto.nome = dev.getNome();
		dto.cpf = dev.getCpf();
		dto.telefone = dev.getTelefone();
		dto.sexo = dev.getSexo();
		dto.dataNascimento = dev.getDataNascimento();
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

	public String getCpf() {
		return cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
}
