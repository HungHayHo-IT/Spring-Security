package com.example.spring_security.config;

import com.example.spring_security.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.example.spring_security.exceptionhandling.CustomeAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
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
            "/error",
            "/register",
            "/invalidSession"

    };

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.sessionManagement(smc->smc.invalidSessionUrl("/invalidSession")) // het phien dang nhap chuyen den trang nay
                .csrf(csrfConfig->csrfConfig.disable());
        http.authorizeHttpRequests(request->request
                .requestMatchers(publicUrl).authenticated()
                .requestMatchers(privateUrl).permitAll()

        );
        http.formLogin(
                withDefaults()
        );
        http.httpBasic(
            hbc->hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())
        );

        http.exceptionHandling(ehc->ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomeAccessDeniedHandler()).accessDeniedPage("/denied")// khi nao co loi 403 se chuyen den trang nay
        );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
