package com.example.practice2.service;

import com.example.practice2.domain.Member;
import com.example.practice2.repository.MemoryMemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemoryMemberRepository re;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemoryMemberRepository re, BCryptPasswordEncoder passwordEncoder) {
        this.re = re;
        this.passwordEncoder = passwordEncoder;
    }

    // 아이디 찾기
    public Member findById(Long id) {
        return re.findById(id);
    }

    // 회원가입
    public void save(String saveId, String password, String nickname) {
        String encodePassword = passwordEncoder.encode(password);
        Member member = Member.of(saveId, encodePassword, nickname);
        System.out.println(member.getLoginId()+ member.getPassword()+ member.getNickname() + " 회원가입 완료 service");
        re.save(member);
    }

    // 로그인
    public Member login(String loginId, String password) {
        Member byId = re.findByLoginId(loginId);
//        if (byId.getLoginId().equals(loginId) && byId.getPassword().equals(password)) {
//            System.out.println("service - 로그인 메서드 성공");
//            return byId;
//        }  --> config passwordEncode 를 설정해서 암호화된거랑 비교가 안되서 그랬던거임

        if (byId.getLoginId().equals(loginId) && passwordEncoder.matches(password, byId.getPassword())) {
            System.out.println("service - 로그인 메서드 성공");
            return byId;
        } else {
            System.out.println("service - 로그인 메서드 실패");;
            return null;
        }
    }
}///
