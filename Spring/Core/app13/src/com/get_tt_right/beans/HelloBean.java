package com.get_tt_right.beans;
/*Define 2 user-defined methods:
1.	initializeBean()
-	Annotate this with @PostConstruct annotation.
-	Inside this method, we are going to provide initializations.
2.	destroyBean() 
-	Annotate this with @PreDestroy annotation.
-	Inside this method we are going to provide de-instantiation||clean-up operations.
We need to tell the container about these annotations.
One way to do this is through Spring Configuration, ApplicationContext.xml file.
We need to make use of a tag: <context:annotation-config/>
Also make sure that your ApplicationContext.xml file has the context namespace as well along with the bean namespace that we have been using in our previous Spring applications in this series.
-	Ctrl + F  .org/schema/context*/

public class HelloBean{

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
	
//	@PostConstruct
	public void initializaBean() {
		System.out.println("Initialization through @PostConstruct method");
		name="Colince";
		message="How are you!";
	}
//	@PreDestroy
	public void destroyBean() {
		System.out.println("Bean deinstantiation through PreDestroy() method");
	}
	
	public String sayHello() {
		return "Hello "+name+" "+message;
	}
	
}
//Research on how you will make the two annotaions work.