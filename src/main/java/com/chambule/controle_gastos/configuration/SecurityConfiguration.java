package com.chambule.controle_gastos.configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

            private final SecurityExceptionHandler securityExceptionHandler;
            private final SecurityFilter securityFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
            return httpSecurity.csrf(csrf -> csrf.disable()
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                            ).exceptionHandling(exception -> exception.authenticationEntryPoint(securityExceptionHandler)
                            .accessDeniedHandler(securityExceptionHandler)).authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(HttpMethod.POST, "/users/save").permitAll()
                            .requestMatchers(HttpMethod.POST, "/users/login").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.GET,"/users/getAll").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,"/users/{id}").hasAnyRole("ADMIN","USER")
                                    .requestMatchers(HttpMethod.POST,"/categories/save").hasAnyRole("ADMIN","USER")
                                    .requestMatchers(HttpMethod.GET,"/categories/getAll").hasAnyRole("ADMIN","USER")
                                    .requestMatchers(HttpMethod.GET,"/categories/{id}").hasAnyRole("ADMIN","USER")
                                    .requestMatchers(HttpMethod.DELETE,"/categories/{id}").hasRole("USER")
                                    .requestMatchers(HttpMethod.PUT,"/categories/{id}").hasRole("USER")
                            .anyRequest().denyAll()).addFilterBefore()// falta a parte de filtros
        }

        @Bean  // para  criptografia de senhas
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


        @Bean // para  autenticação de login e senha
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
            return authenticationConfiguration.getAuthenticationManager();
        }
}
