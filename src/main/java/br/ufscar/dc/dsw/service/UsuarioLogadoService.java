package br.ufscar.dc.dsw.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.ufscar.dc.dsw.dao.IDesenvolvedorDAO;
import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.exception.BusinessException;

@Service
public class UsuarioLogadoService {

	private final IEmpresaDAO empresaDAO;
	private final IDesenvolvedorDAO desenvolvedorDAO;

	public UsuarioLogadoService(IEmpresaDAO empresaDAO, IDesenvolvedorDAO desenvolvedorDAO) {
		this.empresaDAO = empresaDAO;
		this.desenvolvedorDAO = desenvolvedorDAO;
	}

	public Empresa getEmpresaLogada() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return empresaDAO.findByEmail(email)
				.orElseThrow(() -> new BusinessException("Empresa não encontrada para usuário logado."));
	}

	public Desenvolvedor getDesenvolvedorLogado() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return desenvolvedorDAO.findByEmail(email)
				.orElseThrow(() -> new BusinessException("Desenvolvedor não encontrado para usuário logado."));
	}
}
