package br.ufscar.dc.dsw.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.ufscar.dc.dsw.dao.IAnexoDAO;
import br.ufscar.dc.dsw.domain.Anexo;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.exception.BusinessException;

@Service
public class AnexoService {

	private static final int MAX_ANEXOS = 10;
	private static final Set<String> EXTENSOES = Set.of("png", "jpg", "jpeg", "gif", "webp");

	private final IAnexoDAO anexoDAO;
	private final Path uploadDir;

	public AnexoService(IAnexoDAO anexoDAO) {
		this.anexoDAO = anexoDAO;
		this.uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
		try {
			Files.createDirectories(uploadDir);
		} catch (IOException e) {
			throw new BusinessException("Não foi possível criar diretório de uploads.");
		}
	}

	@Transactional(readOnly = true)
	public List<Anexo> findByProjeto(Projeto projeto) {
		return anexoDAO.findByProjeto(projeto);
	}

	@Transactional
	public void salvarAnexos(Projeto projeto, List<MultipartFile> arquivos) {
		if (arquivos == null || arquivos.isEmpty()) {
			return;
		}
		long existentes = anexoDAO.findByProjeto(projeto).size();
		if (existentes + arquivos.size() > MAX_ANEXOS) {
			throw new BusinessException("Máximo de " + MAX_ANEXOS + " anexos por projeto.");
		}
		for (MultipartFile arquivo : arquivos) {
			if (arquivo.isEmpty()) {
				continue;
			}
			String nomeOriginal = arquivo.getOriginalFilename();
			if (nomeOriginal == null || !extensaoPermitida(nomeOriginal)) {
				throw new BusinessException("Formato de arquivo não permitido: " + nomeOriginal);
			}
			String nomeUnico = UUID.randomUUID() + "_" + nomeOriginal;
			Path destino = uploadDir.resolve(nomeUnico);
			try {
				arquivo.transferTo(destino);
			} catch (IOException e) {
				throw new BusinessException("Erro ao salvar anexo: " + nomeOriginal);
			}
			Anexo anexo = new Anexo(nomeOriginal, "/uploads/" + nomeUnico, projeto);
			anexoDAO.save(anexo);
		}
	}

	private boolean extensaoPermitida(String nome) {
		int idx = nome.lastIndexOf('.');
		if (idx < 0) {
			return false;
		}
		return EXTENSOES.contains(nome.substring(idx + 1).toLowerCase());
	}
}
