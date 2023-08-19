package com.get_tt_right.scopes;

import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
/*
 * Number of methods are available in Scope interface.
 * We only need to implement get() and remove.Rest other methods leave them with blank implementations.
 * Before that, 1st of all we need to declare two objects||containers[Think of them as variable] in this class.
 *  - Map<String, Object> object -Of course that is HashMap.Initialize it with a null value.
 *  - CustomThreadLocal object. Initialize it with an instance of CustomThreadLocal class. Nothing but in simple terms we are creating an object for this.
 *    ->Using its reference we are able to access it's methods because it is a public class.
 *  
 * get() method
 * -------------
 * Q. What implementation do we need to provide inside this method?
 * Ans. - Accepts two parameters.
 *      - For its parameters: arg0 should be key, nothing but we are going to use "name" as the variable.
 *      - arg1 variable name we are going to use, "objecFactory"
 *      Logic
 *      ------
 *      - 1. Check whether map object is available or not.
 *           - call initialValue() method which returns a map object.
 *           - Store that in "scope" variable of Map.
 *      - 2. Check whether bean object is available in scope or not.
 *           - If bean object is available in scope then, return that object.
 *           - If not available in scope, get that bean object from objectFactory, keep that bean object in scope and finally return that.
 *        ObjectFactory is providing all of our bean objects.    
 *        Thread Scope is represented by HashMap object, and that we have to get by calling initialValue method using "threadLocal" reference directly
 * remove() method
 * -------------------
 * Q. What implementation do we need to provide inside this method?
 * Ans. - Require only one Parameter.
 *      - For its parameter, we are using: "name". Is the key.
 *      - Just we need to use this to remove a bean object from Object scope.
 *      - Whatever object we removed there, that we are returning.
 * This is all about this ThreadScope class.
 * Next, we need to go for set ups in the Configuration file.See detailed information from the configuration file.
 * */
public class ThreadScope implements Scope {

	Map<String, Object> scope=null;
	CustomThreadLocal threadLocal=new CustomThreadLocal();
	
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
//		When we call get method below, initialValue method will be executed as we did not specify any data inside the thread scope using set method. For more understanding review app05.
		scope =(Map<String, Object>)threadLocal.get(); //this get n=method here is a ThreadLocal method.
//		Getting bean object from map(Scope)
		Object obj=scope.get(name); //get method here is map method.
//		Checking if bean object is available with this particular name.
		if (obj==null) {
//			Automatically, we will be able to get the bean Object for whatever name we provided here.
			obj = objectFactory.getObject();
//			After getting the particular object, that we are keeping it inside the scope.
			scope.put(name, obj); // put is map object here.
		}
		return obj;
	}

	@Override
		public String getConversationId() {
			// TODO Auto-generated method stub
			return null;
		}

	@Override
	public void registerDestructionCallback(String arg0, Runnable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object remove(String name) {
		Object obj= scope.remove(name);
		return obj;
	}

	@Override
	public Object resolveContextualObject(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
