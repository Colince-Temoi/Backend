package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.get_tt_right.beans.HelloBean;
import com.get_tt_right.beans.WelcomeBean;
import com.get_tt_right.config.AppConfig;

/* AnnotationConfigApplicationContext object we need to prepare.
 * - This is just an implementation class to ApplicationContext.
 * - As an argument, AppConfig.class file we need to provide during object creation of the container class.
 * - Whenever we provide this AppConfig.class file, it will be:
 *   -> Loaded and instantiated by AnnotationConfigApplicationContext.
 *   -> Later we will see the flow of execution in detail.
 * - Finally, we can access and utilize bean objects as per our requirement.
 * - To do this we are going to utilize getBean method. To it we can:
 *   -> Pass the method name of respective bean configuration in AppConfig class as parameter to this getBean method.
 *      This will act as id attribute in XML based configuration, just the same way as we used to see in XML based configuration.
 *      After getting the required bean, then we can be able to access its business methods and data at free will.
 *   -> Pass bean.class file. I.e., WelcomeBean.class as a parameter to this getBean method.
 *      This is the second alternative to access the bean object.
 *      For this, we no need to use casting. Directly without any casting we are able to access the bean object.
 *      If you perform casting, okay, you wont get an error either.
 * - NOTE: In addition to the Spring core libraries we have been using up to app06, for this app07 application we also need to add AOP library, else
 *         happily you are going to get an error if you try to execute this program while just only maintaining the previous Spring Libraries we've been using up to app06.
 *         
 * - To provide any Java based configurations, internally we require AOP. So make sure to add it to your User defined Spring library.
 * 
 * - Default scope is still in action for this application. Singleton scope that is.
 * - Means nothing but, even though we are calling this getBean() method a number of times,
 *   In all those n times, pointing is to the same bean object reference.
 * - In place of XML document, we are using Java based configuration, but what exactly the features which are provided by XML document same features are provided in this approach also.
 * - Ex: I am totally against this XML technology, I don't want to use it. REASONS:
 *     -> Myself I'am a Java developer.
 *     -> To achieve this, Spring framework has given an analysis with respect to Java development[Java based configuration], like no need to provide configuration details through an XML file here. We can prepare Java classes and we can go for respective content.
 *     -> Moreover, If you are using XML document for configurations, internally we are going to have a number of parsers or internally some procedure is available[XML document lifecycle is available.]
 *     -> But if it is a Java class, it is a matter of just 2 to 3 lines of code, we can provide that and we can go for the respective bean objects.
 *     
 * 
 * Flow of Execution.
 * ------------------
 * Q. When are the respective bean class objects created when we utilize Java based configurations?
 * Steps to prove:
 * ----------------
 *  For each bean class, prepare a static block, a default and a default constructor.
 *  As we know already:
 *  Static block-->Is executed during loading phase.
 *              -->In the body print a message like, Welcome bean loading
 *  Constructors --> Executed during instantiation. Okay we know already from core java that without constructors we cannot create objects.
 *               -->In the body print a message like, Hello bean instantiating
 *  In Test application, just create container only. Comment out rest other code and run project.
 * Ans. It is clear that Objects for bean classes will be ready||created at the time of container creation.
 * The flow will be as follows:
 * - AppConfig class loading and instantiation will happen first
 * - The various configured beans will load and execute next. Nothing but which methods are marked with @Bean inside AppConfig class.
 *   Those methods will execute, and by the execution of those methods, bean objects will be created with respective data.
 *   Bean object will be returned and stored in ApplicationContext with key as the method name used to configure the bean in AppConfig class.
 *   In the form of key value pair ApplicationContext is able to store all the bean objects where: Values are bean objects whatever we returned from the method and key are the respective method names. 
 * */
/*To display all the configured bean names, we have a method: getBeanDefinitionNames().
 *Using that we can be able to prove that the bean names are the respective method names used in AppConfig class during bean configuration.
 *Those will act as the identity||key of respective bean objects when stored inside ApplicationContext.
 *Using these names as parameters to getBean() method, we can be able to access the respective bean objects.
 * 
 * */
/*NOTICE.
 * The market is moving towards Java based configurations. People are trying to reduce XML dependency as much as possible.
 * Because of that reason, Java configurations and annotations are available.
 * In the case of Spring framework, even though if you use annotations, little bit XML technology||content is required their.
 * But if you are using Java based configurations, total XML, even the word, "xml" we can remove from our Spring applications.
 * */
public class Test {

	public static void main(String[] args) {
		
		 ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		 
//		 String[] beanNames = ctx.getBeanDefinitionNames();
//		 for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}
		 
		 
//		 WelcomeBean welBean1 = (WelcomeBean) ctx.getBean("welcomebean");
//		 System.out.println(welBean1.getWelcomeMessage());
		 
//		 WelcomeBean welBean2 = ctx.getBean(WelcomeBean.class);
//		 System.out.println(welBean2.getWelcomeMessage());
//		 
//		 System.out.println(welBean1);
//		 System.out.println(welBean2);
////		 Proof for default scope in action. Singleton that is.
//		 System.out.println(welBean1==welBean2);

//		 HelloBean helloBean = (HelloBean) ctx.getBean("helloBean");
//		 System.out.println(helloBean.sayHello());
	}

}
