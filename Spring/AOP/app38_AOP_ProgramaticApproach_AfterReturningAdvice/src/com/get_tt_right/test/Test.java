package com.get_tt_right.test;

import org.springframework.aop.framework.ProxyFactoryBean;

import com.get_tt_right.business.Bank;
import com.get_tt_right.service.LogAfterService;

public class Test {

	public static void main(String[] args) {
		
//		Create Target class object
		Bank b = new Bank();
		
//		Create Service class object
		LogAfterService las = new LogAfterService();
		
//		Create AOP proxy-ProxyFactoryBean-object
		ProxyFactoryBean pfb = new ProxyFactoryBean();
		
//		Combine Target + Service
		pfb.setTarget(b);
		pfb.addAdvice(las);
		
//		get returned Target proxy
		Bank bproxy = (Bank) pfb.getObject();
		
//		Access Bankproxy child class to Bank methods.
		int amt = bproxy.deposit(5000, "CBA123");
		System.out.println(amt);
		bproxy.anotherMethod();

	}

}
