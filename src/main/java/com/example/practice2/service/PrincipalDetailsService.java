package com.example.practice2.service;

import com.example.practice2.domain.Member;
import com.example.practice2.repository.MemberRepository;
import com.example.practice2.security.PrincipalDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public PrincipalDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // security 가 로그인 폼에서 아이디를 받으면, 더 이상 자기 내부의 임시 user 계정을 안 찾고,
        // 이 메서드를 호출해서 MemberRepository 를 조회
        Member member = memberRepository.findByLoginId(username);

        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 아이디");
        }
        return new PrincipalDetails(member);
    }
}///
