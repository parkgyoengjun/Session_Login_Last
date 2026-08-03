package com.example.practice2.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class LoginCheckFilter implements Filter {
    /*
    Filter
        - 서블릿컨테이너(톰캣) 레벨에서 동작 - 스프링의 DispatcherServlet 보다 앞단에서 요청을 가로챔
        - doFilter() 안에서 chain.doFilter(request, response) 를 호출해야 다음 단계( 다른 필터 또는 서블릿)로
          요청이 넘어감. 호출 안하면 요청이 거기서 끊김
        - web 의존성에 있어서 따로 의존성을 추가할 필요없음
    사용 목적
    컨트롤러마다 적었던 체크메서드( if(loginUser == null ) 을 한곳에서 공통으로 처리
    */
    private static final String[] whiteList = {"/", "/login", "/save", "/css/*", "/js/*"};
    // 로그인 없이도 접근 가능한 경로
    // static 을 붙이는 이유는 클래스 레벨에 하나만 존재(공유), static 을 안붙이면 new 객체생성할때 마다 배열이 새로 생김
    // final 은 다른값으로 할당되는것을 막는것

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = httpRequest.getRequestURI();

        if (isLoginCheckPath(uri)) { // 허용되지 않은 uri 면
            HttpSession session = httpRequest.getSession(false); // 세션이 없으면 만들지 않는다.

            if (session == null || session.getAttribute("loginUser") == null) {
                httpResponse.sendRedirect("/login");
                return; // 여기서 체인 호출 안함 -> 컨트롤러로 요청이 넘어가지 않음
            }
        }
        chain.doFilter(request, response); // 허용된 uri 이면 다음 단계로 진행
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
//        PatternMatchUtils.simpleMatch() 는 스프링이 제공하는 유틸로, * 같은 패턴 매칭을 도와줌
//        whiteList 에 없는 경로면 세션체크 => 없으면 로그인 페이지로 리다이렉트하고 return 으로 종료
    }
}///
