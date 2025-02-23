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

    // Criptografia de senha com BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Definição do AuthenticationManager com UserDetailsService
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(List.of(authProvider));
    }

    // Configuração das regras de segurança HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Desativa CSRF (para APIs)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/usuarios/cadastro", "/usuarios/login", "/login.html", "/cadastro.html", "/admin.html").permitAll()  // Permite acesso ao login e cadastro
                .anyRequest().authenticated()  // Qualquer outra requisição precisa de autenticação
            )
            .formLogin(form -> form
                .loginPage("/login.html")  // Página de login
                .loginProcessingUrl("/login")  // URL para processar login
                .defaultSuccessUrl("/home", true)  // Redirecionamento após login bem-sucedido
                .failureUrl("/login-error")  // Redirecionamento em caso de falha
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login.html")  // Redireciona para login após logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
