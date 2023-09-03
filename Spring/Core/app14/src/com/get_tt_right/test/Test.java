package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.HelloBean;
import com.get_tt_right.beans.HiBean;
import com.get_tt_right.beans.WishBean;


public class Test {

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		HelloBean hellobean = (HelloBean) context.getBean("helloBean");
		System.out.println(hellobean.sayHello());
		
		WishBean wishBean= (WishBean) context.getBean("wishBean");
		System.out.println(wishBean.sayWish());
		
		HiBean hiBean= (HiBean) context.getBean("haiBean");
		System.out.println(hiBean.sayHi());
		

	}

}
