Session ( 세션 )

세션 : 서버가 특정 사용자의 상태(state)를 일정 시간 동안 서버or 저장소에 저장하고 관리하는 기술

HTTP 는 기본적으로 Stateless( 무상태 ) 프로토콜 이므로, 서버는 요청이 끝나면 사용자가 누구인지 기억X 이 문제를 해결하기위해 Session 사용

HTTP 문제점 - 로그인이 끝난 상태에서 페이지 이동 시 로그인한 사용자의 요청인지 구분이 안된다. 왜냐면 HTTP 는 요청 하나하나를 완전히 독립적으로 처리하기 때문

Session ID - 사용자의 세션을 구별하기 위한 고유한 문자열 - 서버에서 랜덤으로 생성

세션에는 최소한의 정보만 저장을 권장

Session 사용하는 경우

로그인 상태 유지
관리자 로그인
장바구니
사용자 권한 관리
인증 정보 저장
일시적인 사용자 상태 유지
Sesion 사용하지 않는 경우

REST API 서버
JWT 기반 인증
모바일 API 서버
서버 간 인증
==========================
Spring MVC + Thymeleaf 정리
==========================

1. NoSuchMethodError 발생

에러
--------------------------------
NoSuchMethodError:
User.<init>(String, String)

원인
--------------------------------
Repository.class는
new User(String, String)
생성자를 호출하고 있는데,

User.java는
User(String, String, String)
생성자만 가지고 있었다.

즉, 소스와 컴파일된 .class 파일의 버전이 달랐다.

해결
--------------------------------
- Build → Rebuild Project
- 안 되면 out 폴더 삭제
- 필요하면 Invalidate Cache


=================================================

2. Thymeleaf TemplateInputException

에러
--------------------------------
Error resolving template [save]

원인
--------------------------------
return "save";

를 했지만

templates/save.html

파일이 존재하지 않았다.

또는 위치가 잘못되었다.

해결
--------------------------------
src/main/resources/templates/save.html

파일이 존재하는지 확인

또는

return "redirect:/..."

를 사용해야 하는 상황인지 확인


=================================================

3. 로그인이 되지 않았던 이유

잘못된 코드
--------------------------------

if(loginId.isEmpty() && password.isEmpty())

의미

아이디와 비밀번호가 모두 비어있을 때만 로그인 수행

정상 코드
--------------------------------

if(!loginId.isEmpty() && !password.isEmpty())

또는

if(loginId.isBlank() || password.isBlank()){
...
}


=================================================

4. redirect 후 Model이 사라지는 이유

로그인

model.addAttribute("loginUser", user);

return "redirect:/mypage";

문제
--------------------------------

redirect가 발생하면

새로운 HTTP 요청이 만들어진다.

따라서 Model 데이터는 모두 사라진다.

Model은 redirect를 넘어서 전달되지 않는다.


=================================================

5. 로그인 정보는 Session 사용

로그인 성공

session.setAttribute("sessionLogin", user);

마이페이지

User user =
(User)session.getAttribute("sessionLogin");

model.addAttribute("loginUser", user);

return "mypage";


=================================================

6. Thymeleaf에서 사용자 출력

Controller

model.addAttribute("loginUser", user);

html

<p th:text="${loginUser.nickname} + ' 님 환영합니다.'"></p>


또는 Session 직접 사용

<p th:text="${session.sessionLogin.nickname} + ' 님 환영합니다.'"></p>


=================================================

7. 변수 이름은 반드시 동일해야 한다.

예)

Controller

model.addAttribute("loginUser", user);

HTML

${loginUser.nickname}

가능

--------------------------

Controller

model.addAttribute("loginUser", user);

HTML

${loginedUser.nickname}

불가능

변수명이 다르기 때문


=================================================

8. redirect:/mypage 에서 404가 나는 이유

redirect 자체는 정상

404는

GET /mypage

를 처리하는 Controller가 없다는 의미

반드시 있어야 한다.

@GetMapping("/mypage")
public String mypage(...) {
...
return "mypage";
}


그리고

templates/mypage.html

도 존재해야 한다.


=================================================

9. Map으로 로그인 구현

Repository

private final Map<String, User> map = new HashMap<>();

회원가입

map.put(loginId, user);

로그인

User user = map.get(loginId);

if(user == null)
아이디 없음

if(!user.getPassword().equals(password))
비밀번호 틀림

return user;


주의

UserRepository를 직접 new 하면 안 된다.

Spring의

@Repository
@Service

를 이용하여 DI(의존성 주입)를 사용해야
같은 Map 객체를 공유한다.


=================================================

10. 현재 프로젝트 로그인 흐름

