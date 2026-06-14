package br.ufscar.dc.dsw.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw.domain.enums.Papel;
import br.ufscar.dc.dsw.domain.enums.Sexo;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
@DiscriminatorValue("DESENVOLVEDOR")
public class Desenvolvedor extends Usuario {

	@NotBlank
	@Column(unique = true, length = 14)
	private String cpf;

	@Column(length = 15)
	private String telefone;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private Sexo sexo;

	private LocalDate dataNascimento;

	@OneToMany(mappedBy = "desenvolvedor")
	private List<Proposta> propostas = new ArrayList<>();

	public Desenvolvedor() {
		setPapel(Papel.DESENVOLVEDOR);
	}

	public Desenvolvedor(String email, String senha, String nome, String cpf,
			String telefone, Sexo sexo, LocalDate dataNascimento) {
		super(email, senha, nome);
		setPapel(Papel.DESENVOLVEDOR);
		this.cpf = cpf;
		this.telefone = telefone;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public List<Proposta> getPropostas() {
		return propostas;
	}

	public void setPropostas(List<Proposta> propostas) {
		this.propostas = propostas;
	}
}
