package com.get_tt_right.test;

import org.springframework.aop.framework.ProxyFactoryBean;

import com.get_tt_right.business.Bank;
import com.get_tt_right.service.LogInExceptionService;

public class Test {

	public static void main(String[] args) {
//		Create Target||Business class object
		Bank b = new Bank();
	
//		Create Service class object
		LogInExceptionService lies = new LogInExceptionService();
		
//		Create AOP proxy predifine class object
		ProxyFactoryBean pfb = new ProxyFactoryBean();
		
//		Add Target to aop
		pfb.setTarget(b);
		
//		Add Service to aop
		pfb.addAdvice(lies);
		
//		get final weaved Businessproxy object
		Bank bproxy = (Bank) pfb.getObject();
		
//		Invoke business methods
		int amt = bproxy.deposit(100,"CBA1232");
		System.out.println(amt);
	}

}
