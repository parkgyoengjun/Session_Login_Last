package com.example.practice2.domain;

public class Member {
    private  final Long idNum;
    private  String loginId;
    private  String password;
    private  String nickname;
    // final 은 객체 생성 시 한번만 설정하는 것이기때문에, idNum  같은 경우 객체 생성 후 나중에 정해지기때문에
    // final 을 하지 않음  ex) final = 금액, 좌표 같은것들
    // 즉, 수정 가능한거나 객체 생성 후 값이 정해지는것은 final 을 안붙이는게 좋다.

    public Member(Long idNum,String loginId,String password, String nickname) {
        this.idNum = idNum;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
    }

    public static Member of(String userId, String password, String nickname) {
        return new Member(null, userId, password, nickname);
    }

    public Long getIdNum() {
        return idNum;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }
}///
