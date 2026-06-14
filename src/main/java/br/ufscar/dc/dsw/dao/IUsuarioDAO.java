package br.ufscar.dc.dsw.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.enums.Papel;

public interface IUsuarioDAO extends CrudRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String email);

	List<Usuario> findByPapel(Papel papel);
}
