package com.get_tt_right.test;

import org.springframework.aop.framework.ProxyFactoryBean;

import com.get_tt_right.business.Bank;
import com.get_tt_right.services.LogAroundService;

public class Test {

	public static void main(String[] args) {
//		Step1: Create Target class object
		Bank b = new Bank();
		
//		Step2: Create Service class object
		LogAroundService las = new LogAroundService();
		
//		Step3: Combine Target + Service
//		Step3.1: Create AOP proxy class object
		ProxyFactoryBean pfb = new ProxyFactoryBean();
		
//		3.2: Add Target to AOP
		pfb.setTarget(b);
		
//		3.3: Add Service to AOP
		pfb.addAdvice(las);
		
//		Step4: get the weaved Targetproxy object
		Bank bproxy = (Bank) pfb.getObject();
		
//		Invoke Business||Target methods
		int amt = bproxy.deposit(5000, "CBA123");
		bproxy.anotherMethod();
	}

}
