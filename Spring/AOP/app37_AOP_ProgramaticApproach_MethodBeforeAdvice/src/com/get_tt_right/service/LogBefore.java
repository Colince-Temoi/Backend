package com.get_tt_right.service;
/* Logging Service implementation
 * We are implementing this class from MethodBeforeAdvice
  => Means when we combine this service with Target||Business class; for all the business class methods, by using decorator pattern;
    service will execute first before each and every respective business class method logic||functionality.
 * The input parameters inside before(-,-,-) method are for introspection purpose.
 *         +. In case if your service requires your Method(before(-,-,-) input parameters here for doing any introspection; Nothing but if before method wants to do introspection by using the inputs; then inputs here are given as follows;
           - Method m - Takes||holds your Target class method. i.e., deposit method
           - Object array of Method input parameters; Object[] parameters. i.e, your deposit method inputs it will take and store as array.
           - Object o - Takes||holds your Targetproxy class object. i.e., com.get_tt_right.business.Bank@bd8db5a bean class object.
   - So, by using these input parameters of before method you can do introspection here. 
          - If you want to check if accNo is valid or not from here. So, by using this Object[] of method params input you can access the Target class method inputs -deposit Bank class method inputs that are coming from the user i.e., accNo and amt
          - You can check like; if accNo is fine then redirect to deposit functionality  or else from here itself you just throw exception.
Hope you are getting why they given the input parameters in before(-,-,-) and afterReturning(-,-,-,-) methods; It is just for doing introspections.

 */
/* More use cases: 
The `before` method in Spring's `MethodBeforeAdvice` interface is primarily used for executing code before a target method is invoked. 
It provides a way to perform certain actions or validations before the actual method execution. Here are some common use cases and purposes for using the input parameters of the `before` method:

1. Logging: You can log information about the method call and its arguments.
This is a common use case to track the flow of your application and diagnose issues.

2. Security: You can perform security checks or access control checks before allowing a method to proceed.
For example, you might check whether the current user has the necessary permissions to execute the method.

3. Transaction Management: You can manage transactions by starting, committing, or rolling back a transaction before and after a method call.

4. Input Validation: You can validate the input parameters of the target method.
This ensures that the method is only called with valid arguments and helps prevent invalid data from causing issues further down the execution path.

5. Caching: You can implement caching mechanisms that check whether the desired result is already cached and return it if available, bypassing the actual method execution.

6. Monitoring and Metrics: You can collect data for monitoring and performance metrics.
For instance, you might measure the execution time of the method or count the number of times it's called.

7. Preprocessing: You can preprocess or transform the input parameters before the method is executed.
For example, you might convert data formats, perform calculations, or set default values.

8. Resource Management: You can manage resources such as database connections, threads, or file handles before and after the method call.

9. Exception Handling: You can perform custom exception handling, logging, or error recovery before an exception is thrown from the target method.

10. Context Setup: You can set up a context or environment required for the target method's execution.

Overall, the `before` method provides a hook for you to add custom logic that should run before the actual business logic of the target method.
It allows you to separate concerns and keep your code organized, making it easier to maintain and enhance your application.
The specific use case will depend on the requirements and design of your application.
 */
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.MethodBeforeAdvice;

import com.get_tt_right.business.Bank;

public class LogBefore implements MethodBeforeAdvice {

//	This method is throwing Throwable; means that: if any exception arises here; you no need to handle that.
	@Override
	public void before(Method m, Object[] params, Object o) throws Throwable {
        System.out.println("-----------------Service executing--------------------------------");
		Log l = LogFactory.getLog(Bank.class);
		l.info("Will execute first before any Target method logic||code||functionality execution");
		
        // Access input parameters
        for (Object arg : params) {
            System.out.println("Input parameter: " + arg);
        }

	}

}
