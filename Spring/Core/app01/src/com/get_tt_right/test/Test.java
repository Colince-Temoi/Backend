package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.HelloBean;

public class Test {
//For Programs safety point of view: Use Throws Exception
	public static void main(String[] args) throws Exception {
		
/* When applicationContext.xml file is available under Src folder
 * Directly, we can use one argument[Xml file name] as below.
 * Make sure to import: ApplicationContext,ClassPathXmlApplicationContext,Instance classes[i.e.,HelloBean].
 * */
		// create and configure beans
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		// retrieve configured instance
		HelloBean hb= (HelloBean) context.getBean("helloBean");
		// use configured instance||Call the business method whatever you want to use.
		System.out.println(hb.helloUser());

	}

}
/* Summary||Important  NOTES||TIPS.
 * In this example, applicationContext.xml file I provided under src folder. Because of this reason: In Test application when preparing ApplicationContext Container,
   directly am giving the String "applicationContext.xml" [Spring Configuration file] as argument to ClassPathXmlApplicationContext
 * If I kept this Spring Configuration file under src>>resources folder||package, then the above way of passing the Spring configuration file to ClassPathXmlApplicationContext won't work.
   Happily you will get an Exception. To pass the Configuration file, we have to provide complete path of the Spring configuration file from src folder. EX:
   ApplicationContext context = new ClassPathXmlApplicationContext("/com/get_tt_right/resources/applicationContext.xml");
   This is what is always done for standards.[I tried it din't work for me, kept getting an exceptions, Consult how to bypass that]
 */