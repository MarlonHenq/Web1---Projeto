package br.ufscar.dc.dsw.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.exception.BusinessException;
import br.ufscar.dc.dsw.exception.ResourceNotFoundException;

@Service
public class EmpresaService {

	private final IEmpresaDAO empresaDAO;
	private final IUsuarioDAO usuarioDAO;
	private final PasswordEncoder passwordEncoder;

	public EmpresaService(IEmpresaDAO empresaDAO, IUsuarioDAO usuarioDAO, PasswordEncoder passwordEncoder) {
		this.empresaDAO = empresaDAO;
		this.usuarioDAO = usuarioDAO;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public List<Empresa> findAll() {
		return (List<Empresa>) empresaDAO.findAll();
	}

	@Transactional(readOnly = true)
	public Empresa findById(Long id) {
		return empresaDAO.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada: " + id));
	}

	@Transactional
	public Empresa save(Empresa empresa, String senhaPlana) {
		validarUnicidade(empresa, null);
		if (senhaPlana == null || senhaPlana.isBlank()) {
			throw new BusinessException("Senha é obrigatória para nova empresa.");
		}
		empresa.setSenha(passwordEncoder.encode(senhaPlana));
		return empresaDAO.save(empresa);
	}

	@Transactional
	public Empresa update(Long id, Empresa dados, String senhaPlana) {
		Empresa existente = findById(id);
		validarUnicidade(dados, id);
		existente.setNome(dados.getNome());
		existente.setEmail(dados.getEmail());
		existente.setCnpj(dados.getCnpj());
		existente.setDescricao(dados.getDescricao());
		if (senhaPlana != null && !senhaPlana.isBlank()) {
			existente.setSenha(passwordEncoder.encode(senhaPlana));
		}
		return empresaDAO.save(existente);
	}

	@Transactional
	public void delete(Long id) {
		if (!empresaDAO.existsById(id)) {
			throw new ResourceNotFoundException("Empresa não encontrada: " + id);
		}
		empresaDAO.deleteById(id);
	}

	private void validarUnicidade(Empresa empresa, Long idAtual) {
		usuarioDAO.findByEmail(empresa.getEmail()).ifPresent(u -> {
			if (idAtual == null || !u.getId().equals(idAtual)) {
				throw new BusinessException("E-mail já cadastrado: " + empresa.getEmail());
			}
		});
		empresaDAO.findByCnpj(empresa.getCnpj()).ifPresent(e -> {
			if (idAtual == null || !e.getId().equals(idAtual)) {
				throw new BusinessException("CNPJ já cadastrado: " + empresa.getCnpj());
			}
		});
	}
}
