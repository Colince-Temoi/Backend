
  package com.get_tt_right.factoryclasses;
  
  import org.springframework.beans.factory.FactoryBean;

import com.get_tt_right.interfaces.Car;
 
/* 3rd and simplest approach to implement factory classes in Spring: Use this approach to create your factory classes.
   -------------------------------------------------------------------------------------------------------------------
 Logic
 -----
 1. Write one Interface; Car(I)
    - Has rules that need to be implemented by implementation partners
 2. Create a Factory class and implement it from Spring given FactoryBean(I) interface.
 3. Override methods from this interface. The methods are:
    - getObject(): Its return type, maintain it as Object.
       => Holds the instantiation logic to implementation partner classes.
    - getObjectType(): Its return type maintain it as Class.
    - isSingleton() : Its return type is boolean.

 This Factory class; FactoryBeanInterfaceCarFactory, need to have capabilities to return any Car(I) implementation partners class object. Therefore:
 -Fully qualified name of implementation partner class name it needs to take as input.
   => That's why I am maintaining a primitive attribute to store this input.
 -By taking that input, inside getObject() method create object to the implementation partner class. i.e., Class.forName(carname).newInstance();
 -The created object we need to upcast into Car(I) reference. i.e., Car c = (Car)Class.forName(carname).newInstance();
 -Lastly, return c
 
 Now, after loading Xml file with this bean class configuration into your Container, whenever you try to call getBean(“factorybeaninterfacecarfactory”) method; what do you expect to get? 😂 Instead of getting FactoryBeanInterfaceCarFactory object, you will get implementation partner class object in the form of Car(I) reference.
- For getObjectType() method’s logic; just return your expected type; In our case; it is Car type. So, return Car.class;
- For isSingleton() method’s logic; Do you want to maintain that created implementation partner class object as singleton? If yes; return true;  It is the default anyways. Else; return false; then it will return for each call a separate Car object.

 * */
  public class FactoryBeanInterfaceCarFactory implements FactoryBean<Object> {

//	  Primitive association||variable||attribute||dependency
	  private String carname;
	  
//	  Setter method - To receive input from Container during Setter DI
	  public void setCarname(String carname) {
		this.carname = carname;
	}
// Holds instantiation logic to implementation partners classes  
	@Override
	public Object getObject() throws Exception {
		Car car = (Car) Class.forName(carname).newInstance();
		return car;
	}
// Returns the created Object expected return type: In this case I am expecting Car objects which are returned in the form of Car(I), so: Car.class is the return type.
	@Override
	public Class<?> getObjectType() {
		return Car.class;
	}
// Do you want to maintain the implementation partner created object as singleton or not?	
	@Override
		public boolean isSingleton() {
			return true;
		}
  

  }
  /*NOTES:
   * Writing a factory class is the second thing we need to provide when preparing factory classes.
   * Above is how to prepare factory class by implementing spring given interface; FactoryBean(I)
   *Now; this prepared factory class is capable to create implementation partners' class objects.
   *In future; if you have one more implementation class from some other implementation partner happily that implementation partner class object this factory class can be able to create.
   * */
 
