package br.ufscar.dc.dsw.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.domain.Proposta;

public interface IPropostaDAO extends CrudRepository<Proposta, Long> {

	List<Proposta> findByDesenvolvedor(Desenvolvedor desenvolvedor);

	List<Proposta> findByProjeto(Projeto projeto);

	Optional<Proposta> findByDesenvolvedorAndProjeto(Desenvolvedor desenvolvedor, Projeto projeto);
}
