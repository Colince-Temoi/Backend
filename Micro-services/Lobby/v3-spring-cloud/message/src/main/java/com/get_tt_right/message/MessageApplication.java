package com.get_tt_right.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**Update as of 17/08/2025
 * --------------------------
 * Now, under the source folder in your 'message' service, we will create a new package i.e., 'dto'. Inside this package, we are going to create a dto class using which I can always accept the message that I am going to receive from the message broker. My Accounts ms is going to send a message to the message broker in the format of this dto. The same I am going to receive inside this message service and process the same. This time instead of a Java class, I am going to create a 'Record' and the 'Record' name is going to be 'AccountsMsgDto'. Like we discussed, we can use a Record class instead of a Java class. Whenever we use Record class, automatically the container will generate the getters for your fields/attributes and it is also going to make the fields final. Which means once the object of this Record is created, all the data inside it is going to be final. So, inside this record, I am going to mention fields/attributes which I intend to receive from the accounts ms. Whenever we want to mention fields/attributes inside a record class, we just need to mention them inside the brackets i.e., ('mention_fields_here').
 * Create another package i.e., functions - inside this package only I am going to create all the functions that are needed for my business logic. So, Create a class inside this package i.e.,MessageFunctions - All the functions that I am going write inside this class are going to handle messaging to the end-user. Check out this class for more details on its contents.
 * */
@SpringBootApplication
public class MessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageApplication.class, args);
	}

}
