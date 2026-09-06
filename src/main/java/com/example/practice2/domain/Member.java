package com.example.practice2.domain;

public class Member {
    private  final Long idNum;
    private  String loginId;
    private  String password;
    private  String nickname;
    // final 같은 경우 실수로 값을 바꾸는것을 컴파일단계에서 차단
    // 자바 같은 겨우 멀티스레드 환경이므로 동시성 버그가 생길수 있는데 final 이 막아줌


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
