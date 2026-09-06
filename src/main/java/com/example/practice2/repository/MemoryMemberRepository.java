package com.example.practice2.repository;

import com.example.practice2.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryMemberRepository implements MemberRepository{

    private final Map<Long,Member> map = new HashMap();
    private Long count =0L;

    @Override
    public void save(Member member) {
        ++count;
        Member saveMember = new Member(count, member.getLoginId(), member.getPassword(), member.getNickname());

        System.out.println(count +" repository count @@@@");
        System.out.println(count+  " 회원가입 완료 repository");
//        map.put( count, saveMember);  // 여기서 틀림 map.put( count, saveMember) 보다 member에서 꺼내서 넣어주는게 좋음
        map.put(saveMember.getIdNum(), saveMember);
    }

    @Override
    public Member findById(Long id) {
        return map.get(id);
    }

    @Override
    public Member findByLoginId(String loginId) {
        for (Member member : map.values()) {
            if (member.getLoginId().equals(loginId)) {
                return member;
            }
        }
        return null;

//        return store.values().stream()
//                .filter(member -> member.getLoginId().equals(loginId))
//                .findFirst()
//                .orElse(null);
    }


//    @Override
//    public void save(String saveId, String password, String nickname) {
//        count = (count == null) ? 1 :count+1;
//        map.put(count, new Member(count,saveId,password,nickname));
//    }
//
//    @Override
//    public Member findById(Long id) {
//        return map.get(id);
//    }

}///
