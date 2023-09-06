package beans;

public class HelloBean {

	private HelloBean() {
		System.out.println("Accessing private constructor of bean class using reflect api");
	}
}
