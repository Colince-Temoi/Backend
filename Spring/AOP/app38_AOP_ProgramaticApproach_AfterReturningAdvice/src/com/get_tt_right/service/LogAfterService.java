package com.get_tt_right.service;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;

import com.get_tt_right.business.Bank;

/* Implementation of service class by implementing AfterReturningAdvice(I) interface
 * In AfterReturningAdvice(I) interface, there is a method: afterReturning(-,-,-,-) method.
    -This method has a return type: void.
    -Takes 4 inputs||arguments:
        -	Takes 4 inputs||arguments:
             1. Object returnval  - This first one takes the return value.
                     Nothing but the return value from a Target method will be stored here and we can use it as we wish.
                     For me am just simply printing it.
             2. Method m. i.e., deposit method
             3. Object array of Method input parameters; Object[] parameters. i.e., deposit method inputs
             4. Object o - Holds Bankproxy object. 

    - Now, what is the need of the input parameters: Object o, Method m and Object array of Method input parameters; Object[] parameters?
        +. In case if your service requires your Method(afterReturning(-,-,-,-) input parameters here for doing any introspection; Nothing but if afterReturning method wants to do introspection by using the 3 named parameters above; then inputs here are given as follows;
           - Method m - Takes||holds your Target class method. i.e., deposit method
           - Object array of Method input parameters; Object[] parameters i.e., deposit method inputs from user
           - Object o - Takes||holds your Targetproxy class object. i.e., com.get_tt_right.business.Bank@bd8db5a bean class object.
   - So, by using these input parameters of afterReturning method you can do introspection here. Like for example;
          - If you want to check if inputed accNo is valid or not from here. So, by using this Object[] params input you can get the Target class method inputs -Bank class deposit inputs i.e., accNo and amt
          - You can check like; if accNo is fine then redirect to deposit functionality  or else from here itself you just throw exception.
Hope you are getting why they given the input parameters in before(-,-,-) and afterReturning(-,-,-,-) methods; It is just for doing introspection.

Now your service class is ready.
 */

public class LogAfterService implements AfterReturningAdvice {

//	By using the inputs of afterReturning(-,-,-,-) method; we can be able to perform inspection on your respective Target class method. 
	@Override
	public void afterReturning(Object returnval, Method m, Object[] params, Object o) throws Throwable {
        System.out.println("-----------------Service executing--------------------------------");
		Log l = LogFactory.getLog(Bank.class);
		l.info("Using decorator pattern internally am executing after each and every target method has executed");
		System.out.println("Return value from target method: "+returnval);
		System.out.println("Bankproxy Object: "+o);
		System.out.println("---------------------------------------------------------------------");
	}

}
