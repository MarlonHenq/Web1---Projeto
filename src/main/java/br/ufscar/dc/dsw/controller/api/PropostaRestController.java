package br.ufscar.dc.dsw.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufscar.dc.dsw.controller.api.dto.PropostaResponse;
import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.service.DesenvolvedorService;
import br.ufscar.dc.dsw.service.ProjetoService;
import br.ufscar.dc.dsw.service.PropostaService;

@RestController
@RequestMapping("/api/propostas")
public class PropostaRestController {

	private final PropostaService propostaService;
	private final ProjetoService projetoService;
	private final DesenvolvedorService desenvolvedorService;

	public PropostaRestController(PropostaService propostaService, ProjetoService projetoService,
			DesenvolvedorService desenvolvedorService) {
		this.propostaService = propostaService;
		this.projetoService = projetoService;
		this.desenvolvedorService = desenvolvedorService;
	}

	@GetMapping("/projetos/{id}")
	public List<PropostaResponse> listarPorProjeto(@PathVariable Long id) {
		Projeto projeto = projetoService.findById(id);
		return propostaService.findByProjeto(projeto).stream()
				.map(PropostaResponse::from)
				.toList();
	}

	@GetMapping("/desenvolvedores/{id}")
	public List<PropostaResponse> listarPorDesenvolvedor(@PathVariable Long id) {
		Desenvolvedor desenvolvedor = desenvolvedorService.findById(id);
		return propostaService.findByDesenvolvedor(desenvolvedor).stream()
				.map(PropostaResponse::from)
				.toList();
	}
}
