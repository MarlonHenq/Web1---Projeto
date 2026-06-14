package br.ufscar.dc.dsw.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Desenvolvedor;

public interface IDesenvolvedorDAO extends CrudRepository<Desenvolvedor, Long> {

	Optional<Desenvolvedor> findByCpf(String cpf);

	Optional<Desenvolvedor> findByEmail(String email);
}
