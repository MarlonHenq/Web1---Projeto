package br.ufscar.dc.dsw;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.ufscar.dc.dsw.dao.IAnexoDAO;
import br.ufscar.dc.dsw.dao.IProjetoDAO;
import br.ufscar.dc.dsw.dao.IPropostaDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Anexo;
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
	@Profile("!prod")
	public CommandLineRunner demo(
			IUsuarioDAO usuarioDAO,
			IProjetoDAO projetoDAO,
			IPropostaDAO propostaDAO,
			IAnexoDAO anexoDAO,
			PasswordEncoder passwordEncoder) {
		return args -> {
			if (usuarioDAO.count() > 0) {
				log.info("Dados de seed já existem, pulando demo.");
				return;
			}

			log.info("Salvando usuários de demonstração");
			Usuario admin = Usuario.admin("admin@freelancers.com",
					passwordEncoder.encode("admin123"), "Administrador");
			usuarioDAO.save(admin);

			Usuario empresa = Usuario.empresa("contato@techcorp.com",
					passwordEncoder.encode("senha123"), "TechCorp Ltda",
					"12.345.678/0001-90", "Empresa de tecnologia especializada em soluções web");
			usuarioDAO.save(empresa);

			Usuario dev = Usuario.desenvolvedor("joao@dev.com",
					passwordEncoder.encode("dev123"), "João Silva",
					"123.456.789-00", "(16) 99999-0000",
					Sexo.M, LocalDate.of(1995, 3, 15));
			usuarioDAO.save(dev);

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

			log.info("Seed concluído. Logins: admin@freelancers.com/admin123, contato@techcorp.com/senha123, joao@dev.com/dev123");
		};
	}
}
