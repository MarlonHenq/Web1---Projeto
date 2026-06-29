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

import br.ufscar.dc.dsw.controller.api.dto.DesenvolvedorRequest;
import br.ufscar.dc.dsw.controller.api.dto.DesenvolvedorResponse;
import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.service.DesenvolvedorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/desenvolvedores")
public class DesenvolvedorRestController {

	private final DesenvolvedorService desenvolvedorService;

	public DesenvolvedorRestController(DesenvolvedorService desenvolvedorService) {
		this.desenvolvedorService = desenvolvedorService;
	}

	@GetMapping
	public List<DesenvolvedorResponse> listar() {
		return desenvolvedorService.findAll().stream()
				.map(DesenvolvedorResponse::from)
				.toList();
	}

	@GetMapping("/{id}")
	public DesenvolvedorResponse buscar(@PathVariable Long id) {
		return DesenvolvedorResponse.from(desenvolvedorService.findById(id));
	}

	@PostMapping
	public ResponseEntity<DesenvolvedorResponse> criar(@Valid @RequestBody DesenvolvedorRequest request,
			UriComponentsBuilder uriBuilder) {
		Desenvolvedor salvo = desenvolvedorService.save(request.toEntity(), request.getSenha());
		URI location = uriBuilder.path("/api/desenvolvedores/{id}").buildAndExpand(salvo.getId()).toUri();
		return ResponseEntity.created(location).body(DesenvolvedorResponse.from(salvo));
	}

	@PutMapping("/{id}")
	public DesenvolvedorResponse atualizar(@PathVariable Long id,
			@Valid @RequestBody DesenvolvedorRequest request) {
		Desenvolvedor atualizado = desenvolvedorService.update(id, request.toEntity(), request.getSenha());
		return DesenvolvedorResponse.from(atualizado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {
		desenvolvedorService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
