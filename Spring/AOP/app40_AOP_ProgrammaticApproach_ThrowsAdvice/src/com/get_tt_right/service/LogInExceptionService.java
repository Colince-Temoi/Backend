package com.get_tt_right.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.get_tt_right.business.Bank;
/*
 * This piece of code will execute in case if you give wrong accNo for deposit operation; nothing but in case of exceptions arising in any of the business||Target methods this 
   service is executing for.
 * Until unless an exception arises in Target class method(s), then this service will not be executed.
 */
public class LogInExceptionService implements org.springframework.aop.ThrowsAdvice {

	public void afterThrowing(Exception ex) {
		Log l = LogFactory.getLog(Bank.class);
		l.info("Excecuting in case of Exception in deposit method:");
	}

}
