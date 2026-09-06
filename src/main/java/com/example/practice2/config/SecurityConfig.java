package com.example.practice2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
//    SecurityFilterChain
//      - 인증, 인가, CSRF 방어, 세션관리 등을 각각 담당하는 여러 개의 filter를 자동으로 체인처럼 연결해서 요청 처리

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    //. csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers( "/","login", "/save", "/css/**", "/js/**").permitAll()
                                .anyRequest().authenticated())
                // LoginCheckFilter, LoginCheckInterceptor 와 같은 역할
                // .anyRequest().authenticated() : getSession(false) 와 같은 역할( null 체크 )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/mypage",true)
                        .permitAll())
                // 로그인 성공시 인증 처리를 스프링이 대신함
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/"));
                // 로그아웃 대신 함
        return http.build();
    }

}///
