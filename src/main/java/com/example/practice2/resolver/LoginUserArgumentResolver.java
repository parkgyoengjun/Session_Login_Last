package com.example.practice2.resolver;

import com.example.practice2.annotation.LoginUser;
import com.example.practice2.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 스프링이 컨트롤러 호출 직전,
        // 컨트롤러 메서드들 중 파라미터 요소들을 처리할 수 있는지
        // true(처리할 수 있다) 면 resolveArgument() 가 호출되어 값을 채운다.

        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        // 파라미터에 @LoginUser 가 붙어있는지 확인
        // @Retention(RetentionPolicy.RUNTIME) 가 있어서 가능함( 정보가 사라지기 떄문에 )

        boolean isMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        // parameter.getParameterType() : 타입이 뭔지 가져옴
        // Member.class.isAssignableFrom() : Member 타입이거나 자식타입 인가 검사

        return hasLoginUserAnnotation && isMemberType;
        // 두 조건을 모두 만족해야 이 리졸버가 처리를 담당
        // 캐스팅 에러를 막기위해 @LoginUser 가 붙은 Member 타입 파라미터만 골라내기위해 두 조건 모두 만족
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // supportsParameter() 가 true 를 반환한 경우에만 호출, 실제 값을 만들어서 반환하는 메서드
        // Object 인 이유는 Member 뿐만 아니라 어떤 타입이든 리졸빙할 수 있도록
        // MethodParameter(위치확인용), ModelAndViewContainer(뷰/모델 관련)
        // NativeWebRequest(이것만 씀), WebDataBinderFactory(데이터 바인딩 관련)

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        //

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }
        return session.getAttribute("loginUser");
    }
}
