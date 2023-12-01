package com.get_tt_right.services;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import com.get_tt_right.business.Bank;

public class LogAfterService implements AfterReturningAdvice {

	@Override
	public void afterReturning(Object returnval, Method m, Object[] params, Object o) throws Throwable {
//		logging service we are implementing here
		 Log l = LogFactory.getLog(Bank.class);
		 l.info("Executing after target class method: "+m.getName());		
	}

	
}
