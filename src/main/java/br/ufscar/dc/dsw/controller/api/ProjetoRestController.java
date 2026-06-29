package br.ufscar.dc.dsw.controller.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.ufscar.dc.dsw.controller.api.dto.ProjetoRequest;
import br.ufscar.dc.dsw.controller.api.dto.ProjetoResponse;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.service.EmpresaService;
import br.ufscar.dc.dsw.service.ProjetoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoRestController {

	private final ProjetoService projetoService;
	private final EmpresaService empresaService;

	public ProjetoRestController(ProjetoService projetoService, EmpresaService empresaService) {
		this.projetoService = projetoService;
		this.empresaService = empresaService;
	}

	@PostMapping("/empresas/{id}")
	public ResponseEntity<ProjetoResponse> criar(@PathVariable Long id,
			@Valid @RequestBody ProjetoRequest request, UriComponentsBuilder uriBuilder) {
		Empresa empresa = empresaService.findById(id);
		Projeto projeto = request.toEntity();
		projeto.setEmpresa(empresa);
		Projeto salvo = projetoService.save(projeto, List.of(), new ArrayList<>());
		URI location = uriBuilder.path("/api/projetos/empresas/{id}").buildAndExpand(id).toUri();
		return ResponseEntity.created(location).body(ProjetoResponse.from(salvo));
	}

	@GetMapping("/empresas/{id}")
	public List<ProjetoResponse> listarPorEmpresa(@PathVariable Long id) {
		Empresa empresa = empresaService.findById(id);
		return projetoService.findByEmpresa(empresa).stream()
				.map(ProjetoResponse::from)
				.toList();
	}

	@GetMapping("/stacks/{nome}")
	public List<ProjetoResponse> listarPorStack(@PathVariable String nome) {
		return projetoService.findByStack(nome).stream()
				.map(ProjetoResponse::from)
				.toList();
	}
}
