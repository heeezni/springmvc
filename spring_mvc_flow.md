# Spring MVC 요청 처리 흐름: 어노테이션, 컨트롤러, 포워딩, 리다이렉트

Spring MVC에서 웹 요청이 어떻게 처리되는지, 그리고 그 과정에서 핵심 개념들이 어떻게 사용되는지 단계별로 설명합니다.

## 전체 흐름 요약

1.  **사용자 요청**: 브라우저에서 특정 URL을 클릭하거나 주소를 입력합니다. (예: `/notice/list`)
2.  **`DispatcherServlet` 수신**: Spring의 중앙 관제소인 `DispatcherServlet`이 모든 요청을 가장 먼저 받습니다.
3.  **`HandlerMapping`**: `DispatcherServlet`은 요청된 URL( `/notice/list` )을 처리할 수 있는 **컨트롤러**가 누구인지 `HandlerMapping`에게 물어봅니다.
4.  **`@Controller` & `@RequestMapping`**: `HandlerMapping`은 **`@Controller`** 어노테이션이 붙은 클래스들을 뒤져서, **`@RequestMapping("/notice/list")`** 와 같이 요청 URL과 일치하는 **어노테이션**이 붙은 메서드를 찾아냅니다.
5.  **컨트롤러 메서드 실행**: 찾은 컨트롤러(예: `NoticeController`)의 해당 메서드가 실행됩니다. 이 메서드는 비즈니스 로직을 처리합니다.
6.  **View 또는 Redirect 결정**: 메서드는 로직 처리 후, 사용자에게 무엇을 보여줄지 결정합니다.
    *   **포워딩 (Forwarding)**: 사용자에게 특정 화면(JSP)을 보여주고 싶을 때 사용합니다.
    *   **리다이렉트 (Redirect)**: 사용자를 완전히 다른 URL로 보내버리고 싶을 때 사용합니다.
7.  **`ViewResolver`**: 포워딩의 경우, 컨트롤러가 반환한 뷰 이름(예: `"notice/list"`)을 가지고 `ViewResolver`가 실제 JSP 파일의 경로(예: `/WEB-INF/views/notice/list.jsp`)를 찾아냅니다.
8.  **응답 (View 렌더링)**: `DispatcherServlet`은 찾아낸 JSP 파일을 실행(렌더링)하여 생성된 HTML을 사용자 브라우저에게 최종 응답으로 보냅니다.

---

## 1. 어노테이션 (Annotation)

-   **의미**: '주석'이라는 뜻이지만, 코드에 특별한 의미나 기능을 부여하는 역할.
-   **핵심 역할**: Spring에게 "이 클래스는 컨트롤러야", "이 메서드는 이 URL을 처리해" 라고 알려주는 표지판.
-   **주요 어노테이션**:
    -   `@Controller`: "이 자바 클래스는 웹 요청을 처리하는 컨트롤러입니다." 라고 Spring에 등록합니다.
        -   **예시**: `NoticeController.java` 파일 위에 `@Controller` 라고 적혀있을 겁니다.
    -   `@RequestMapping("/some/url")`: 특정 URL과 컨트롤러의 메서드를 연결합니다.
        -   `@GetMapping`, `@PostMapping` 등 특정 HTTP 요청 방식(GET, POST)에만 반응하도록 세분화할 수 있습니다.
        -   **예시**: `NoticeController` 클래스 내부의 특정 메서드 위에 `@GetMapping("/list")` 와 같이 사용되어, 사용자가 `/notice/list` 로 GET 요청을 보낼 때 해당 메서드가 실행되도록 합니다.

## 2. 컨트롤러 (Controller)

-   **의미**: MVC 패턴의 'C'. 실제 웹 요청을 받아서 처리하는 객체.
-   **핵심 역할**:
    1.  사용자 요청을 받습니다 (어노테이션 덕분에).
    2.  요청에 필요한 데이터 처리, 계산 등 비즈니스 로직을 수행합니다.
    3.  로직 처리 후, 사용자에게 어떤 화면을 보여줄지(포워딩) 또는 어떤 다른 페이지로 보낼지(리다이렉트) 결정하고 그 정보를 Spring에게 반환합니다.
-   **예시**: `NoticeController.java` 파일 자체가 하나의 컨트롤러입니다. 그 안에는 공지사항 목록 보기, 글쓰기, 글 내용 보기 등의 요청을 처리하는 각각의 메서드들이 들어있습니다.

## 3. 포워딩 (Forwarding)

-   **의미**: "요청 위임". 서버 내부에서 요청을 다른 자원(주로 JSP)에게 넘겨서 처리하게 하는 것.
-   **핵심 특징**:
    -   **서버 내부에서만 이동**하므로 사용자의 브라우저 URL은 바뀌지 않습니다.
    -   요청(Request)과 응답(Response) 객체가 유지되므로, 컨트롤러에서 생성한 데이터를 JSP로 넘겨서 화면에 표시할 수 있습니다. (예: `model.addAttribute("noticeList", ...)` )
-   **사용법**: 컨트롤러 메서드가 **문자열(String)로 뷰 이름을 반환**합니다.
    -   **예시**: `return "notice/list";`
    -   이렇게 반환하면 Spring의 `ViewResolver`가 설정에 따라 `"/WEB-INF/views/notice/list.jsp"` 와 같은 실제 파일 경로를 찾아 그 JSP를 실행하고, 그 결과를 사용자에게 보여줍니다.

## 4. 리다이렉트 (Redirect)

-   **의미**: "재요청 지시". 서버가 클라이언트(브라우저)에게 "이 주소로 다시 요청해" 라고 응답을 보내는 것.
-   **핵심 특징**:
    -   브라우저는 서버의 지시에 따라 **새로운 URL로 실제로 다시 요청**을 보냅니다. 따라서 **브라우저의 주소창 URL이 바뀝니다.**
    -   완전히 새로운 요청이므로, 이전 요청의 데이터(Request 객체)는 유지되지 않습니다.
-   **사용법**: 컨트롤러 메서드가 반환하는 문자열 앞에 **`"redirect:"`** 접두사를 붙입니다.
    -   **예시**: `return "redirect:/notice/list";`
    -   주로 데이터에 변화가 생기는 요청(글쓰기, 수정, 삭제 등 POST 방식) 처리 후에 사용합니다. 왜냐하면 사용자가 새로고침(F5)했을 때 이전 요청(글쓰기)이 다시 실행되는 것을 방지할 수 있기 때문입니다. (이를 **PRG 패턴** - Post-Redirect-Get 이라고 합니다.)
