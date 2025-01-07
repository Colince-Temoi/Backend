package com.get_tt_right.accounts.dto;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
/** Record is a new type of Java classes introduced in Java 17. What is it? Why did Java people introduce it? What are the benefits of using it? Let's see in this article.
 * Sometimes we want our POJO classes or DTO classes to simple act as data carriers . Which means, first I will create an object of this Dto class and someone can read the data from the object of this Dto class, but they should not be able to change it.
 * So, whatever fields data that you pass during the object creation those are going to be final and immutable. So, no one can change the data of the object of this Dto class. They can only read using the getter methods. There won't be any setter methods.
 * This is a most common scenario and requirement. Instead of writing a class with private fields, final fields, constructor, getter methods, equals and hashcode methods, toString method, etc. We can simply use the record keyword to create a record class.
 * I mean instead of writing all the getter methods and creating a constructor that is going to accept the final Java fields, you can simply use this Record class. So, to this record class, you can pass all the java fields inside its brackets.
 * In order to define the Java fields, first, you need to know what is the property name that I have defined inside the application.yaml file. The very first property name is message. So, I will create a Java field with the name message and since it is holding a value of type String, I will mention the type of this field as String.
 * The second property name is contactDetails. So, I will create a Java field with the name contactDetails and since it is holding a value of type Map, I will mention the type of this field as Map/HashMap. Inside the Map, the key is of type String and the value is of type String.
 * The third property name is onCallSupport. So, I will create a Java field with the name onCallSupport and since it is holding a value of type List, I will mention the type of this field as List. Inside the List,I will make the value be of type String.
 * With this we should be good. Now behind the scenes what happens is, the Java compiler will generate all the boilerplate code for you. It will generate the constructor, getter methods, equals and hashcode methods, toString method, etc. So, you don't need to write all these boilerplate code. I mean,
 * Java is going to make these fields final and at the same time it is going to generate a getter method for each field and also a constructor that is going to accept all the fields. There won't be any setter methods which means whenever you are using record type classes you can only initialize the data only once during the object creation.
 * You cannot change that data only what you have provided during object creation that is going to be the final data. You can only read the data using the getter methods.
 * Since we also have a requirement to not change these configuration property values inside our Java business code at runtime again, and again it makes sense to use the record type classes. Because, the record type classes are immutable and final. So, no one can change the data of the object of this Dto class. They can only read using the getter methods.
 * Now to map the configuration properties to this record class/Java fields we need to use the @ConfigurationProperties annotation. We will mention the prefix of the configuration properties inside the @ConfigurationProperties annotation. So, the prefix of the configuration properties is accounts. So, I will mention the prefix as accounts.
 * Once we are done making these changes, we need to go to our Spring boot main class and mention an annotation called @EnableConfigurationProperties. Inside this annotation, we need to mention the class name of the record class that we have created as a value to 'value' attribute.
 * I mean the value(s) of 'value' attribute need to be Pojo class name(s) or record class name(s) that we have created. This we will mention inside open and close curly braces. So, I will mention the record class name as AccountsContactInfoDto.class.
 * Check out the main class to see what I am talking about.
 *Now, the Pojo class is ready and during the startup Spring boot will try to read all the properties with the prefix value as accounts, and it will try to map all those property values to the fields present inside this Pojo class. As a next step, I can use this Pojo class anywhere inside my business logic and I don't have to depend on the @Value annotation to read the configuration properties.
 * So, let me go to the AccountsController class and create a new REST Api with the name getContactInfo. Check it out.
 * Also since we have mentioned this @ConfigurationProperties annotation on top of this class, beind the scenes Spring framework is going to create a bean of this class. That's why we can inject it directly wherever we want to use it using @Autowired annotation. So, I will inject it inside the AccountsController class.
 * */
@ConfigurationProperties(prefix = "accounts")
public record AccountsContactInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {

}