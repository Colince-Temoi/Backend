package com.get_tt_right.beans;

/*Printing some statements inside init() method and destroy() method.
 * If I run this program, am getting output from init() method but where is the destroy() method's output??
 * My program is termited but can't see the destroy method output?? How??
 * Actually destroy method() should be executed when this bean is going to be de-instantiated. But when bean objects are
   de-instantiated?? Whenever we are terminating this container, automatically that is going to be de-instantiated but that output we are
   not going to see because:
      main thread is going to be completed before performing this destruction here.
 * Because of this reason, if you are going destroy the bean object explicitly, we need to make a use of a separate method known as:
 *   context.registerShutdownHook() method.
 *But this method is not available in ApplicationContext in one sub-interface of this Application context called: AbstractApplicationContext.
 *In Application context, initialization process is present, but if you want to destroy bean objects explicitly, then we need to make use of  what we have just discussed above.
 *Now, whenever we call: context.registerShutdownHook(), happilly destroy method is going to be executed and it's out put we are able to see as well.
 * */
/*Benefit of having init() and destroy()method explicitly like this.
 * 1. XML configurations are reduced. No need to perform initializations inside Spring configuration file by making use of property tag which has name and value attributes.
 * 2. Moreover, whatever the initializations we provided through this XML, we can think of that as static initialization. But if we go for the Java based initializations inside init() method
 *    like below, somewhat dynamic initialization we are able to provide.
 * */
public class HelloBean {

//	Properties
	private String name;
	private String message;
//	Getter and setter methods.
	public String getName() {
		return name;
	}
	public void setName(String name) {
		System.out.println("Initializing name thought setName() method.");		
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		System.out.println("Initializing message through setMessage() method.");
		this.message = message;
	}
//	My user define initialization method.
	public void init() {
		System.out.println("init()-Method");
		System.out.println("Initializing both name and message properties through init()-method");
		name="Nag,";
//		message=" Good afternoon!";
	}
//  My user define destruction method.
	public void destroy() {
		System.out.println("destroy()-Method");
		name="";
		message="";
		
	}
//	Business method(s)
	public String sayHello() {
//message value initialization will be taken from that which we provided through setXXX() method.
//name value initialization will be taken from init() method
		return "Hello "+name+" "+message;
	}
	
	
}
