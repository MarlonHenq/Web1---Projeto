package br.ufscar.dc.dsw.service.dto;

import br.ufscar.dc.dsw.domain.enums.StatusProposta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PropostaAnaliseForm {

	@NotNull
	private StatusProposta status;

	@Size(max = 2000)
	private String mensagem;

	private String horarioReuniao;

	@Size(max = 500)
	private String linkVideoconferencia;

	public StatusProposta getStatus() {
		return status;
	}

	public void setStatus(StatusProposta status) {
		this.status = status;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getHorarioReuniao() {
		return horarioReuniao;
	}

	public void setHorarioReuniao(String horarioReuniao) {
		this.horarioReuniao = horarioReuniao;
	}

	public String getLinkVideoconferencia() {
		return linkVideoconferencia;
	}

	public void setLinkVideoconferencia(String linkVideoconferencia) {
		this.linkVideoconferencia = linkVideoconferencia;
	}
}
