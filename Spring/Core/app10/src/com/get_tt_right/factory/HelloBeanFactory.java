package com.get_tt_right.factory;

import com.get_tt_right.beans.HelloBean;

public class HelloBeanFactory {

	public HelloBean getHelloBeanInstance() {
//Proof that the flow of execution should come here first during object creation by the IOC container in the case of this instance factory method object creation.
		System.out.println("Creating object for the loaded bean class by using||from Instance Factory method: NOTE that the default constructor will be called internally for the respective bean object. And before it is called, static blocks for this bean class will be executed first.");
		return new HelloBean();
	}
}