회원가입

POST /join
↓
Repository(Map)에 저장
↓

로그인 페이지

GET /login
↓

로그인

POST /login
↓
Repository에서 사용자 조회
↓
비밀번호 검사
↓
HttpSession 저장

session.setAttribute("sessionLogin", user)

      ↓

redirect:/mypage

      ↓

@GetMapping("/mypage")

      ↓

Session에서 User 가져오기

User user =
(User)session.getAttribute("sessionLogin");

      ↓

Model에 저장

model.addAttribute("loginUser", user);

      ↓

mypage.html

${loginUser.nickname}

출력

==========================
HttpSession 핵심 정리
==========================

1. Session이란?
- 서버에 사용자 정보를 저장하는 공간이다.
- 로그인 상태를 유지하기 위해 사용한다.
- 브라우저는 JSESSIONID라는 쿠키만 가지고 있고, 실제 데이터는 서버의 Session에 저장된다.

예)
브라우저
JSESSIONID = ABC123

↓

서버
ABC123 → HttpSession
loginId = kim
name = 홍길동

------------------------------------------------

2. session.invalidate()란?
- 서버의 HttpSession을 삭제한다.
- 브라우저의 JSESSIONID 쿠키가 바로 없어지는 것은 아니다.
- 따라서 개발자도구에서 JSESSIONID가 보여도 서버에는 Session이 이미 삭제되어 있을 수 있다.

즉,
invalidate() = 서버 Session 삭제
쿠키 삭제 = 별개의 동작

------------------------------------------------

3. 페이지를 이동해도 Session이 유지되는 이유
   로그인
   ↓
   MyPage
   ↓
   MyPage2
   ↓
   MyPage3

브라우저는 같은 JSESSIONID를 계속 보내고,
서버는 같은 Session을 찾아 사용한다.

------------------------------------------------

4. Controller에서는 왜 HttpSession을 또 받아야 하나?

Controller는 요청(Request)이 올 때마다 새로 실행된다.

따라서 현재 사용자의 Session을 가져오기 위해

HttpSession session

또는

HttpServletRequest request

를 매 요청마다 받아야 한다.

예)

String loginId =
(String) session.getAttribute("loginId");

------------------------------------------------

5. 화면(JSP/Thymeleaf)에 출력하려면?

Session의 값을 바로 화면에서 사용할 수 있는 것이 아니다.

반드시

Session
↓ getAttribute()
Controller
↓ addAttribute()
Model
↓
JSP / Thymeleaf

순서로 전달해야 한다.

예)

String loginId =
(String) session.getAttribute("loginId");

model.addAttribute("loginId", loginId);

JSP
${loginId}

------------------------------------------------

6. 페이지가 많으면?

매 Controller마다

session.getAttribute("loginId");
model.addAttribute("loginId", loginId);

를 반복하게 된다.

그래서 실무에서는

- @SessionAttribute
- @ControllerAdvice + @ModelAttribute
- HandlerInterceptor
- Spring Security

등을 사용하여 반복을 줄인다.

------------------------------------------------

7. 핵심 흐름

로그인 성공
↓
Session 생성
↓
session.setAttribute("loginId", "kim")
↓
페이지 이동
↓
Controller
↓
session.getAttribute("loginId")
↓
model.addAttribute("loginId", loginId)
↓
JSP / Thymeleaf에서 ${loginId} 출력

------------------------------------------------

★ 가장 중요한 한 줄

Session은 서버에 저장되고,
Controller는 요청마다 Session에서 값을 꺼내 Model에 담아 화면으로 전달한다.

---------------------------------------------------------------------
순수 서블릿 세션 실습 정리노트

---

## Q1. 서블릿 관련 내용이 없어 보인다. HttpSession 메서드만 있으면 세션 로그인이 가능해 보이는데?

**답변**
- `HttpSession`, `HttpServletRequest`는 그 자체가 **서블릿 API**(`jakarta.servlet.http` 패키지)다. 별도의 "서블릿 파트"가 추가로 있는 게 아니라, 지금 쓰고 있는 세션 메서드 자체가 이미 서블릿 스펙이다.
- 진짜 "순수 서블릿" 경험을 하려면 스프링의 `@Controller` 없이 `HttpServlet`을 직접 상속받아 `doGet()`/`doPost()`를 오버라이드하는 방식으로 짜야 한다. 그래야 스프링이 내부적으로 대신 해주던 부분이 뭔지 체감할 수 있다.

