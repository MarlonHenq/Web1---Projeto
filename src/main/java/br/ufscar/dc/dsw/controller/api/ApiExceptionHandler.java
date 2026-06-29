package br.ufscar.dc.dsw.controller.api;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.ufscar.dc.dsw.exception.BusinessException;
import br.ufscar.dc.dsw.exception.ResourceNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "br.ufscar.dc.dsw.controller.api")
public class ApiExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
		log.warn("API - recurso não encontrado: {}", ex.getMessage());
		return build(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
		log.warn("API - erro de negócio: {}", ex.getMessage());
		HttpStatus status = isConflito(ex.getMessage()) ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
		return build(status, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> campos = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(erro -> campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));
		log.warn("API - validação falhou: {}", campos);
		Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, "Dados inválidos.");
		body.put("campos", campos);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException ex) {
		log.warn("API - corpo da requisição inválido: {}", ex.getMessage());
		return build(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou malformado.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
		log.error("API - erro inesperado", ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor.");
	}

	private boolean isConflito(String mensagem) {
		if (mensagem == null) {
			return false;
		}
		return mensagem.contains("já cadastrado") || mensagem.contains("já possui");
	}

	private ResponseEntity<Map<String, Object>> build(HttpStatus status, String mensagem) {
		return ResponseEntity.status(status).body(baseBody(status, mensagem));
	}

	private Map<String, Object> baseBody(HttpStatus status, String mensagem) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", mensagem);
		return body;
	}
}
