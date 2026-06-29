package br.ufscar.dc.dsw.controller.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.ufscar.dc.dsw.controller.api.dto.EmpresaRequest;
import br.ufscar.dc.dsw.controller.api.dto.EmpresaResponse;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.service.EmpresaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaRestController {

	private final EmpresaService empresaService;

	public EmpresaRestController(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@GetMapping
	public List<EmpresaResponse> listar() {
		return empresaService.findAll().stream()
				.map(EmpresaResponse::from)
				.toList();
	}

	@GetMapping("/{id}")
	public EmpresaResponse buscar(@PathVariable Long id) {
		return EmpresaResponse.from(empresaService.findById(id));
	}

	@PostMapping
	public ResponseEntity<EmpresaResponse> criar(@Valid @RequestBody EmpresaRequest request,
			UriComponentsBuilder uriBuilder) {
		Empresa salva = empresaService.save(request.toEntity(), request.getSenha());
		URI location = uriBuilder.path("/api/empresas/{id}").buildAndExpand(salva.getId()).toUri();
		return ResponseEntity.created(location).body(EmpresaResponse.from(salva));
	}

	@PutMapping("/{id}")
	public EmpresaResponse atualizar(@PathVariable Long id, @Valid @RequestBody EmpresaRequest request) {
		Empresa atualizada = empresaService.update(id, request.toEntity(), request.getSenha());
		return EmpresaResponse.from(atualizada);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {
		empresaService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
