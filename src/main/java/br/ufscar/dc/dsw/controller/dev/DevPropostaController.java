package br.ufscar.dc.dsw.controller.dev;

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
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.domain.Proposta;
import br.ufscar.dc.dsw.exception.BusinessException;
import br.ufscar.dc.dsw.service.PropostaService;
import br.ufscar.dc.dsw.service.ProjetoService;
import br.ufscar.dc.dsw.service.UsuarioLogadoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/dev/propostas")
@PreAuthorize("hasRole('DESENVOLVEDOR')")
public class DevPropostaController {

	private final PropostaService propostaService;
	private final ProjetoService projetoService;
	private final UsuarioLogadoService usuarioLogadoService;

	public DevPropostaController(PropostaService propostaService, ProjetoService projetoService,
			UsuarioLogadoService usuarioLogadoService) {
		this.propostaService = propostaService;
		this.projetoService = projetoService;
		this.usuarioLogadoService = usuarioLogadoService;
	}

	@GetMapping
	public String listar(Model model) {
		Desenvolvedor dev = usuarioLogadoService.getDesenvolvedorLogado();
		model.addAttribute("propostas", propostaService.findByDesenvolvedor(dev));
		return "dev/propostas/lista";
	}

	@GetMapping("/novo/{projetoId}")
	public String novo(@PathVariable Long projetoId, Model model, RedirectAttributes redirect) {
		Desenvolvedor dev = usuarioLogadoService.getDesenvolvedorLogado();
		if (propostaService.jaProposto(dev, projetoId)) {
			redirect.addFlashAttribute("erro", "proposta.erro.duplicada");
			return "redirect:/projetos/" + projetoId;
		}
		Projeto projeto = projetoService.findById(projetoId);
		Proposta proposta = new Proposta();
		model.addAttribute("proposta", proposta);
		model.addAttribute("projeto", projeto);
		return "dev/propostas/form";
	}

	@PostMapping("/salvar")
	public String salvar(@RequestParam Long projetoId, @Valid @ModelAttribute Proposta proposta,
			BindingResult result, Model model, RedirectAttributes redirect) {
		Desenvolvedor dev = usuarioLogadoService.getDesenvolvedorLogado();
		Projeto projeto = projetoService.findById(projetoId);

		if (result.hasErrors()) {
			model.addAttribute("projeto", projeto);
			return "dev/propostas/form";
		}

		try {
			propostaService.criar(proposta, dev, projetoId);
			redirect.addFlashAttribute("sucesso", "proposta.sucesso.criada");
		} catch (BusinessException ex) {
			redirect.addFlashAttribute("erro", ex.getMessage());
			return "redirect:/dev/propostas/novo/" + projetoId;
		}
		return "redirect:/dev/propostas";
	}
}
