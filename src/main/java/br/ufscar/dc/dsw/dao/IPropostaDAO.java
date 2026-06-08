package br.ufscar.dc.dsw.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.domain.Proposta;
import br.ufscar.dc.dsw.domain.Usuario;

public interface IPropostaDAO extends CrudRepository<Proposta, Long> {

	List<Proposta> findByDesenvolvedor(Usuario desenvolvedor);

	List<Proposta> findByProjeto(Projeto projeto);

	Optional<Proposta> findByDesenvolvedorAndProjeto(Usuario desenvolvedor, Projeto projeto);
}
