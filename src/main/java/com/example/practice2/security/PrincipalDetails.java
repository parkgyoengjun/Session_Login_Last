package com.example.practice2.security;

import com.example.practice2.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class PrincipalDetails implements UserDetails {
    // UserDetails 는 security 가 "인증된 사용자" 를 표현할 때 쓰는 표준 인터페이스

    private final Member member; // PrincipalDetails 가 Member 를 감싸서 security 가 이해할수 있게

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // 권한 - 지금은 단일 권한만
    }

    @Override
    public String getPassword() {
        return member.getPassword(); // 이미 BCrypt 로 암호화되어 저장된 값
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}///
