
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

