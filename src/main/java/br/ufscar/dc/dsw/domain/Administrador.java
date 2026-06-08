package br.ufscar.dc.dsw.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Administrador")
public class Administrador extends Usuario {

	public Administrador() {
	}

	public Administrador(String email, String senha, String nome) {
		super(email, senha, nome);
	}

	@Override
	public String toString() {
		return "Administrador [nome=" + getNome() + ", email=" + getEmail() + "]";
	}
}
