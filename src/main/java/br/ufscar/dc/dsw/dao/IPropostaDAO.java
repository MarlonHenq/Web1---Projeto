package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.domain.Proposta;

@SuppressWarnings("unchecked")
public interface IPropostaDAO extends CrudRepository<Proposta, Long> {

	Proposta findById(long id);

	List<Proposta> findAll();

	Proposta save(Proposta proposta);

	void deleteById(Long id);

	List<Proposta> findByDesenvolvedor(Desenvolvedor desenvolvedor);

	List<Proposta> findByProjeto(Projeto projeto);
}
