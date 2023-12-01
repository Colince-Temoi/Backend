package com.get_tt_right.services;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

import com.get_tt_right.business.Bank;

public class LogIncaseOfExceptionService implements ThrowsAdvice {
	
	/*
	 * public void afterThrowing(Exception ex) { // logging service we are
	 * implementing here Log l = LogFactory.getLog(Bank.class);
	 * l.info("Executing in case of Exception in Target class method"); }
	 */
	
	public void afterThrowing(Method m, Object[] params, Object o, Exception ex) {
//		logging service we are implementing here
		 Log l = LogFactory.getLog(Bank.class);
		 l.info("Executing in case of Exception in Target class method: "+m.getName());
//		 The exception message we are printing here
		 System.out.println(ex);
	}
	
}
