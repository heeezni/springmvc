package springmvc.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * xml로 스프링이 관리할 빈을 설정하는 방식X
 * Java 클래스 기반 설정 방식
 * 
 * prefix와 suffix는 컨트롤러가 반환한 뷰 이름에 앞뒤로 붙는 경로/확장자 설정
 * */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"springmvc.controller"}) // 이 패키지의 @Component어노테이션이 붙은 모든 것을 메모리에 올린다
public class WebConfig {
	
	/*
	 * <bean class="~~IngernalResourceViewResolver" id="resolver">
	 * 		<property name="prefix" value=""> 접두어
	 * 		<property name="suffix" value=""> 접미어
	 * </bean>
	 * */
	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		// 하위 컨트롤러들이 Dispatcher에게 뷰의 이름을 반환하면,
		// DispatcherServlet은 이 이름을 사용하여 jsp 페이지를 조합하게 됨
		
		InternalResourceViewResolver viewResolver=new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	
	
	
}
