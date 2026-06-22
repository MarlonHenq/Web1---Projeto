package br.ufscar.dc.dsw.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.service.PropostaService;
import br.ufscar.dc.dsw.service.ProjetoService;
import br.ufscar.dc.dsw.service.UsuarioLogadoService;

@Controller
public class ProjetoController {

	private final ProjetoService projetoService;
	private final PropostaService propostaService;
	private final UsuarioLogadoService usuarioLogadoService;

	public ProjetoController(ProjetoService projetoService, PropostaService propostaService,
			UsuarioLogadoService usuarioLogadoService) {
		this.projetoService = projetoService;
		this.propostaService = propostaService;
		this.usuarioLogadoService = usuarioLogadoService;
	}

	@GetMapping("/projetos")
	public String listar(@RequestParam(required = false) String stack, Model model) {
		if (stack != null && !stack.isBlank()) {
			model.addAttribute("projetos", projetoService.findByStack(stack));
			model.addAttribute("stack", stack);
		} else {
			model.addAttribute("projetos", projetoService.findAll());
		}
		return "projetos/lista";
	}

	@GetMapping("/projetos/{id}")
	public String detalhe(@PathVariable Long id, Model model) {
		model.addAttribute("projeto", projetoService.findById(id));

		if (isDesenvolvedorLogado()) {
			Desenvolvedor dev = usuarioLogadoService.getDesenvolvedorLogado();
			model.addAttribute("jaProposto", propostaService.jaProposto(dev, id));
		}

		return "projetos/detalhe";
	}

	private boolean isDesenvolvedorLogado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.isAuthenticated()
				&& auth.getAuthorities().stream()
						.anyMatch(a -> a.getAuthority().equals("ROLE_DESENVOLVEDOR"));
	}
}
