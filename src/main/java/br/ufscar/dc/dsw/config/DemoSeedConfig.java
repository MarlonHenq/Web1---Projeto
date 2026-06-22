package br.ufscar.dc.dsw.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.ufscar.dc.dsw.dao.IAnexoDAO;
import br.ufscar.dc.dsw.dao.IProjetoDAO;
import br.ufscar.dc.dsw.dao.IPropostaDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Anexo;
import br.ufscar.dc.dsw.domain.Desenvolvedor;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Projeto;
import br.ufscar.dc.dsw.domain.Proposta;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.enums.Sexo;
import br.ufscar.dc.dsw.domain.enums.StatusProposta;

@Configuration
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DemoSeedConfig {

	private static final Logger log = LoggerFactory.getLogger(DemoSeedConfig.class);

	@Bean
	public CommandLineRunner demoSeed(
			IUsuarioDAO usuarioDAO,
			IProjetoDAO projetoDAO,
			IPropostaDAO propostaDAO,
			IAnexoDAO anexoDAO,
			PasswordEncoder passwordEncoder,
			@Value("${app.demo.admin.password:}") String adminPassword,
			@Value("${app.demo.empresa.password:}") String empresaPassword,
			@Value("${app.demo.dev.password:}") String devPassword) {
		return args -> {
			if (usuarioDAO.count() > 0) {
				log.info("Seed: banco já possui usuários, nada a fazer.");
				return;
			}
			if (adminPassword.isBlank() || empresaPassword.isBlank() || devPassword.isBlank()) {
				log.warn("Seed: senhas demo não configuradas (DEMO_*_PASSWORD). Usuários não criados.");
				return;
			}

			log.info("Seed: criando usuários de demonstração");
			Usuario admin = Usuario.admin("admin@freelancers.com",
					passwordEncoder.encode(adminPassword), "Administrador");
			usuarioDAO.save(admin);

			Empresa empresa = new Empresa("contato@techcorp.com",
					passwordEncoder.encode(empresaPassword), "TechCorp Ltda",
					"12.345.678/0001-90", "Empresa de tecnologia especializada em soluções web");
			usuarioDAO.save(empresa);

			Desenvolvedor dev = new Desenvolvedor("joao@dev.com",
					passwordEncoder.encode(devPassword), "João Silva",
					"123.456.789-00", "(16) 99999-0000",
					Sexo.M, LocalDate.of(1995, 3, 15));
			usuarioDAO.save(dev);

			Projeto projeto = new Projeto(
					"E-commerce Spring Boot",
					"Desenvolvimento de loja virtual com Spring MVC, Thymeleaf e JPA",
					"Java, Spring Boot, Thymeleaf, MySQL",
					new BigDecimal("15000.00"),
					LocalDate.of(2026, 8, 31),
					empresa);
			projetoDAO.save(projeto);

			Proposta proposta = new Proposta(
					new BigDecimal("12000.00"),
					LocalDate.of(2026, 7, 15),
					"Experiência com Spring Boot e projetos similares de e-commerce",
					LocalDate.now(),
					StatusProposta.ABERTO,
					dev,
					projeto);
			propostaDAO.save(proposta);

			Anexo anexo = new Anexo("mockup-home.png", "/uploads/mockup-home.png", projeto);
			anexoDAO.save(anexo);

			log.info("Seed concluído: 3 usuários + projeto demo.");
		};
	}
}
