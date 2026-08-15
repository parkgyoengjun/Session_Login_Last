package com.example.practice2.controller;

import com.example.practice2.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//import org.springframework.mock.web.MockHttpSession;


//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest // 스프링 컨테이너 전체를 띄워서 테스트( 실제 빈들이 동작 )
@AutoConfigureMockMvc // MockMVC 를 자동으로 설정해서 @Autowired 주입받을 수 있게 함
public class LoginControllerTest { // 패키지 경로는 테스트 대상 클래스와 동일하게 맞춰주는 게 관례

    @Autowired
    private MockMvc mockMvc; // 실제 서버를 띄우지 않고, 가짜로 HTTP 요청을 흉내 내는 도구

    @BeforeEach
    void first_login_practice () throws Exception{
//        new Member(1L, "testId", "testPassword", "테스트닉네임");
        mockMvc.perform(MockMvcRequestBuilders.post("/save")
                .param("savedId", "testId") // @RequestParam 처럼 받는 값을 채워줌
                .param("password", "testPassword")
                .param("nickname", "테스트닉네임"));
    }

    @Test
    void login_success_mypage_redirect() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("loginId", "testId")
                        .param("password", "testPassword"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection()) // status 가 3xx 인지 검증
                .andExpect(MockMvcResultMatchers.redirectedUrl("/mypage")); // redirect 대상의 url 을 검증
    }

    @Test
    void Not_Login_GoToMypage_Redirect_LoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mypgege"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }

//    @Test  // 학습용으로 실행 시 302번 에러가 뜸 직접적인 session 을 넣어줘야 작동됨
//    void LoginedSession_true_myPage() throws Exception {
//        MockHttpSession mockHttpSession = new MockHttpSession(); // 가짜 세션 생성
//        // 실제로는 로그인 api 를 먼저 호출해서 세션을 채우는 방식이 더 정확하지만,
//        // 여기서는 개념 확인을 위해 세션에 직접 값을 넣어보는 방식도 가능
//        // session.setAttribute("loginedUser",someMember");
//
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/mypage").session(mockHttpSession))
//                // .session(mockHttpSession) : 만든 가짜 세션을 이 요청에 실어서 보냄 ( 마치 로그인된 상태인 것처럼 )
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
}///