package com.example.practice2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class Config {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /*
     * BCryptPasswordEncoder 는 데이터 유출을 막기위해 원본을 변경하게 해줌
     * 주요메서드
     * 1. encode() - 평문을 암호화된 문자열로 변환
     * String rawPassword = "1234";
     * String encoded = encoder.encode(rawPassword); -> $2a$10$N9qo8uLOic
     *
     * 2. matches() - 쳥문과 암호화된 값이 같은 비밀번호에서 나온 건지 검증
     * Boolean a = encoder.matches(password, encoded);
     * System.out.println(isMatch); // true
     *  .equals() 나 == 로 비교가 안된다.
     *
     * 그리고 쉽게 쓰기 위해 의존성을 추가했지만 bean 등록 함
     */
}///
