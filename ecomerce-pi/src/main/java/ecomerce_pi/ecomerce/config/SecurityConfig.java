package ecomerce_pi.ecomerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desativando CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/usuarios/cadastro",
                    "/usuarios/login",
                    "/login.html",
                    "/cadastro.html",
                    "/admin.html",
                    "/listaUsers.html",
                    "/usuarios/listar",
                    "/usuarios/{id}" // 🔹 Liberando o endpoint para buscar usuário por ID
                ).permitAll() // Permite acesso público aos endpoints listados
                .anyRequest().authenticated() // Exige autenticação para o resto
            )
            .formLogin(form -> form
                .loginPage("/login.html") // Página de login
                .loginProcessingUrl("/usuarios/login") // Endpoint de processamento de login
                .defaultSuccessUrl("/admin.html", true) // Redirecionamento após login bem-sucedido
                .failureUrl("/login.html?error=true") // Redirecionamento em caso de falha
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login.html") // Redirecionamento após logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
