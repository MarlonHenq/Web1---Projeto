package br.ufscar.dc.dsw.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Empresa;

public interface IEmpresaDAO extends CrudRepository<Empresa, Long> {

	Optional<Empresa> findByCnpj(String cnpj);

	Optional<Empresa> findByEmail(String email);
}
