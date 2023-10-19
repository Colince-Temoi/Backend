package com.get_tt_right.services;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.get_tt_right.business.Bank;

/* It's the MethodInterceptor(I), one of aopalliance that we need to implement our service from.
 * -If we need:
    I.	Object o : Targetproxy class
   II.	Object[] of target method input params &&
  III.	Method m : Target method
for performing inspection, how can we get them? Below is how;

    I.Object o = mi.getThis(); // Will return your Targetproxy class name. 
   II.Object param[] = mi.getArguments(); // It will return your target method inputs-that have been given||inputted||injected by the user.
  III.Method m = mi.getMethod(); // Will return the Target class method name.
Only for inspection the above will help us.

-	Inside this method; we are going to write the service code we require to execute before a given specified business class(Target class) method executes.
-	For the code that need to execute after a given specified business class(Target class) method executes we need to follow the following criteria:
      Call mi.proceed() method. On invoking this method:
      Execution will proceed to your specified business class(Target class) method code and execute that.
      Then it will return us the return value of the target method which we are going to store into Object super class.
      Immediately after this line of code, we can now go ahead and write code that need to execute after the given specified business class(Target class) method has executed.
      As the last line of code inside invoke(-) method you need to return the return value which we just upcasted into Object super class above.
-	In Filters, there is filter chain mechanism; FilterChain.doFilter() if you call||invoke:
     beforeDoFilter() method code will execute before Servlet code execution.
     afterDoFilter() method code will execute after Servlet code execution.
-	In the same way before your specified business||target class method execution; in the service||aspect bean class that implements MethodInterceptor(I), we can maintain code that will execute at that time||point.
-	When we invoke mi.proceed() method in the service||aspect bean class that implements MethodInterceptor(I), Execution will proceed to your specified business class(Target class) method code and execute that.
-	Then it will return us the Target class return value which we are going to store into Object super class.
-	Immediately after this line of code, we can now go ahead and write code that need to execute after the given specified business class(Target class) method has executed.
-	As the last line of code inside invoke(-) method you need to return the return value which we just upcasted into Object super class above.
-	For this example; am just going to maintain debugging print statements to indicate that everything will work as expected || am going two just implement a simple Log service.


 */

public class LogAroundService implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		
		/*
		 * Object[] params = mi.getArguments(); for ( Object object : params) {
		 * System.out.println(object); }
		 * 
		 * Object o = mi.getThis(); System.out.println("Businessproxy class:"+o);
		 * 
		 * Object o1 = mi.getClass(); System.out.println(o1);
		 */
		
//	Before code will go here
		System.out.println("---------------Before Target class method code execution-----------");
		Log l = LogFactory.getLog(Bank.class);
		l.info("Executing Service before Target class method -"+mi.getMethod()+"-  gets executed");
		
//		Proceed to executing Target class method
		Object returnval = mi.proceed();  // It will return us Target class method return value. Actually it is of int type; that will internally be typecasted into Integer by the assignment operator and then we are storing that into Object reference. 
		
//		After code will go here
		System.out.println("---------------After Service code execution-----------");
		l.info("Executing Service After Target class method -"+mi.getMethod()+"-  has executed");

		return returnval;

	}

}
