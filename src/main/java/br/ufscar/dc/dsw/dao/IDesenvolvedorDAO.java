package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Desenvolvedor;

@SuppressWarnings("unchecked")
public interface IDesenvolvedorDAO extends CrudRepository<Desenvolvedor, Long> {

	Desenvolvedor findById(long id);

	List<Desenvolvedor> findAll();

	Desenvolvedor save(Desenvolvedor desenvolvedor);

	void deleteById(Long id);

	Desenvolvedor findByCpf(String cpf);
}
