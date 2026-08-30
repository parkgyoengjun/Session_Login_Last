package com.example.practice2.repository;

import com.example.practice2.domain.Member;

public interface MemberRepository {

    // 회원가입
    void save(Member member);

    // 아이디 찾기
    Member findById(Long id);

    // 로그인
    Member findByLoginId(String loginId);
}///
