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
            .csrf(csrf -> csrf.disable()) // 🔹 Desativando CSRF para evitar erros em requisições POST
            .authorizeHttpRequests(auth -> auth
                .requestMatchers( // 🔥 Permitir acesso público às páginas e endpoints
                    "/usuarios/cadastro",
                    "/usuarios/login",
                    "/login.html",
                    "/cadastro.html",
                    "/admin.html",
                    "/listaUsers.html",
                    "/usuarios/listar" // 🔹 Agora está liberado
                ).permitAll()
                .anyRequest().authenticated() // 🔒 Protege o resto do sistema
            )
            .formLogin(form -> form
                .loginPage("/login.html") // 🔹 Página de login
                .loginProcessingUrl("/usuarios/login") // 🔥 Agora bate com o endpoint do back-end
                .defaultSuccessUrl("/admin.html", true) // 🔥 Corrigido para uma página válida
                .failureUrl("/login.html?error=true") // 🔹 Redireciona para login com erro
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login.html") // 🔥 Após logout, redireciona para login
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
