package br.ufscar.dc.dsw.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.IPropostaDAO;
import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.domain.Proposta;
import br.ufscar.dc.dsw.domain.enums.StatusProposta;
import br.ufscar.dc.dsw.exception.BusinessException;
import br.ufscar.dc.dsw.exception.ResourceNotFoundException;
import br.ufscar.dc.dsw.service.dto.PropostaAnaliseForm;

@Service
public class PropostaService {

	private final IPropostaDAO propostaDAO;
	private final ProjetoService projetoService;
	private final EmailService emailService;

	public PropostaService(IPropostaDAO propostaDAO, ProjetoService projetoService,
			EmailService emailService) {
		this.propostaDAO = propostaDAO;
		this.projetoService = projetoService;
		this.emailService = emailService;
	}

	@Transactional(readOnly = true)
	public List<Proposta> findByDesenvolvedor(Desenvolvedor desenvolvedor) {
		return propostaDAO.findByDesenvolvedor(desenvolvedor);
	}

	@Transactional(readOnly = true)
	public List<Proposta> findByProjeto(Projeto projeto) {
		return propostaDAO.findByProjeto(projeto);
	}

	@Transactional(readOnly = true)
	public Proposta findById(Long id) {
		return propostaDAO.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proposta não encontrada: " + id));
	}

	@Transactional(readOnly = true)
	public boolean jaProposto(Desenvolvedor desenvolvedor, Long projetoId) {
		Projeto projeto = projetoService.findById(projetoId);
		return propostaDAO.findByDesenvolvedorAndProjeto(desenvolvedor, projeto).isPresent();
	}

	@Transactional
	public Proposta criar(Proposta proposta, Desenvolvedor desenvolvedor, Long projetoId) {
		Projeto projeto = projetoService.findById(projetoId);

		if (propostaDAO.findByDesenvolvedorAndProjeto(desenvolvedor, projeto).isPresent()) {
			throw new BusinessException("Você já possui uma proposta para este projeto.");
		}

		proposta.setDesenvolvedor(desenvolvedor);
		proposta.setProjeto(projeto);
		proposta.setStatus(StatusProposta.ABERTO);
		proposta.setDataProposta(LocalDate.now());

		return propostaDAO.save(proposta);
	}

	@Transactional
	public Proposta analisar(Long propostaId, Empresa empresa, PropostaAnaliseForm form) {
		Proposta proposta = findById(propostaId);
		Projeto projeto = proposta.getProjeto();

		if (!projeto.getEmpresa().getId().equals(empresa.getId())) {
			throw new BusinessException("Proposta não pertence a um projeto desta empresa.");
		}

		if (proposta.getStatus() != StatusProposta.ABERTO) {
			throw new BusinessException("Esta proposta já foi analisada.");
		}

		StatusProposta novoStatus = form.getStatus();
		if (novoStatus != StatusProposta.ACEITO && novoStatus != StatusProposta.NAO_ACEITO) {
			throw new BusinessException("Status de análise inválido.");
		}

		if (novoStatus == StatusProposta.ACEITO) {
			if (form.getHorarioReuniao() == null || form.getHorarioReuniao().isBlank()) {
				throw new BusinessException("Informe o horário da reunião.");
			}
			if (form.getLinkVideoconferencia() == null || form.getLinkVideoconferencia().isBlank()) {
				throw new BusinessException("Informe o link da videoconferência.");
			}
		}

		proposta.setStatus(novoStatus);
		propostaDAO.save(proposta);

		emailService.notificarDecisaoProposta(proposta, form.getMensagem(),
				form.getHorarioReuniao(), form.getLinkVideoconferencia());

		return proposta;
	}
}
