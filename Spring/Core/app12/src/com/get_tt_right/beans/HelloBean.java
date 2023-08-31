package com.get_tt_right.beans;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
/*Bean initialization by implementing InitializingBean and DisposableBean callback interfaces on the respective bean classes.
 *Whenever we are implementing InitializingBean callback interface on top of the respective bean class, afterPropertiesSet() method will be executed.
 *  -Inside this method we are able to provide initializations for our bean object. Nothing but assign values to variables.
 *Whenever we implement DisposableBean callback interface on the respective bean class, destroy() method will be executed.
   - Will be executed during de-instantiation of the respective bean class.
 *    This is all you need to know about this approach.
 * */
/*Callback interfaces-->Means nothing but, just if you implement these interfaces and if you provide implementations for the methods, automatically those methods are going to be executed by Container.
 * We are not going to call this type of methods. Container will take care of everything. Automatically container will call these methods when performing initialization and destruction for the respective bean objects
 * */
public class HelloBean implements InitializingBean, DisposableBean {

	private String name;
	private String message;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String sayHello() {
		return "Hello "+name+" ,"+message;
	}
	@Override
	public void destroy() throws Exception {
		System.out.println("destroy() method");
		
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Initializingbthrough aferPropertiesSet() method");
		name="Colince";
		message="GoodMorning";
	}
	
	
}
