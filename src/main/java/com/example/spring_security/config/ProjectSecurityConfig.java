package com.example.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    public final String[] publicUrl={
            "/myAccount",
            "/myBalance",
            "/myLoans",
            "/myCards"
    };
    public final String[] privateUrl={
            "/notices",
            "/contact",
            "/error"

    };

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(request->request
                .requestMatchers(publicUrl).authenticated()
                .requestMatchers(privateUrl).permitAll()
        );
        http.formLogin(
                withDefaults()
        );



        http.httpBasic(
                withDefaults()
        );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.withUsername("user").password("{bcrypt}$2a$12$qmnW9ti7th3rtqLPVhrIbOejLswbpKjMcYBgKRlYml03/XQrK21gq").authorities("read").build();
        UserDetails admin = User.withUsername("admin").password("{noop}321").authorities("admin").build();
        return new InMemoryUserDetailsManager(user,admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
