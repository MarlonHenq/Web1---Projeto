package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Anexo;
import br.ufscar.dc.dsw.domain.Projeto;

public interface IAnexoDAO extends CrudRepository<Anexo, Long> {

	List<Anexo> findByProjeto(Projeto projeto);
}
