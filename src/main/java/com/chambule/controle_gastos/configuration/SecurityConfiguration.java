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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

            private final SecurityExceptionHandler securityExceptionHandler;
            private final SecurityFilter securityFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
            return httpSecurity.csrf(csrf ->
                csrf.disable())
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .exceptionHandling(exception -> exception.authenticationEntryPoint(securityExceptionHandler)
                                .accessDeniedHandler(securityExceptionHandler)).authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.POST, "/users/save").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/users/save/admin").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/users/getAll").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/categories/save").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/categories/getAll").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/categories/{categoryId}").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/categories/{categoryId}").hasRole("USER")
                                .requestMatchers(HttpMethod.PUT, "/categories/{id}").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "/launchs/save").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/launchs/getOne/{launchId}").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/launchs/find").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/launchs/{launchId}").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/launchs/balance/{userId}").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/launchs/category/{categoryId}").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/launchs/{launchId}").hasAnyRole("ADMIN", "USER")
                                .anyRequest().denyAll()).
                        addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();

        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
}
