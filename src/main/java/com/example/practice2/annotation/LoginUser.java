package com.example.practice2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 메서드 파라미터에만 붙일 수 있다는 제약
@Retention(RetentionPolicy.RUNTIME) // 프로그램 실행 중에도 이 어노테이션 정보가 유지되어야함
public @interface LoginUser {
    // 세션에 저장되어 있는 정보만을 가져올수 있음
    // ( 레포지토리에 있는 정보는 가져올수 없음 - 오직 세션에 있는 정보만 가져올 수 있음 )
//    @RequestParam 과 같이 새로운 정보( 세션에 저장되어있지 않은 정보) 는 @LoginUser 로 가져올수 없음
}///
