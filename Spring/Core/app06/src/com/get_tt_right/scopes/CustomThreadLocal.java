package com.get_tt_right.scopes;

import java.util.HashMap;

/*ThreadLocal predefined class
 * Has several methods:get(), set(), remove(), initialValue(),...etc.
 * We are going to make use of initialValue method here. REASON:
 * Inside this method, we are going to return: HashMap. REASON:
 *  - Scope in the sense, map object. Nothing but think of scope as an object. And we are making use of map object, because it is able to take key value pair.
 *  - If you keep bean objects along with key in this map object, then that object is available in the scope.
 *  - If you remove the bean object from map object, then that object is removed from scope.
 * Like this, map object is acting as scope object. Nothing but the scope is represented in the form of HashMap.
 * So this is all about this class. For more information about ThreadLocal implementation visit app05, we did a simple example on its implementation.
 * This class is able to generate scope and is able to to keep the data in form of key-value pair and retrieve the data||bean object.
 * Next we need to define ThreadScope class. This will check whether bean object is available in the scope or not.
 *  - If present, return that.
 *  - If not present, create the object, keep it in scope and then return that.
 * So, to retrieve bean objects from scope|| keep bean objects in scope, then a separate class we require to prepare: ThreadScope class.
 * ThreadScope class MUST implement Scope interface.
 * */
public class CustomThreadLocal extends ThreadLocal<Object> {

	@Override
	protected Object initialValue() {
		return new HashMap<String, Object>();
	}
}
