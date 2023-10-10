package com.get_tt_right.test;

import org.springframework.aop.framework.ProxyFactoryBean;

import com.get_tt_right.business.Bank;
import com.get_tt_right.service.LogBefore;
/* Combining Target + Service through AOP.
     Steps
     ------
  1. Create Target class object. In this case; Bank class.
  2. Create Service||Aspect class object. In this case; LogBefore class.
  3. Add Target + Service to AOP proxy class-ProxyFactoryBean-for weaving 
      3.1. Create AOP proxy class object. This AOP proxy class has several setter methods which we need to utilize in order to achieve step 3.
      3.2. Set Target: Achieved through AOP proxy class object setter method-> setTarget(Input Target class reference)
      3.3. Set Service(s) : Achieved through AOP proxy class object setter method -> setInterCeptorNames(Take String[] array of Service||Interceptor names)
                            For programmatic approach; like in this case-utilize addAdvice() method to add service to AOP proxy.
  4. Get generated Target||Business proxy object. For this, we need to invoke one ProxyFactoryBean class method: getObject
     - This will return us proxy object and that proxy is child class for your Target||Business class.
     - We can type cast that into Bank type; Nothing but storing child class object reference into parent.  
  5. Now you can invoke Target class methods and based on the type advice you implemented your service class(s); Service classes will get executed for all your business class methods using the decorator pattern internally.
     - For this case; our LogBefore service class will execute before Target class methods functionality executes. This is because we have implemented our LogBefore service class from MethodBeforeAdvice.  
* We have other advices as well:
  => AfterReturningAdvice: Will make your service execute after each and every business||Target class method functionality has executed.
  => MethodInterceptor: Will make your service execute before and after each and every business||Target class method functionality has executed.
  => ThrowsAdvice: In case of Exception.
* Visit notes for more clarity about these advices.   
* 
* In this Client||Test class; we are not using any Container(Spring Container or J2ee Container) to create and manage us objects to bean classes.
* Just programmatically we are creating object to service bean class(s) and Target bean class(s) and adding them to AOP proxy for weaving.
* 
* If you run this program, expected outcome we are getting;
*     - First service bean class is executing each time before execution of each and every Target class method using the decorator pattern.
* In case if you want o execute your service method each time after execution of each and every Target class method, then you have  to:
*     - Implement your service bean class from AfterReturningAdvice.   
 */
public class Test {

	public static void main(String[] args) {
		
		Bank b = new Bank();
		LogBefore lb = new LogBefore();
		
		ProxyFactoryBean pfb = new ProxyFactoryBean();
		
		pfb.setTarget(b);
//		pfb.setInterceptorNames(new String[] {"lb"});  // In Xml file configurations is where we actually have to use this.
		pfb.addAdvice(lb);
		
		Bank bproxy = (Bank) pfb.getObject();
		
		int amt = bproxy.deposit(1000, "CBA123");
		
		System.out.println(amt);
		
		bproxy.anotherMethod();
	}

}