**순수 서블릿 코드 예시**
```java
@WebServlet("/session-test")
public class SessionTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        Integer count = (Integer) session.getAttribute("visitCount");
        count = (count == null) ? 1 : count + 1;
        session.setAttribute("visitCount", count);

        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().write("세션 ID: " + session.getId() + "\n방문 횟수: " + count);
    }
}
```
- 스프링 부트에서 `@WebServlet`을 인식시키려면 메인 클래스에 `@ServletComponentScan` 추가 필요.

---

## Q2. getSession()을 어떻게 써야 하는지 감이 안 잡힌다.

**답변**
- `getSession()`은 단독 메서드가 아니라 **`HttpServletRequest` 객체의 메서드**다. 반드시 `request.getSession()` 형태로 호출한다.
- `request` 객체는 직접 만드는 게 아니라, 요청이 들어올 때 서블릿 컨테이너(톰캣)가 자동으로 생성해서 넘겨준다.
- 스프링 `@Controller`에서도 메서드 파라미터에 `HttpServletRequest request`를 선언만 하면 스프링이 실제 request 객체를 자동 주입한다. 스프링을 쓰든 안 쓰든 이 객체 자체는 톰캣이 만드는 동일한 물건이다.

**스프링 컨트롤러 버전 비교 코드**
```java
@RestController
public class SessionTestController {

    @GetMapping("/session-test2")
    public String test(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer count = (Integer) session.getAttribute("visitCount2");
        count = (count == null) ? 1 : count + 1;
        session.setAttribute("visitCount2", count);

        return "세션 ID: " + session.getId() + " / 방문 횟수: " + count;
    }
}
```

---

## Q3. 실행 가능한 순서로 다시 정리해달라 (감이 안 잡힘)

**실행 절차**

1. 메인 클래스에 `@ServletComponentScan` 추가
```java
@SpringBootApplication
@ServletComponentScan
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```
2. `SessionTestServlet.java` 작성 (위 Q1 코드)
3. 프로젝트 실행
4. 브라우저에서 `http://localhost:8080/session-test` 접속 → "세션 ID / 방문 횟수" 출력 확인
5. F5 새로고침 여러 번 → 방문 횟수가 1→2→3으로 증가하는지 확인 (세션 유지 확인)
6. 크롬 개발자도구(F12) → Application → Cookies → `http://localhost:8080` → `JSESSIONID` 값이 화면에 출력된 세션 ID와 같은지 대조
7. `JSESSIONID` 쿠키를 개발자도구에서 삭제 → 새로고침 → 방문 횟수가 1로 초기화되고 세션 ID도 바뀌는지 확인 (새 세션 발급 확인)
8. `SessionTestController.java` 작성 (Q2 코드) → `http://localhost:8080/session-test2`에서 동일 과정 반복 → 서블릿 방식과 스프링 방식이 똑같이 동작함을 비교 확인

---

## Q4. 이 실습을 왜 하는 건지 모르겠다. 로그인은 그냥 HttpSession으로 setAttribute/invalidate 하면 되는 거 아닌가?

**답변 — 실습 목적 3가지**

1. **`getSession()`이 스프링과 무관하게 항상 동일하게 동작함을 확인**
    - `@WebServlet` 방식과 `@RestController` 방식 모두 결국 `request.getSession()`을 호출. 세션은 스프링이 만든 기능이 아니라 서블릿 컨테이너(톰캣)가 제공하는 기능이고, 스프링은 이를 가져다 쓸 뿐임을 확인.

2. **눈에 안 보이던 JSESSIONID 쿠키를 직접 확인**
    - 1단계에서 이론으로만 배운 "세션 ID가 쿠키로 전달된다"를, 개발자도구에서 실제 값으로 코드 출력값과 대조하며 확인.

3. **세션이 "요청 간 유지된다"는 것을 체감**
    - 새로고침마다 방문 횟수가 올라간 것 = HTTP가 Stateless인데도 서버가 같은 사용자를 인식하는 원리(1단계 개념)를 직접 목격한 것.
    - 쿠키 삭제 시 세션 ID가 바뀌고 카운트가 초기화된 것도 같은 맥락.

**질문 답변 — "로그인은 그냥 setAttribute + invalidate로 되는 거 아니냐"**
- 맞음. 실제 로그인 구현이 정확히 이 패턴이다.
    - 로그인 성공: `session.setAttribute("loginMember", member)`
    - 로그아웃: `session.invalidate()`
- 방금 실습한 `visitCount` 예제는 이 로그인 로직과 기능적으로 동일한 패턴이며, `visitCount` 자리에 실제 회원 정보(Member 객체)가 들어가는 것이 3단계다. 즉 2단계는 3단계에서 쓸 코드 패턴을 로그인이라는 복잡한 맥락 없이 미리 단순화해서 연습한 것.

---

