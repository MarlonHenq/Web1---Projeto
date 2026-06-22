package br.ufscar.dc.dsw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.exception.BusinessException;
import br.ufscar.dc.dsw.service.DesenvolvedorService;
import br.ufscar.dc.dsw.service.EmpresaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cadastro")
public class CadastroController {

	private final EmpresaService empresaService;
	private final DesenvolvedorService desenvolvedorService;

	public CadastroController(EmpresaService empresaService, DesenvolvedorService desenvolvedorService) {
		this.empresaService = empresaService;
		this.desenvolvedorService = desenvolvedorService;
	}

	@GetMapping
	public String index() {
		return "cadastro/index";
	}

	@GetMapping("/empresa")
	public String empresa(Model model) {
		model.addAttribute("empresa", new Empresa());
		return "cadastro/empresa";
	}

	@GetMapping("/desenvolvedor")
	public String desenvolvedor(Model model) {
		model.addAttribute("desenvolvedor", new Desenvolvedor());
		return "cadastro/desenvolvedor";
	}

	@PostMapping("/empresa")
	public String salvarEmpresa(@Valid @ModelAttribute Empresa empresa, BindingResult result,
			@RequestParam String senhaPlana, Model model, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return "cadastro/empresa";
		}
		if (senhaPlana == null || senhaPlana.isBlank()) {
			model.addAttribute("erro", "cadastro.erro.senha");
			return "cadastro/empresa";
		}
		try {
			empresaService.save(empresa, senhaPlana);
			redirect.addFlashAttribute("sucesso", "cadastro.sucesso.empresa");
			return "redirect:/login";
		} catch (BusinessException ex) {
			model.addAttribute("erro", ex.getMessage());
			return "cadastro/empresa";
		}
	}

	@PostMapping("/desenvolvedor")
	public String salvarDesenvolvedor(@Valid @ModelAttribute Desenvolvedor desenvolvedor, BindingResult result,
			@RequestParam String senhaPlana, Model model, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return "cadastro/desenvolvedor";
		}
		if (senhaPlana == null || senhaPlana.isBlank()) {
			model.addAttribute("erro", "cadastro.erro.senha");
			return "cadastro/desenvolvedor";
		}
		try {
			desenvolvedorService.save(desenvolvedor, senhaPlana);
			redirect.addFlashAttribute("sucesso", "cadastro.sucesso.desenvolvedor");
			return "redirect:/login";
		} catch (BusinessException ex) {
			model.addAttribute("erro", ex.getMessage());
			return "cadastro/desenvolvedor";
		}
	}
}
