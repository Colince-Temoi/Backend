package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.HelloBean;

public class Test {

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		HelloBean helloBean = (HelloBean) context.getBean("helloBean");
		System.out.println(helloBean.sayHello());
	}

}