## Q5. doGet과 doPost는 무슨 차이인가?

**답변**

- 브라우저가 `GET`으로 요청 → 서블릿의 `doGet()` 실행
- 브라우저가 `POST`로 요청 → 서블릿의 `doPost()` 실행
- `HttpServlet` 상속 시 `doGet`, `doPost`, `doPut`, `doDelete` 등을 오버라이드해서 메서드별로 다른 로직 작성 가능

**GET vs POST 비교**

| 구분 | GET | POST |
|---|---|---|
| 용도 | 데이터 조회 | 데이터 생성/변경 |
| 데이터 위치 | URL 쿼리 파라미터 (`?key=value`) | HTTP 요청 본문(Body) |
| 데이터 노출 | URL에 그대로 보임(주소창/히스토리/서버로그) | Body에 담겨 URL엔 안 보임 |
| 데이터 크기 | 제한적 | 상대적으로 큰 데이터 가능 |
| 캐싱 | 브라우저 캐싱 가능 | 기본적으로 캐싱 안 됨 |
| 재요청 | 주소만으로 재요청 가능(북마크 등) | 재제출 경고 뜨는 경우 많음 |

**로그인과의 연결**
- 로그인 폼 제출은 반드시 **POST**를 써야 함. `method="get"`으로 만들면 `http://localhost:8080/login?userId=abc&password=1234`처럼 비밀번호가 URL에 그대로 노출되어 브라우저 히스토리·서버 로그에 평문으로 남음.
- 방금 실습에서 `doGet()`만 오버라이드했기 때문에 주소창 입력만으로 테스트가 가능했던 것. `doPost()`만 있었다면 폼 제출이나 Postman 같은 도구가 필요.

**스프링과의 대응 관계**
- `@GetMapping` ↔ 서블릿의 `doGet()`
- `@PostMapping` ↔ 서블릿의 `doPost()`
- 스프링에서는 `HttpServlet`을 직접 상속하지 않아도, `DispatcherServlet`이 요청의 GET/POST를 보고 알맞은 어노테이션이 붙은 컨트롤러 메서드로 라우팅해준다.

---


Filter vs Interceptor

클라이언트 요청
↓
[WAS(톰캣) — 서블릿 컨테이너]
↓
Filter        ← 여기서 동작 (스프링 컨텍스트 진입 전)
↓
[DispatcherServlet — 스프링의 진입점]
↓
Interceptor    ← 여기서 동작 (스프링 컨텍스트 안, 컨트롤러 직전)
↓
Controller

Filter 는 톰캣(서블릿 컨테이너) 레벨에서 동작, 요청이 스프링 영역(dispatcherServlet)에 들어오기전에 가로챔
Interceptor 는 DispatcherServlet 이 요청을 받은 이후, 컨트롤러로 넘기기 직전에 동작(스프링 컨텍스트 안에서 동작)

Servlet container vs dispatcherServlet
servlet container(톰캣) 는 요청을 받아 알맞은 서블릿을 실행 시켜주는 실행 환경 자체
dispatcher servlet 은 container 안에 스프링이 등록해 놓은 하나의 servlet

비유
서블릿 컨테이너는 건물
dispatcherServlet 은 건물안의 안내 데스크 같은 느낌


서블릿 컨테이너(ex 톰캣) 들을 좀 더 큰 범주에서 WAS(Web Application Server) 라고 부름
WAS 는 정적파일에서는 필요없음 동적 파일들을 처리하기 위해 필요한것


SecurityFilterChain
- 인증, 인가, CSRF 방어, 세션관리 등을 각각 담당하는 여러 개의 filter를 자동으로 체인처럼 연결해서 요청 처리
- 사용 시 아이디는 user 고정값, password 는 콘솔에 나옴 

CSRF ( cross-site request forgery, 사이트 간 요청 위조)
정의
- 사용자가 로그인된 상태를 이용해서, 공격자가 사용자 몰래 원치 않는 요청을 서버에 보내게 만드는 공격
- 다른(악성) 사이트가, 사용자를 대신해서 진짜 사이트로 가짜 요청을 위조해서 보내는것
- CSRF 토큰은 서버가 세션마다 발급하는, 외부에서 알수 없는 랜덤값
공격 성립조건
- 이미 로그인되어 세션이 살아있는 상태
- 그 상태로 공격자가 만든 다른 페이지를 방문
- 그 페이지가, 사용자 브라우저를 통해 우리 사이트로 몰래 요청 보냄
- 브라우저는 요청을 보낼 때 쿠키(세션정보)를 자동으로 함께 실어 보내므로, 서버 입장에서는 "로그인된 진짜 사용자가 보낸 요청으로 보임"