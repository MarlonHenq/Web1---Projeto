package br.ufscar.dc.dsw.config;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Usuario;

@Service
public class UsuarioUserDetailsService implements UserDetailsService {

	private final IUsuarioDAO usuarioDAO;

	public UsuarioUserDetailsService(IUsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioDAO.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

		String role = "ROLE_" + usuario.getPapel().name();
		return new User(usuario.getEmail(), usuario.getSenha(),
				List.of(new SimpleGrantedAuthority(role)));
	}
}
