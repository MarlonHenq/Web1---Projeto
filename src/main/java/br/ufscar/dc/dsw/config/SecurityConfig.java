package br.ufscar.dc.dsw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authz -> authz
				.requestMatchers("/css/**", "/js/**", "/uploads/**", "/error/**").permitAll()
				.requestMatchers("/", "/projetos", "/projetos/**").permitAll()
				.requestMatchers("/api/**").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/empresa/**").hasRole("EMPRESA")
				.requestMatchers("/dev/**").hasRole("DESENVOLVEDOR")
				.anyRequest().authenticated())
			.csrf(csrf -> csrf
				.ignoringRequestMatchers("/api/**"))
			.formLogin(form -> form
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.permitAll())
			.logout(logout -> logout
				.logoutSuccessUrl("/")
				.permitAll());
		return http.build();
	}
}
