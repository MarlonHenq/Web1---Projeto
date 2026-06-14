package br.ufscar.dc.dsw.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.exception.BusinessException;
import br.ufscar.dc.dsw.service.EmpresaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/empresas")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEmpresaController {

	private final EmpresaService empresaService;

	public AdminEmpresaController(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("empresas", empresaService.findAll());
		return "admin/empresas/lista";
	}

	@GetMapping("/novo")
	public String novo(Model model) {
		model.addAttribute("empresa", new Empresa());
		model.addAttribute("edicao", false);
		return "admin/empresas/form";
	}

	@GetMapping("/{id}/editar")
	public String editar(@PathVariable Long id, Model model) {
		model.addAttribute("empresa", empresaService.findById(id));
		model.addAttribute("edicao", true);
		return "admin/empresas/form";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid @ModelAttribute Empresa empresa, BindingResult result,
			@RequestParam(required = false) String senhaPlana,
			@RequestParam(defaultValue = "false") boolean edicao,
			Model model, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			model.addAttribute("edicao", edicao);
			return "admin/empresas/form";
		}
		try {
			if (edicao) {
				empresaService.update(empresa.getId(), empresa, senhaPlana);
				redirect.addFlashAttribute("sucesso", "Empresa atualizada.");
			} else {
				empresaService.save(empresa, senhaPlana);
				redirect.addFlashAttribute("sucesso", "Empresa cadastrada.");
			}
		} catch (BusinessException ex) {
			redirect.addFlashAttribute("erro", ex.getMessage());
			return edicao ? "redirect:/admin/empresas/" + empresa.getId() + "/editar"
					: "redirect:/admin/empresas/novo";
		}
		return "redirect:/admin/empresas";
	}

	@PostMapping("/{id}/excluir")
	public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
		empresaService.delete(id);
		redirect.addFlashAttribute("sucesso", "Empresa excluída.");
		return "redirect:/admin/empresas";
	}
}
