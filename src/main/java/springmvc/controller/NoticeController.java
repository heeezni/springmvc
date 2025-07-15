package springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

// 스프링에서 관리하는 클래스를 가리켜 빈이라고 하며,
// 빈들을 대상으로 어떤 역할을 수행하는 관점에서 지칭되는 용어로 컴포넌트가 있다.

@Slf4j
@Controller
public class NoticeController {
	
	@RequestMapping("/notice/list")
	public ModelAndView selectAll() {
		// Model에는 개발자가 저장할 데이터를 저장
		// request.setAttribute("key", value);
		
		// View DispatcherServlte이 조합할 뷰의 이름
		// 접두어 접미어
		ModelAndView mav=new ModelAndView();
		mav.addObject("list", "게시물 목록"); // request.setAttribute("list", "게시물 목록"); 과 동일
		mav.setViewName("notice/list");
		
		return mav;
	}
	
	// 글쓰기 폼 요청 처리 (원래 jsp를 webapp바로 밑에 두었다면, 매핑이 필요없음 (직접 접근 가능)
	// 하지만 spring mvc에서는 jsp마저도 직접 접근을 막고 컨트롤러 매핑으로 처리함)
	@RequestMapping("/notice/registform")
	public String registForm() {
		return "notice/write";
	} // localhost:7777/shop/notice/registform
	
	
	// 메서드 호출 후, 반환할 값이 없을 때, 즉 저장할 것이 없을 때는
	// ModelAndView 중 View만 반환하면 되므로, 이때는 String으로 대체해도 됨
	// ex) notice/detail => String 넘겨받은 DispatcherServlet이 WEB-INF/views/notice/detail.jsp로 조합함
	@RequestMapping(value="/notice/regist", method=RequestMethod.POST)
	public String regist() {
		
		log.debug("글쓰기 요청 받음");
		
		// 개발자가 redirect를 명시하지 않으면, 스프링은 디폴트 forward임
		return "redirect:/shop/notice/list";
		
	}
}
