package br.ufscar.dc.dsw.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.IDesenvolvedorDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.exception.BusinessException;
import br.ufscar.dc.dsw.exception.ResourceNotFoundException;

@Service
public class DesenvolvedorService {

	private final IDesenvolvedorDAO desenvolvedorDAO;
	private final IUsuarioDAO usuarioDAO;
	private final PasswordEncoder passwordEncoder;

	public DesenvolvedorService(IDesenvolvedorDAO desenvolvedorDAO, IUsuarioDAO usuarioDAO,
			PasswordEncoder passwordEncoder) {
		this.desenvolvedorDAO = desenvolvedorDAO;
		this.usuarioDAO = usuarioDAO;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public List<Desenvolvedor> findAll() {
		return (List<Desenvolvedor>) desenvolvedorDAO.findAll();
	}

	@Transactional(readOnly = true)
	public Desenvolvedor findById(Long id) {
		return desenvolvedorDAO.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Desenvolvedor não encontrado: " + id));
	}

	@Transactional
	public Desenvolvedor save(Desenvolvedor desenvolvedor, String senhaPlana) {
		validarUnicidade(desenvolvedor, null);
		if (senhaPlana == null || senhaPlana.isBlank()) {
			throw new BusinessException("Senha é obrigatória para novo desenvolvedor.");
		}
		desenvolvedor.setSenha(passwordEncoder.encode(senhaPlana));
		return desenvolvedorDAO.save(desenvolvedor);
	}

	@Transactional
	public Desenvolvedor update(Long id, Desenvolvedor dados, String senhaPlana) {
		Desenvolvedor existente = findById(id);
		validarUnicidade(dados, id);
		existente.setNome(dados.getNome());
		existente.setEmail(dados.getEmail());
		existente.setCpf(dados.getCpf());
		existente.setTelefone(dados.getTelefone());
		existente.setSexo(dados.getSexo());
		existente.setDataNascimento(dados.getDataNascimento());
		if (senhaPlana != null && !senhaPlana.isBlank()) {
			existente.setSenha(passwordEncoder.encode(senhaPlana));
		}
		return desenvolvedorDAO.save(existente);
	}

	@Transactional
	public void delete(Long id) {
		if (!desenvolvedorDAO.existsById(id)) {
			throw new ResourceNotFoundException("Desenvolvedor não encontrado: " + id);
		}
		desenvolvedorDAO.deleteById(id);
	}

	private void validarUnicidade(Desenvolvedor dev, Long idAtual) {
		usuarioDAO.findByEmail(dev.getEmail()).ifPresent(u -> {
			if (idAtual == null || !u.getId().equals(idAtual)) {
				throw new BusinessException("E-mail já cadastrado: " + dev.getEmail());
			}
		});
		desenvolvedorDAO.findByCpf(dev.getCpf()).ifPresent(d -> {
			if (idAtual == null || !d.getId().equals(idAtual)) {
				throw new BusinessException("CPF já cadastrado: " + dev.getCpf());
			}
		});
	}
}
