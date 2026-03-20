package com.example.spring_security.config;

import com.example.spring_security.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.example.spring_security.exceptionhandling.CustomeAccessDeniedHandler;
import com.example.spring_security.filter.*;
import com.example.spring_security.handler.CustomAuthenticationFailureHandler;
import com.example.spring_security.handler.CustomAuthenticationSucessHandler;
import com.example.spring_security.handler.CustomLogoutSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableMethodSecurity // kich hoat pre/post anotation mac dinh
@RequiredArgsConstructor
@Profile("!prod")
public class ProjectSecurityConfig {

    private final CustomAuthenticationSucessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

//    public final String[] publicUrl={
//            "/myAccount",
//            "/myBalance",
//            "/myLoans",
//            "/myCards"
//    };
    public final String[] privateUrl={
            "/notices",
            "/contact",
            "/error",
            "/register",
            "/invalidSession",
            "/apiLogin"

    };

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");


        http.cors(corsConfig->corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public @Nullable CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration(); // Khởi tạo một đối tượng chứa các thiết lập về chính sách chia sẻ tài nguyên.
                        config.setAllowedOrigins(Collections.singletonList("http://127.0.0.1:5500")); //Xác định danh sách các "nguồn" (tên miền/port) được phép gọi API này.
                        config.setAllowedMethods(Collections.singletonList("*")); //Xác định các phương thức HTTP nào được cho phép. như: GET, POST, PUT, DELETE, ....
                        config.setAllowCredentials(true); //Cho phép trình duyệt gửi kèm các thông tin xác thực như Cookies hoặc header Authorization (như User/Pass trong Basic Auth).
                        config.setAllowedHeaders(Collections.singletonList("*")); //Cho phép tất cả các loại Header mà phía Frontend gửi lên
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);//Thiết lập thời gian (tính bằng giây) mà trình duyệt có thể "nhớ" cấu hình CORS này.
                        return config;
                    }
                }))
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter() , BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .securityContext(sessionConfig->sessionConfig.requireExplicitSave(false))
                .sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// khoong su dung session , Không tạo session mới,Không dùng session để lưu thông tin đăng nhập,Mỗi request phải tự mang thông tin xác thực (token
                .csrf(csrfConfig->csrfConfig
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers("/contact", "/notices", "/register", "/login" , "/apiLogin")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );



        http.authorizeHttpRequests(request->request
                .requestMatchers("/myAccount").hasRole("USER")
                .requestMatchers("/myBalance").hasAnyRole("USER","ADMIN")
                .requestMatchers("/myLoans").authenticated()
                .requestMatchers("/myCards").hasRole("USER")
                .requestMatchers("/user").authenticated()
                .requestMatchers(privateUrl).permitAll()

        );
        http.formLogin(
                flc->flc.loginPage("/login").usernameParameter("userId").passwordParameter("secretPwd").defaultSuccessUrl("/myAccount").failureUrl("/login?error=true")
                        .successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler
        )).logout(loc->loc.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler).invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID"));
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

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService , PasswordEncoder passwordEncoder) {
        EazyBankUsernamePwdAuthenticationProvider authenticationProvider = new EazyBankUsernamePwdAuthenticationProvider(userDetailsService,passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }
}
