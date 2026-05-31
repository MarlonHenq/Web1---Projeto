package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Anexo;
import br.ufscar.dc.dsw.domain.Projeto;

@SuppressWarnings("unchecked")
public interface IAnexoDAO extends CrudRepository<Anexo, Long> {

	Anexo findById(long id);

	List<Anexo> findAll();

	Anexo save(Anexo anexo);

	void deleteById(Long id);

	List<Anexo> findByProjeto(Projeto projeto);
}
