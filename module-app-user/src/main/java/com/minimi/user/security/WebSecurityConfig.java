package com.minimi.user.security;

import com.minimi.user.jwt.filter.JwtAuthenticationFilter;
import com.minimi.user.jwt.provider.JwtAuthenticationProvider;
import com.minimi.user.jwt.service.JwtService;
import com.minimi.user.security.exception.hanlder.AccessDeniedHandler;
import com.minimi.user.security.exception.hanlder.AuthenticationExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationExceptionHandler())
                .accessDeniedHandler(new AccessDeniedHandler())
                .and()
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
                .mvcMatchers("/api/v1/user/**").hasRole("USER")
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtService);
        return new ProviderManager(jwtAuthenticationProvider);
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> web.ignoring()
//                .antMatchers(HttpMethod.OPTIONS,"/**")
//                .antMatchers("/")
//                .mvcMatchers("/api/v1/login")
//                .mvcMatchers("/api/v1/logout")
//                .mvcMatchers("/api/v1/re-issue")
//                .mvcMatchers("/api/v1/join/**")
//                .mvcMatchers("/api/v1/post/**")
//                .mvcMatchers("/api/v1/post/image/**")
                .mvcMatchers("/h2-console/**");
    }
}
