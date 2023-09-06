package test;

import java.lang.reflect.Constructor;

/*Logic to Creating instance for private Constructor
* By using direct approach we cannot create instancs for private constructors.
* We have to make use of reflect package classes like we are doing below.
*For more information, see notes: Spring Framework 4 [At the bottom]
*/
public class Test {

	public static void main(String[] args) {
		try {
//			Getting class from path
			Class c = Class.forName("beans.HelloBean");
//			Gitting all the possible constructors from the class
			Constructor con[] = c.getDeclaredConstructors();
//			Setting the constructor to being accessible
			con[0].setAccessible(true);
//			Creating instance for the bean class
			con[0].newInstance(null);
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
