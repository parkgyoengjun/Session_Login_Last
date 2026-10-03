package com.example.practice2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class SecurityConfig {
//    SecurityFilterChain
//      - 인증, 인가, CSRF 방어, 세션관리 등을 각각 담당하는 여러 개의 filter를 자동으로 체인처럼 연결해서 요청 처리

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http    //. csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/", "login", "/save", "/css/**", "/js/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                // LoginCheckFilter, LoginCheckInterceptor 와 같은 역할
                // .anyRequest().authenticated() : getSession(false) 와 같은 역할( null 체크 )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/mypage", true)
                        .permitAll())
                // 로그인 성공시 인증 처리를 스프링이 대신함
                .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                        // 로그아웃 대신 함)
                )
                .sessionManagement(session -> session
                        .maximumSessions(1) // 계당 세션 1개만 허용
                        .maxSessionsPreventsLogin(false)); // false  : 새로그인 성공, 기존 세션은 만료됨
        /**
         * spring security 는 SessionRegistry 라는 컴퍼넌트를 통해 " 어떤 사용자가, 어떤 세션 ID로 로그인해 있는지"
         * 계속 기록한다.
         */
        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        // 이것도 bean 등록을 해야함
        // spring security 가 세션 만료를 감지하려면 이 리스너가 필요함
        // 빠트리면 세션이 계속 쌓이기만 하고 정리가 안됨
        /**
         * 사용자가 브라우저를 닫거나, 세션이 타임아웃되면, 톰캣(서블릿컨에이너 ) 은 그 세션을 자동으로 소멸시킴
         * 하지만 SessionRegistry 는 톰캣과 별개의 스프링컴퍼넌트라서, "톰캣에서 세션이 방금 사라졌다" 는 사실을 저절로 알수 없음
         * HttpSessionEventPublisher 는 톰캣이 세션을 만들거나 없앨 때마다 그 이벤트르 감지해서, SessionRegistry에게
         * " 이 세션이 사라졌어, 장부를 지워" 라고 알려주는 연결다리 역할 이게 없으면 SessionRegistry 의 기록이 실제 상황과 어긋나서,
         * maximumSessions 가 정확히 동작하지 않음
         */
        return new HttpSessionEventPublisher();
    }
}///
