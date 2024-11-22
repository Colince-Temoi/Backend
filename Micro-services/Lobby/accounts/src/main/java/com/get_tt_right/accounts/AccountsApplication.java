package com.get_tt_right.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
/*@ComponentScans({ @ComponentScan("com.eazybytes.accounts.controller") })  // Using this, we need to mention all the packages where our beans are present. Like for example we have beans under the controller package. Similarly, we have beans inside the service package ... etc.
                                                                            // So, all those packages we need to repeat with the help of this @ComponentScan annotation inside @ComponentScans annotation.
@EnableJpaRepositories("com.eazybytes.accounts.repository")  // To mention where all your repositories are like jpa repositories and Jpa entities.
@EntityScan("com.eazybytes.accounts.model")

// I have commented out the above 3 annotations because I don't need to mention them. Reason, I maintained my sub-packages inside the main class package.
*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl") // In this way we are telling the Spring Data JPA framework that please activate the Spring Data Jpa auditing feature and leverage the bean with the name  auditAwareImpl to understand the auditor.
/** We are trying to provide the definition details with the help of OpenAPI and that's why we are using the annotation @OpenAPIDefinition
 * Using this annotation, we need to invoke a parameter called info.
 * To this info param we should assign by invoking another annotation called @Info.
 * Inside this @Info annotation, we need to provide the title, description, version, contact, and license details of our application.
 * using title you can mention a short summery about your application. It must make sense based upon the business application.
 * Using description you can mention a good description about your REST APIs. It must make sense based upon the business application.
 * Using version you can mention the version of your application. As of now we are trying to build a Version V1. If you are trying to build a new version of the application, you can mention the version as V2 ...Vn based upon your business requirements.
 * Using contact param by assigning another annotation called @Contact you can mention the contact details of the developer who has developed this application and whom you can contact regarding this application or documentation. You can mention the name, email, and URL of the developer.
 * This information doesn't have to be individual information, you can define the email of your team or the URL of your team or even organization. This is up to you.
 * Using license param by assigning another annotation called @License you can mention the license information. Because in real projects, we should also provide license information.
 * Whether our REST APIs are free to use or you have any licence restrictions.
 * You can mention the name of the license and the URL of the license. apache 2.0 is a very popular license which I want to follow.
 * I have also provided the url where people can read more about the license details. For now, I'm just mentioning the URL of my GitHub account.
 * All this is sample data that I'm mentioning here. In real projects you should mention something which is relevant to your project and your organization.
 * Once we have mentioned all these info details, we can also provide/mention the external documentation details by invoking another annotation called @ExternalDocumentation.
 *  For example, if people want to learn more about our REST APIs or business, in such scenarios we can provide an external documentation that people can read in case they want to gain more knowledge about our REST APIs and application.
 *  In this annotation, we need to provide the description and URL of the external documentation. I've just mentioned some dummy data here. In real projects you should mention the URL of your application or the URL of your documentation where people can read more about your REST APIs.
 *  In this way, we can provide many and many more details about our application and REST APIs using the OpenAPI annotations.
 *  For example, you can mention security details(Like what security you followed), server details, tags information, extension details, etc. But for now, I'm just mentioning the basic details.
 *  These are all advanced concepts inside OpenAPI specification which need its own discussion.
 *  We will learn more about OpenAPI specification and Swagger ecosystem separately later.
 *  With these changes we should be good from the main class.
 *  Run your application and refer to the Swagger UI to see all this.
 *  You should be able to see a beautiful title,description, contact details to whom they can send an email, they can visit a website, licensing information as well as external documentation information
 * In this way, we have enhanced the top section which is common for all our REST APIs
 * */
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts microservice REST API Documentation",
				description = "EazyBank Accounts microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Colince Temoi",
						email = "colincetemoi190@gmail.com",
						url = "https://github.com/Colince-Temoi"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://github.com/Colince-Temoi"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "EazyBank Accounts microservice REST API Documentation",
				url = "http://localhost:8080/swagger-ui/index.html"
		)
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
