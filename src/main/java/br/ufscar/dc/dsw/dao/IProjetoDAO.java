package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.domain.Usuario;

public interface IProjetoDAO extends CrudRepository<Projeto, Long> {

	List<Projeto> findByEmpresa(Usuario empresa);

	List<Projeto> findByStackTecnologicaContainingIgnoreCase(String nome);
}
