package com.example.practice2.config;

import com.example.practice2.filter.LoginCheckFilter;
import com.example.practice2.interceptor.LoginCheckInterceptor;
import com.example.practice2.resolver.LoginUserArgumentResolver;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

   // 1.  Filter
    // LoginCheckFilter 만들었다고 실행이 되지 않으니 config 를 이용해서 등록하기 위함
    // 별도로 설정한 이유는 구분하기위함 합쳐도 상관은 없음
    // 순수 서블릿에서는 필터를 등록할 때 web.xml 에 등록하거나 @WebFilter 를 클래스에 직접 붙여씀
    // 스프링 부트는 내장 톰캣을 쓰기 때문에 web.xml 이 따로 없고, 이걸 자바 코드로
    // 등록할 수 있게 만든게 FilterRegistrationBean 임
    // 즉, 필터를 등록하기 위한 설정 정보를 담은 객체 -> 어떻게 적용할지 에 대한 설정을 스프링에게 전달하는 역할

    @Bean
    public FilterRegistrationBean<Filter> loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        // 등록 설정을 담을 빈 상자를 하나 만드는것

        filterRegistrationBean.setFilter(new LoginCheckFilter()); // 빈에 등록
        filterRegistrationBean.setOrder(1); // 필터가 여러 개일때 실행 순서 지정( 숫자가 작을수록 먼저 실행)
        filterRegistrationBean.addUrlPatterns("/*");
        // 모든 URL 에 적용, 실제 로그인 체크여부는 필터 내부의 whiteList 로 걸러냄
        System.out.println("Filter 실행");
        return filterRegistrationBean;
    }

    // 2. Interceptor
    // WebMvcConfigurer - 스프링 mvc 의 여러설정을 커스터마이징할 수 있게 해주는 인터페이스
    // InterceptorRegistry - 인터셉터를 등록하는 창구 역할의 객체
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 스프링이 애플리케이션을 초기화할 때, 등록하고싶은 인터셉터가 있는지 확인하려고 자동으로 호출해주는 메서드
        // registry - 여기에 인터셉터를 추가하면 스프링이 내부적으로 관리
        registry.addInterceptor(new LoginCheckInterceptor()) // LoginCheckInterceptor를 등록대상 지정
                .order(1) // setOder 와 같은 기능, 숫자 작을수록 먼저 진행
                .addPathPatterns("/**") // 패턴지정 /** 는 모든경로와 그 하위경로까지 포함하는 패턴
                .excludePathPatterns("/", "/login", "/save", "/cass/**", "/js/**");
                // 전체 지정한 것 중에서, 이 안에 있는 경로들은 인터셉터 검사에서 제외
        System.out.println("Interceptor 실행");
        // Filter 는 필터코드 안에서 로직을 직접 짜야 하지만, Interceptor 는 제외 경로를 옵션으로 넘기기만 하면됨
        // interceptor 가 filter 보다 스프링에 친화적인 이유
    }

    // Resolver
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 커스텀 ArgumentResolver를 등록하고 싶으면 여기에 추가

        resolvers.add(new LoginUserArgumentResolver());
        // 스프링이 관리하는 리볼버 목록(resolvers)에, LoginUserArgumentResolver 추가
        // 이 과정이 있어야 리졸버가 처리할 수 있음
    }
}///
