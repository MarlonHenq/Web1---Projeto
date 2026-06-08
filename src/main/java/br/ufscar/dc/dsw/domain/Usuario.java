package br.ufscar.dc.dsw.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw.domain.enums.Papel;
import br.ufscar.dc.dsw.domain.enums.Sexo;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Email
	@Column(nullable = false, unique = true, length = 60)
	private String email;

	@Column(nullable = false, length = 60)
	private String senha;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String nome;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Papel papel;

	@Column(unique = true, length = 18)
	private String cnpj;

	@Column(length = 500)
	private String descricao;

	@Column(unique = true, length = 14)
	private String cpf;

	@Column(length = 15)
	private String telefone;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private Sexo sexo;

	private LocalDate dataNascimento;

	@OneToMany(mappedBy = "empresa")
	private List<Projeto> projetos = new ArrayList<>();

	@OneToMany(mappedBy = "desenvolvedor")
	private List<Proposta> propostas = new ArrayList<>();

	public Usuario() {
	}

	public static Usuario admin(String email, String senha, String nome) {
		Usuario u = new Usuario();
		u.email = email;
		u.senha = senha;
		u.nome = nome;
		u.papel = Papel.ADMIN;
		return u;
	}

	public static Usuario empresa(String email, String senha, String nome, String cnpj, String descricao) {
		Usuario u = new Usuario();
		u.email = email;
		u.senha = senha;
		u.nome = nome;
		u.papel = Papel.EMPRESA;
		u.cnpj = cnpj;
		u.descricao = descricao;
		return u;
	}

	public static Usuario desenvolvedor(String email, String senha, String nome, String cpf,
			String telefone, Sexo sexo, LocalDate dataNascimento) {
		Usuario u = new Usuario();
		u.email = email;
		u.senha = senha;
		u.nome = nome;
		u.papel = Papel.DESENVOLVEDOR;
		u.cpf = cpf;
		u.telefone = telefone;
		u.sexo = sexo;
		u.dataNascimento = dataNascimento;
		return u;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
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

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public List<Proposta> getPropostas() {
		return propostas;
	}

	public void setPropostas(List<Proposta> propostas) {
		this.propostas = propostas;
	}

	@Override
	public String toString() {
		return "Usuario [nome=" + nome + ", email=" + email + ", papel=" + papel + "]";
	}
}
