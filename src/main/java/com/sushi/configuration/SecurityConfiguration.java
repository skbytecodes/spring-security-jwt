package com.sushi.configuration;

import com.sushi.dto.Permission;
import com.sushi.dto.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/demo-controller/","/api/v1/auth/**")
                .permitAll()

                .requestMatchers("/api/v1/management/**").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())

                .requestMatchers(HttpMethod.GET,"/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_READ.name(),Permission.MANAGER_READ.name())
                .requestMatchers(HttpMethod.POST,"/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_CREATE.name(),Permission.MANAGER_CREATE.name())
                .requestMatchers(HttpMethod.PUT,"/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name(),Permission.MANAGER_UPDATE.name())
                .requestMatchers(HttpMethod.DELETE,"/api/v1/management/**").hasAnyAuthority(Permission.ADMIN_DELETE.name(),Permission.MANAGER_DELETE.name())


                .requestMatchers("/api/v1/admin/**").hasRole(Role.ADMIN.name())

                .requestMatchers(HttpMethod.GET,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_READ.name())
                .requestMatchers(HttpMethod.POST,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_CREATE.name())
                .requestMatchers(HttpMethod.PUT,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_UPDATE.name())
                .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_DELETE.name())


                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
