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

import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.exception.BusinessException;
import br.ufscar.dc.dsw.service.DesenvolvedorService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/desenvolvedores")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDesenvolvedorController {

	private final DesenvolvedorService desenvolvedorService;

	public AdminDesenvolvedorController(DesenvolvedorService desenvolvedorService) {
		this.desenvolvedorService = desenvolvedorService;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("desenvolvedores", desenvolvedorService.findAll());
		return "admin/desenvolvedores/lista";
	}

	@GetMapping("/novo")
	public String novo(Model model) {
		model.addAttribute("desenvolvedor", new Desenvolvedor());
		model.addAttribute("edicao", false);
		return "admin/desenvolvedores/form";
	}

	@GetMapping("/{id}/editar")
	public String editar(@PathVariable Long id, Model model) {
		model.addAttribute("desenvolvedor", desenvolvedorService.findById(id));
		model.addAttribute("edicao", true);
		return "admin/desenvolvedores/form";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid @ModelAttribute Desenvolvedor desenvolvedor, BindingResult result,
			@RequestParam(required = false) String senhaPlana,
			@RequestParam(defaultValue = "false") boolean edicao,
			Model model, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			model.addAttribute("edicao", edicao);
			return "admin/desenvolvedores/form";
		}
		try {
			if (edicao) {
				desenvolvedorService.update(desenvolvedor.getId(), desenvolvedor, senhaPlana);
				redirect.addFlashAttribute("sucesso", "Desenvolvedor atualizado.");
			} else {
				desenvolvedorService.save(desenvolvedor, senhaPlana);
				redirect.addFlashAttribute("sucesso", "Desenvolvedor cadastrado.");
			}
		} catch (BusinessException ex) {
			redirect.addFlashAttribute("erro", ex.getMessage());
			return edicao ? "redirect:/admin/desenvolvedores/" + desenvolvedor.getId() + "/editar"
					: "redirect:/admin/desenvolvedores/novo";
		}
		return "redirect:/admin/desenvolvedores";
	}

	@PostMapping("/{id}/excluir")
	public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
		desenvolvedorService.delete(id);
		redirect.addFlashAttribute("sucesso", "Desenvolvedor excluído.");
		return "redirect:/admin/desenvolvedores";
	}
}
