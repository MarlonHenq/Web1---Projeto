package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Empresa;

@SuppressWarnings("unchecked")
public interface IEmpresaDAO extends CrudRepository<Empresa, Long> {

	Empresa findById(long id);

	List<Empresa> findAll();

	Empresa save(Empresa empresa);

	void deleteById(Long id);

	Empresa findByCnpj(String cnpj);
}
