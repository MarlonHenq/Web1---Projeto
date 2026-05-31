package br.ufscar.dc.dsw;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.ufscar.dc.dsw.dao.IAnexoDAO;
import br.ufscar.dc.dsw.dao.IDesenvolvedorDAO;
import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IProjetoDAO;
import br.ufscar.dc.dsw.dao.IPropostaDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Administrador;
import br.ufscar.dc.dsw.domain.Anexo;
import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.domain.Proposta;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.enums.Sexo;
import br.ufscar.dc.dsw.domain.enums.StatusProposta;

@SpringBootApplication
public class FreelancersJpaApplication {

	private static final Logger log = LoggerFactory.getLogger(FreelancersJpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FreelancersJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(
			IUsuarioDAO usuarioDAO,
			IEmpresaDAO empresaDAO,
			IDesenvolvedorDAO desenvolvedorDAO,
			IProjetoDAO projetoDAO,
			IPropostaDAO propostaDAO,
			IAnexoDAO anexoDAO) {
		return args -> {

			// --- CREATE ---

			log.info("Salvando Administrador");
			Administrador admin = new Administrador("admin@freelancers.com", "admin123", "Administrador");
			usuarioDAO.save(admin);

			log.info("Salvando Empresa");
			Empresa empresa = new Empresa(
					"contato@techcorp.com", "senha123", "TechCorp Ltda",
					"12.345.678/0001-90", "Empresa de tecnologia especializada em soluções web");
			empresaDAO.save(empresa);

			log.info("Salvando Desenvolvedor");
			Desenvolvedor dev = new Desenvolvedor(
					"joao@dev.com", "dev123", "João Silva",
					"123.456.789-00", "(16) 99999-0000",
					Sexo.M, LocalDate.of(1995, 3, 15));
			desenvolvedorDAO.save(dev);

			log.info("Salvando Projeto");
			Projeto projeto = new Projeto(
					"E-commerce Spring Boot",
					"Desenvolvimento de loja virtual com Spring MVC, Thymeleaf e JPA",
					"Java, Spring Boot, Thymeleaf, MySQL",
					new BigDecimal("15000.00"),
					LocalDate.of(2026, 8, 31),
					empresa);
			projetoDAO.save(projeto);

			log.info("Salvando Proposta");
			Proposta proposta = new Proposta(
					new BigDecimal("12000.00"),
					LocalDate.of(2026, 7, 15),
					"Experiência com Spring Boot e projetos similares de e-commerce",
					LocalDate.now(),
					StatusProposta.ABERTO,
					dev,
					projeto);
			propostaDAO.save(proposta);

			log.info("Salvando Anexo");
			Anexo anexo = new Anexo("mockup-home.png", "/uploads/mockup-home.png", projeto);
			anexoDAO.save(anexo);

			// --- READ ---

			log.info("Imprimindo usuarios - findAll()");
			for (Usuario u : usuarioDAO.findAll()) {
				log.info(u.toString());
			}

			log.info("Imprimindo projetos da empresa TechCorp - findByEmpresa()");
			for (Projeto p : projetoDAO.findByEmpresa(empresa)) {
				log.info(p.toString());
			}

			log.info("Imprimindo propostas do projeto - findByProjeto()");
			for (Proposta prop : propostaDAO.findByProjeto(projeto)) {
				log.info(prop.toString());
			}

			log.info("Buscando projetos por stack - findByStackTecnologicaContainingIgnoreCase()");
			for (Projeto p : projetoDAO.findByStackTecnologicaContainingIgnoreCase("spring")) {
				log.info(p.toString());
			}

			// --- UPDATE ---

			log.info("Atualizando titulo do projeto");
			projeto.setTitulo("E-commerce Spring Boot v2");
			projetoDAO.save(projeto);
			log.info("Projeto atualizado: {}", projetoDAO.findById(projeto.getId()));

			log.info("Atualizando status da proposta para ACEITO");
			proposta.setStatus(StatusProposta.ACEITO);
			propostaDAO.save(proposta);
			log.info("Proposta atualizada: {}", propostaDAO.findById(proposta.getId()));

			// --- DELETE ---

			log.info("Removendo anexo");
			Long anexoId = anexo.getId();
			anexoDAO.deleteById(anexoId);
			log.info("Anexo removido. Total de anexos restantes: {}", anexoDAO.findAll().size());
		};
	}
}
