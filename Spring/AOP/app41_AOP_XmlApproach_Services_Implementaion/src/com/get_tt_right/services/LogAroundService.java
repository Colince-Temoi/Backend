package com.get_tt_right.services;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.get_tt_right.business.Bank;

public class LogAroundService implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
//		logging service we are implementing here
		 Log l = LogFactory.getLog(Bank.class);
		 l.info("Executing before target class method: "+mi.getMethod()+" in case of Around Advice");
		 
//		 Proceed to executing target class method
		 Object returnval = mi.proceed();
		 
//			logging service we are implementing here
			 l.info("Executing after target class method: "+mi.getMethod()+" in case of Around Advice");
			 
		return returnval;
	}

	
	
}
