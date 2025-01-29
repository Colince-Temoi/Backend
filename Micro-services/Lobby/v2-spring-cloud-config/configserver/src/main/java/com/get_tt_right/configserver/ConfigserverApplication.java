package com.get_tt_right.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/** Update as of 1/26/2025
 * We added the @EnableConfigServer annotation to enable the config server functionality.
 * Next,rename the accounts.yml file to accounts.yml. Open it to see whatever we are doing in it.
 * For config server we are going to use the port 8071. This is the first thing we have configured. Once these 2 changes are done, we have our config server ready inside our microservices network.
 * But, as of now, there is no place for our config server to read our configurations. That's why we need to move all the configurations of our microservices to a centralized location. Next, we need
 * to configure that centralized location inside this config server so that our config server will start reading from the centralized location.
 * We have multiple options to store our configurations in a centralized location. Right from class path to file system to GitHub repository to database to Cloud i.e., Aws S3, Azure Blob etc. We can use any of these options.
 * But, for now, we are going to cover 3 approaches which are very common i.e., class path of the config server, file system - Means anywhere inside your Server/Local system and you can read the configurations from the particular folder inside your file system and GitHub repository which is the most commonly used.
 * 1. Reading configurations from class path of the config server
 * - 1st define the property spring.application.name to name my config server as configserver
 *   . From now onwards, make sure to give names to all our Spring boot applications. Using these names only the entire concept of Spring Cloud config server is going to work. On how, we will see later.
 * - Create a new folder under the resources folder named config. Inside this folder, we will store all the configurations related to other microiservices.
 * - Under Acounts microservices, you know that we have 3 different profiles i.e., default, qa and prod.  Copy/Paste the 3 files to the config folder of the config server. But we have a challange!?  For Accounts microservice, yes!, we can mention these 3 files as application-default.yml, application-qa.yml and application-prod.yml.
 *   But, what about the other microservices?  We can't mention the same names for all the microservices. So, we need to mention the names of the files as {spring.application.name}-{profile}.yml. So, for the Accounts microservice, we can mention the names as accounts.yml, accounts-qa.yml and accounts-prod.yml.
 *   So, copy/paste the 3 files of the Accounts microservice to the config folder of the config server and rename them as accounts.yml, accounts-qa.yml and accounts-prod.yml.
 *   If you open accounts-qa.yml, you can see properties such as build.version, accounts.message, accounts.contactDetails.name and accounts.contactDetails.email. These are the properties which we have defined in the accounts.yml file and the ones we are trying to externalize from our business logic.
 *   Remove the property spring.config.activate.on-profile because we are from now onwards not going to use the approaches related to spring boot. We are going to use the Spring Cloud Config Server approach. And that's why we can safely delete this property.
 *   Do the same for the other 2 files i.e., accounts.yml and accounts-prod.yml.
 *   Its also worth noting that for accounts.yml, we have properties related server.port, datasource related properties, h2, Jpa , config, profiles. We don't want to externalize all these properties Delete all of them.
 *   and retain build.version, accounts.message, accounts.contactDetails.name,  accounts.contactDetails.email andaccounts.onCallSupport.
 * - Like this inside the config server we have properties that are strictly configurations we need externalized for default, qa and prod profiles of the Accounts microservice.
 * - Do the very same thing for loans and cards microservices. Copy/Paste the files to the config folder of the config server and rename them as loans.yml, loans-qa.yml, loans-prod.yml, cards.yml, cards-qa.yml and cards-prod.yml.
 * - If you randomly open any of these files, you will see they have ONLY the configurations we want to externalize and follow/activate whenever we want to use the respective profiles.
 * - Now, we have moved all the required properties that we need to externalize from our business logic to a centralized location which is the class path location of the config server.
 * - Next, we have to communicate to the Spring Cloud config server we have stored all these files into so-and-so location. For the same we need to open the application.yml file of the config server and define the below configurations/properties.
 *  . spring.profiles.active: native
 *    With the value 'native', we are telling the spring cloud config server that we want to activate the native profile of spring cloud config server.
 *    Whenever we are using class path to store all our configurations, we need to activate the native profile.
 *  . Once we defined these profile activation details, under the same spring, we need to create few more properties. I.e.,
 *    spring.cloud.config.server.native (Under the server we need to mention native because we activated the native profile) and under this property, we need to mention the location where we have stored all our configurations by the property search-locations.
 *    With this property only, we are going to tell to the Spring cloud config server where our properties are stored. Right now, we have stored our properties inside the classpath and that's why we have to mention the 'classpath' prefix followed by colon and the location of the properties/Folder location inside your classpath.
 *    So, the property will be search-locations: classpath:/config
 *  . With this, we should be good as we have made all the required changes. Now, we can start our config server in debug mode and see if it is able to read the configurations from the classpath location.
 *  The server started successfully at port 8071. How do we validate that the server loaded all the properties from our storage location?
 *  - For the same we can try to invoke various GET API paths that are exposed by the Spring Cloud Config server. We need to invoke the path http://localhost:8071/{application-name}/{profile} and see if we are able to get the configurations.
 *  - For example, if we want to see the configurations of the Accounts microservice for the default profile, we can invoke the path http://localhost:8071/accounts/default. If we want to see the configurations of the Accounts microservice for the qa profile, we can invoke the path http://localhost:8071/accounts/qa. If we want to see the configurations of the Accounts microservice for the prod profile, we can invoke the path http://localhost:8071/accounts/prod.
 *  - We invoked http://localhost:8071/accounts/prod and got the configurations related to default profile?! How?! Not what we expect! This is because I intentionally made a mistake in the naming of the files. Instead of accounts-prod.yml,I named it accounts-prod.yml. Hope you can see how as small mistake can be costly. So, correct the file names to have hyphens instead of underscores.
 *  - Once we corrected the file names,when we invoke the path http://localhost:8071/accounts/prod. We are getting under the "propertySources" key an Array of objects. Each object in the array is a configuration file and in our case, we should be able to see properties of the default and prod profiles of the Accounts microservice.
 *  - Yes! that's the expected behavior because by default all the properties inside your default profile will be loaded. Apart from that since we are trying to access the properties from the prod profile, we are able to see the properties of the prod profile as well.
 *  - During the startup of our microservice, the default profile values will be ignored the values of the prod profile will be used if you try to activate the prod profile.
 *  - You can validate the same for qa profile as well. Same behavior you will see. For default profile you will only see one object inside the propertySources array.
 *  - Validate the same for other microservices and yeey! everything is working as expected.
 * With this we can safely conclude that all the profiles are loaded into spring cloud config server. Like whatever configurations that we have stored in the classpath location of the config server are loaded successfully during the startup of the config server.
 * As a next step, we need to establish a link between our config server and our individual microservices so that during the startup of the individual microservices based upon the profile that we are trying to activate these APIs of the config server will be invoked and the property details present inside them will be considered by our individual microservices.
 * Yes, that's the target we have right now. You may have a question like, how is the JSON repsponse displaying for me very beautifully? In your case it's maybe showing very raw data/JSON data. That's because I have installed a plugin in my Chrome browser i.e., JSONView
 * With this, we have successfully created a config server and right now we are able to read the configurations from the classpath location of the config server. Next, we will establish a  link between our config server and our individual microservices.
 *
 * Connecting our Accounts microservice to the Config server we have created.
 * - In the resources folder of Accounts microservice, delete the application_qa.yml and application_prod.yml files. We don't need them anymore because we are right now storing them in the classpath location of the config server.
 * - In the application.yml file if there are any configurations related to config.import, profiles.active,build.version,accounts.message,accounts.contactDetails.name,accounts.contactDetails.email,accounts.onCallSupport, delete all of them because we no more need them.
 * - Now, I am left with properties related to the server.port, datasource, h2 and jpa related properties as we don't want to externalize these properties. We are no more maintaining any properties related to Spring boot profile files and Spring boot profile activation inside the Accounts microservice application.yml file.
 * - As a next step like we discussed, we SHOULD! give a name to our microservice. So, I am going to give the name as accounts. This is the name that we are going to use to establish a link between our Accounts microservice and the config server.
 * - The name has to be exact as the prefix of the file names specific to a particular microservice that we have stored in the classpath location of the config server. So, the name of the microservice should be accounts.
 * - Using this application name only, your individual microservice is going to request config server that "Hey! I am the Accounts microservice. Can you please give me the configurations that are specific to me based upon the profile that I am trying to activate?".
 * - Next, create a property under spring to activate a profile by default. I.e., spring.profiles.active: prod. This is the profile that will be activated by default during the startup of the Accounts microservice.
 *   You can always change this behavior from an externalized configuration with the help of command line arguments or environment variables or JVM arguments like we discussed earlier.
 * - As a next step, we need to go to the pom.xml of Accounts microservice and add a dependency related to Spring Cloud config Client. You can easily get this from the start.spring.io website. Just search for Spring Cloud Config Client and add the dependency then copy the dependency and paste it in the pom.xml file of the Accounts microservice.
 *   This is the dependency that we need to add for the client applications who are trying to connect with your config server. Since it's the very first time inside our Accounts microservices we are trying to add the dependencies related to Spring cloud;
 *    . Create a property defining what the version of Spring Cloud we want to use. You can get this from the start.spring.io website in the exploration pom when you added the Spring Cloud Config Client dependency. This you can easily identify from the properties section of the pom.xml file.
 *       Otherwise, you will end up facing an issue like, org.springframework.cloud:spring-cloud-starter-config:jar:unknown was not found in
 *    . Lastly, make sure to copy the dependency management section from the start.spring.io website and paste it in the pom.xml file of the Accounts microservice jus above the build plugins section if you don't have it already.
 *      This is because the Spring Cloud Config Client dependency that we added is dependent on the Spring Cloud dependencies. It inherits the version from the Spring Cloud Bill Of Materials (BOM) imported in <dependencyManagement>
 *      It is important when it comes to consistency across dependencies. All Spring Cloud-related dependencies in your project will use consistent versions (as defined in the BOM), avoiding potential version conflicts.
 *      That's why we need to have the dependency management section in our pom.xml file. Not only for Spring Cloud Config Client but for any Spring Cloud related dependencies that we are going to add in the future and also maybe any other dependencies that we are going to add in the future.
 *      If you don't do this, you will end up facing issues like, org.springframework.cloud:spring-cloud-starter-config:jar:unknown was not found in
 *   . Finally, you can load your maven changes which will download all the required dependencies related to Spring Cloud Config Client into your local repository.
 * - As a next step, we need to communicate to Accounts microservices about the endpoint url details of the config server. For the same, we need to open the application.yml file of the Accounts microservice and define the below configurations/properties.
 *   . spring.config.import: 'configserver:http://localhost:8071/'  - Like this we are passing the url details of the config server to the Accounts microservice.
 *      8071 is the port number of our config server. localhost is the host name of our config server. It's what we are mentioning for our local environment.
 *      Q. Why do we need to mention the prefix 'configserver' before the url of the config server?
 *      A. This will be an indication to Accounts microservice or any other Spring boot application that it is going to connect with a config server.
 *         Don't confuse this with the name of the config server that we choose for our config server application. Regardless of what is the application name of your config server we should always mention the prefix 'configserver' before the url of the config server in our respective client applications/microservices.
 *         And also, just before the prefix 'configserver', you can also mention the keyword 'optional' if you want to make the connection with the config server optional. If you mention 'optional', the Accounts microservice will start without any issues even if it is not able to connect with the config server.
 *         We are jus telling to Accounts microservice that if it is not able to connect to the config server for whatever reason, it can still continue to start the microservice application.
 *         If you have a scenario where your externalized configurations are super important for your microservice to start and without them, you can't perform any business logic, then you should not mention the keyword 'optional'.
 *         But in our case, since our externalized configurations are not going to be super critical for our microservice to start, we are mentioning the keyword 'optional'. With this, even if my accounts microservice starts before the config server, it will not throw any exceptions or errors during the startup.
 *         It will just throw a warning, and it will continue to start the microservice application.
 * - Now, if I try to start my Accounts microservice , it will go and connect with the config server during the startup and since in Accounts microservice we are trying to activate a profile 'prod' with the property spring.profiles.active: prod, it will go and request the config server to give the configurations of the prod profile of the Accounts microservice.
 *   I mean, it is going to only fetch the configuration/properties related to the prod profile of the Accounts microservice. The properties whatver we have configured inside the accounts-prod.yml file of the config server will be loaded by the accounts microservice during the startup.
 *   If you scroll up in the console output, you should be able to see statements related to the config server. It will identify that the profile activated right now is prod, and it will try load all the relevant properties from the config server.
 *   Now, lets try to test these changes inside our postman client. We can try to invoke the endpoint http://localhost:8080/api/contact-info and see if we are able to get the configurations that we have stored in the accounts-prod.yml file of the config server.
 *   Yes! we are able to get the configurations that we have stored in the accounts-prod.yml file of the config server. This is because we have established a link between our Accounts microservice and the config server and have also specified what profile we want to activate during the startup of the Accounts microservice.
 *   You can also test the http://localhost:8080/api/build-info enpoint, and we are getting 1.0 as expected. http://localhost:8080/api/java-version is going to work all the time very similarly because we are not storing any environment specific configurations/variables inside the spring cloud config server.
 *   Environment variables will work based upon the environment variables that you have created inside your local system or the server where you are trying to deploy your microservice. So whenever we test this, we will simply get the Java home path location of our local system.
 * - As of now we have started our Accounts microservice with a default profile which is 'prod'.  But let's try to activate a different profile from an external configuration and see if our microservice is able to fetch the relevant configurations from the config server.
 *   Add the program argument --spring.profiles.active=qa in the run configuration of the Accounts microservice and start the microservice. You should be able to see that the profile activated is qa and the configurations of the qa profile of the Accounts microservice are loaded during the startup.
 *   You can validate the same by invoking the http://localhost:8080/api/contact-info endpoint and see if you are able to get the configurations that we have stored in the accounts-qa.yml file of the config server. If you invoke the http://localhost:8080/api/build-info endpoint, you should be able to see the build version as 2.0 as expected.
 *   This confirms that our Accounts microservice is perfectly integrated with the config server and we have moved all the properties that we need to externalize from our Accounts microservice business logic to a centralized location which is the classpath location of the config server.
 *   The very same changes we need to do for the Loans and Cards microservices. We need to delete the application_qa.yml and application_prod.yml files from the resources folder of the Loans and Cards microservices. We need to add the dependency related to Spring Cloud Config Client in the pom.xml file of the Loans and Cards microservices.
 *   We need to open the application.yml file of the Loans and Cards microservices and define the below configurations/properties.
 *   . spring.config.import: 'configserver:http://localhost:8071/'  - Like this we are passing the url details of the config server to the Loans and Cards microservices.
 *   . spring.application.name: loans and spring.application.name: cards respectively. This is the name that we are going to use to establish a link between our Loans and Cards microservices and the config server.
 *   . spring.profiles.active: qa. This is the profile that will be activated by default during the startup of the Loans and Cards microservices.
 *   . You can always change this behavior from an externalized configuration with the help of command line arguments or environment variables or JVM arguments like we discussed earlier.
 *   . With this, we have successfully created a config server and right now we are able to read the configurations from the classpath location of the config server. We have also established a link between our individual microservices and the config server.
 *   . We have moved all the properties that we need to externalize from our business logic to a centralized location which is the classpath location of the config server.
 *   . We have also tested the same by activating different profiles from an external configuration and see if our microservices are able to fetch the relevant configurations from the config server.
 * Like this, we have successfully integrated all our microservices with the Spring Cloud config server. Right now, all the configurations/properties that we need to externalize from our business logic are stored in a centralized location which is the classpath location of the config server.
 * Regardless of how many microservices you have, 3 or 100, the process of integrating them with the config server is the same. You need to follow the same steps that we have discussed for the Accounts, Loans and Cards microservices. But as of now, we are storing all these externalized configurations in the
 * classpath location of the config server. This approach may work for few projects and few other projects may not like this approach because you are trying to store all your externalized configurations inside the classpath of the config server. If someone has access to the source code of the config server then  they can
 * definitely see all the configurations that you have stored in the classpath location of the config server. This is a security concern. So, we need to think of a better approach to store all our configurations in a centralized location. That's why we are going to see the next approach which is storing all our configurations in the file system location
 * so that we can separate the configurations from the source code of the config server. We will see how to do this next.
 * */
@SpringBootApplication
@EnableConfigServer // To enable th config server functionality
public class ConfigserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigserverApplication.class, args);
	}

}
