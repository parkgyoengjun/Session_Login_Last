package com.example.practice2.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 컨트롤러가 실행되기 전에 가로채서 실행되는 메서드
        // handler : 이 요청을 처리할 예정인 컨트롤러 메서드에 대한 정보(어떤 컨트롤러로 가려던 요청인지" 구분할때 사용)

        HttpSession session = request.getSession(false);// session 자동 생성 방지

        if (session == null || session.getAttribute("loginUser") == null) {
            // 세션 상태 판단

            response.sendRedirect("/login");
            return false; // 컨트롤러로 요청을 넘기지 않겠다
        }
        return true; // 세션 확인 상태 => 컨트롤러로 요청 진행 ( chain.doFilter(req,res) 랑 같은 역할 )
    }
}///
