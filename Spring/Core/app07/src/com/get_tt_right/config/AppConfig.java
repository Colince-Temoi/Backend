package com.get_tt_right.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.get_tt_right.beans.HelloBean;
import com.get_tt_right.beans.WelcomeBean;

/*Configuration class-Java based configuration class
 * Annotate this class with @Configuration.
 * By using this annotation, we are telling the container that this class is acting as configuration class.
 * Configuration class in the sense, Java based configuration, we are not using xml based configuration again.
 * Inside this class, a number of methods we are going to maintain and those are returning respective bean objects.
 * 
 * Below are example methods we are going to use here:
 * We are naming them based on the bean objects we are having for our application.
 * welcomebean() method.
 * --------------------
 * - Returns WelcomeBean object.
 * 
 * To tell the container that this method is returning bean object, we need to use @Bean annotation on top of the method.
 * Summary.
 * ----------
 * - By providing @Configuration, we are saying to IOC container that this class is a configuration class.
 * - IOC container will create Object for this class.
 * - Internally, to get any bean object based on the respective configuration details in methods with @Bean annotation, it is able to execute the bean objects.
 * 
 * That's all about this class. You can go ahead and prepare Test class.
 * */
/*ALERT!!
 * Q. If I prepare 100 bean classes, how many methods do I need to prepare in AppConfig class if I need to manage 100 bean objects??
 * Ans. Definitely 100 methods I require in AppConfig.
 *    - It is in these methods as well I am going to set data to the respective beans.
 *  
 *NOTE: Each method annotated with @Bean annotation, MUST and MUST Return a bean object.
 *
 * */
@Configuration
public class AppConfig {

	
	static {
		System.out.println("AppConfig class loading....");
	}
	
	public AppConfig() {
		System.out.println("AppConfig class instantiation...");
	}
	
	@Bean
	public WelcomeBean welcomeBean() {
		return new WelcomeBean();
	}
	
	@Bean
	public HelloBean helloBean() {
//		Creating bean object
		HelloBean hb = new HelloBean();
//	Setting data for bean Object properties.
		hb.setName("Colince");
//		Returning bean object.
		return hb;
	}
	
}
