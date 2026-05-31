package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.domain.Empresa;

@SuppressWarnings("unchecked")
public interface IProjetoDAO extends CrudRepository<Projeto, Long> {

	Projeto findById(long id);

	List<Projeto> findAll();

	Projeto save(Projeto projeto);

	void deleteById(Long id);

	List<Projeto> findByEmpresa(Empresa empresa);

	List<Projeto> findByStackTecnologicaContainingIgnoreCase(String nome);
}
