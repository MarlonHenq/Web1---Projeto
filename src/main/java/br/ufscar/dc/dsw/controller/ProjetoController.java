package br.ufscar.dc.dsw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufscar.dc.dsw.service.ProjetoService;

@Controller
public class ProjetoController {

	private final ProjetoService projetoService;

	public ProjetoController(ProjetoService projetoService) {
		this.projetoService = projetoService;
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
		return "projetos/detalhe";
	}
}
