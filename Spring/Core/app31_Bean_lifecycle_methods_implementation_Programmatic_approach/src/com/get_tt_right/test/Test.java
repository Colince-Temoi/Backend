package com.get_tt_right.test;

import java.util.Scanner;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.DaoBean;

/*In Spring bean classes, we can also write init and destroy lifecycle methods.
These methods, your ConfigurableApplicationContext will maintain||manage.
	ConfigurableApplicationContext(I) container is nothing but a child interface to ApplicationContext(I) container. 
	For these two interfaces, we have one implementation class known as: ClassPathXMLApplicationContext(C).
	So, while starting ConfigurableApplicationContext(I) container, it will call lifecycle init() methods.
	If you want to call lifecycle destroy methods; inside ConfigurableApplicationContext container we have  one method close() method. Once you invoke this method on top of the Container; all the destroy bean lifecycle methods will be invoked. i.e., cap.close()

We are able to access this DaoBean class save() method as many times as we wish using one single connection object reference.
*/

public class Test {

	public static void main(String[] args) throws Exception {
		// Prepare Container
		ConfigurableApplicationContext cap = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		while (true) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter 1 for save operation and 2 for closing DB connection");
			int i = sc.nextInt();
			
			switch (i) {
			case 1: {
				DaoBean daobean = (DaoBean) cap.getBean("daobean");
				
				System.out.println("Enter id");
				int id = sc.nextInt();
				
				System.out.println("Enter name");
				String name = sc.next();
				
				System.out.println("Enter email");
				String email = sc.next();
				
				System.out.println("Enter address");
				String address = sc.next();
				
				daobean.save(id, name, email, address);
				
				break;
			}
			default:
//				Closing container; It is during this stage that all lifecycle destroy methods will be invoked and executed.
				cap.close();
				break;
			}
		}

	}

}
