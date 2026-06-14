package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Projeto;

public interface IProjetoDAO extends CrudRepository<Projeto, Long> {

	List<Projeto> findByEmpresa(Empresa empresa);

	List<Projeto> findByStackTecnologicaContainingIgnoreCase(String nome);
}
