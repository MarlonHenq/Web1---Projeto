package br.ufscar.dc.dsw.controller.api.dto;

import java.time.LocalDate;

import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.enums.Sexo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class DesenvolvedorRequest {

	@NotBlank
	@Email
	@Size(max = 60)
	private String email;

	private String senha;

	@NotBlank
	@Size(max = 14)
	private String cpf;

	@NotBlank
	@Size(max = 100)
	private String nome;

	@Size(max = 15)
	private String telefone;

	private Sexo sexo;

	@Past
	private LocalDate dataNascimento;

	public Desenvolvedor toEntity() {
		Desenvolvedor dev = new Desenvolvedor();
		dev.setEmail(email);
		dev.setNome(nome);
		dev.setCpf(cpf);
		dev.setTelefone(telefone);
		dev.setSexo(sexo);
		dev.setDataNascimento(dataNascimento);
		return dev;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
}
