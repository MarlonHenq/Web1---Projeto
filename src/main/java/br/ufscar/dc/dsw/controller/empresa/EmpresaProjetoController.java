package br.ufscar.dc.dsw.controller.empresa;

import java.util.Collections;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.exception.BusinessException;
import br.ufscar.dc.dsw.service.ProjetoService;
import br.ufscar.dc.dsw.service.UsuarioLogadoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/empresa/projetos")
@PreAuthorize("hasRole('EMPRESA')")
public class EmpresaProjetoController {

	private final ProjetoService projetoService;
	private final UsuarioLogadoService usuarioLogadoService;

	public EmpresaProjetoController(ProjetoService projetoService, UsuarioLogadoService usuarioLogadoService) {
		this.projetoService = projetoService;
		this.usuarioLogadoService = usuarioLogadoService;
	}

	@GetMapping
	public String listar(Model model) {
		Empresa empresa = usuarioLogadoService.getEmpresaLogada();
		model.addAttribute("projetos", projetoService.findByEmpresa(empresa));
		return "empresa/projetos/lista";
	}

	@GetMapping("/novo")
	public String novo(Model model) {
		model.addAttribute("projeto", new Projeto());
		return "empresa/projetos/form";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid @ModelAttribute Projeto projeto, BindingResult result,
			@RequestParam(value = "anexos", required = false) List<MultipartFile> anexos,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return "empresa/projetos/form";
		}
		try {
			Empresa empresa = usuarioLogadoService.getEmpresaLogada();
			projeto.setEmpresa(empresa);
			List<MultipartFile> files = anexos != null ? anexos : Collections.emptyList();
			projetoService.save(projeto, files);
			redirect.addFlashAttribute("sucesso", "Projeto cadastrado.");
		} catch (BusinessException ex) {
			redirect.addFlashAttribute("erro", ex.getMessage());
			return "redirect:/empresa/projetos/novo";
		}
		return "redirect:/empresa/projetos";
	}

	@GetMapping("/{id}")
	public String detalhe(@PathVariable Long id, Model model) {
		Empresa empresa = usuarioLogadoService.getEmpresaLogada();
		Projeto projeto = projetoService.findById(id);
		if (!projeto.getEmpresa().getId().equals(empresa.getId())) {
			return "redirect:/empresa/projetos";
		}
		model.addAttribute("projeto", projeto);
		return "empresa/projetos/detalhe";
	}
}
