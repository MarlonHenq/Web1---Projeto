package br.ufscar.dc.dsw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.ufscar.dc.dsw.dao.IProjetoDAO;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.exception.ResourceNotFoundException;

@Service
public class ProjetoService {

	private final IProjetoDAO projetoDAO;
	private final AnexoService anexoService;

	public ProjetoService(IProjetoDAO projetoDAO, AnexoService anexoService) {
		this.projetoDAO = projetoDAO;
		this.anexoService = anexoService;
	}

	@Transactional(readOnly = true)
	public List<Projeto> findAll() {
		return (List<Projeto>) projetoDAO.findAll();
	}

	@Transactional(readOnly = true)
	public Projeto findById(Long id) {
		return projetoDAO.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado: " + id));
	}

	@Transactional(readOnly = true)
	public List<Projeto> findByEmpresa(Empresa empresa) {
		return projetoDAO.findByEmpresa(empresa);
	}

	@Transactional(readOnly = true)
	public List<Projeto> findByStack(String nome) {
		return projetoDAO.findByStackTecnologicaContainingIgnoreCase(nome);
	}

	@Transactional
	public Projeto save(Projeto projeto, List<MultipartFile> anexos) {
		Projeto salvo = projetoDAO.save(projeto);
		anexoService.salvarAnexos(salvo, anexos);
		return salvo;
	}

	@Transactional
	public Projeto update(Long id, Projeto dados, List<MultipartFile> anexos) {
		Projeto existente = findById(id);
		existente.setTitulo(dados.getTitulo());
		existente.setDescricao(dados.getDescricao());
		existente.setStackTecnologica(dados.getStackTecnologica());
		existente.setOrcamentoEstimado(dados.getOrcamentoEstimado());
		existente.setPrazoEntrega(dados.getPrazoEntrega());
		Projeto salvo = projetoDAO.save(existente);
		anexoService.salvarAnexos(salvo, anexos);
		return salvo;
	}

	@Transactional
	public void delete(Long id) {
		if (!projetoDAO.existsById(id)) {
			throw new ResourceNotFoundException("Projeto não encontrado: " + id);
		}
		projetoDAO.deleteById(id);
	}
}
