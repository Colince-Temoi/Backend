package com.get_tt_right.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/*@Autowired annotation
- 1st;it internally uses byType mechanism to perform Secondary type autowiring||automatic DI-hence chances are available to run into ambiguity problems.
- 2nd; it tries to use byName mechanism to perform Secondary type autowiring in case 1st step has ambiguity issues.
- If 2nd option fails as well; ambiguity issue arises.
- To solve the ambiguity issues; utilize @Qualifier("id") annotation.
- As a parameter to @Qualiofier(value="id") annotaion, pass the configured bean id you exactly need to inject||autowire from the Spring configuration file by the Container.
- When using annotations to perform autowiring of Secondary types, NO NEED to  maintain Setter and parameterized constructors to receive these Secondary type inputs.
- These annotations have direct access to the Secondary variables||associations||dependencies||attributes||properties from where they can inject||assign the dependencies||references.
- So, in a nutshell, Be Utilizing @Autowired and @Qualifier(value=“id”) annotations to perform AutoWiring||Automatic DI for Secondary types. REASON;
		•	Minimizes configurations inside Spring configuration file.
		•	Minimizes code inside bean classes, as NO NEED to maintain Setter and parameterized constructors to receive Secondary type associations inputs.
		•	Visit Spring 7-DI notes for more info.
- To activate these two annotations, you need to instantiate one class namely; AutowiredAnnotaionBeanPostProcessor.
- Actually, just configure this class inside Spring Configuration file and Container will instantiate it for us. Remember to use its fully qualified name during configuration.
*/
public class Car {

//	primitive association(s)||dependency(s)||attribute(s)||property(s)
	private String carname;
//	Secondary association(s)||dependency(s)||attribute(s)||property(s)

	@Autowired
//	@Qualifier(value = "e1")
	private Engine engine;

//	setter method(s) - To receive input from Container  during Setter DI
	public void setCarname(String carname) {
		this.carname = carname;
	}

//	Business method(s) -- Hold class functionality||logic
	public void printData() {
		System.out.println(carname);
		System.out.println(engine.getModelyear());
	}
}

