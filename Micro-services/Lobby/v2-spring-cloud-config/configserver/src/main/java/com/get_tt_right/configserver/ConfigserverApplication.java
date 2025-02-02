package com.get_tt_right.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/** Update as of 1/31/2025
 * Maintaining configurations into a file system location
 * ------------------------------------------------------
 * - Using that same location we can try start our spring cloud config server
 * Q. Why some projects prefer to use File system approach? Using this approach you are going to maintain all the required properties inside the server location where your microservice is deployed.
 * Advantages
 * ----------
 * - Your server admins can enforce security restrictions like: No one can open/see the contents of a particular folder except your config server application.
 * This is the main reason as to why few projects prefer to use/follow the file system approach.
 *
 * How to store all our configurations into the file system location
 * ------------------------------------------------------------------
 * - Take all the yml files present inside the config folder and move them to a folder in the file system location. i.e., documents/config folder
 * - Open the application.yml file of the config server and update the search-location of the file system where all the configurations are present.
 *   . spring.cloud.config.server.native.search-locations=file:///C://Users//pc//Documents//config
 *   After the file: prefix, make sure to mention 3 slashes (///) and in between the folders mention double slashes (//). This is the syntax to mention the file system location.
 * - With this, now my config server will point to a new search location which is following the file system approach.
 * - Stop all your microservices including the config server and restart them starting with the config server.
 * - First, you can start by testing your config server urls in the browser. For example, http://localhost:8071/loans/default and check if you are able to see the configurations.
 *   Yes! happilly you should be able to see the configurations. If keen you can also see the 'name' key has a value which points to the fully qualified path of the file where the configurations are  being read from.
 *   This is the proof that the configurations are being read/loaded successfully from the file system location.
 * - As a next step, lets try testing the integration between our individual microservices and the config server by randomly invoking the http://localhost:port/api/contact-info endpoint of the 3 microservices via postman.
 *   . Yes! it worked!!
 * - And that's all you need to know about the file system approach of maintaining configurations.
 * The only changes we have done are in the application.yml file of the config server. We have updated the search location to a file system location where all the configurations are present.
 * The profile.active is set to native ONLY!
 *
 * Reading configurations from GitHub Repository
 * -----------------------------------------
 * - We will understand how to store all our configuration properties inside a GitHub repository and  using the same my config server can try to load and read all the properties during the startup.
 * - Its the most recommended approach because when you try to store your properties inside GitHub repository you will get multiple advantages i.e.,
 *  . You can properly secure your GitHub repo so that no one can access the configurations.
 *  . It is going to support versioning and auditing of the configurations. In future down the line after like 1/2 years if you want to understand what is the property that we used to have, you can always see the history in the GitHub repository.
 *    For other approaches like classpath and filesystem, it is nearly impossible to track such versioning changes.
 * - To start with this approach, 1st we have to move all our configuration files into the GitHub repository.
 *   Behind the scenes, I have created a repo i.e., /configs and I have moved all the configuration files related to our microservices into this repository. If you open any one of them, you will notice it holds whatever we have been discussing in the previous sections.
 *   In real time, the repo has to be private which means our config server has to authenticate with the GitHub repository whenever it is trying to connect during the startup.
 *   We will see how to provide security details whenever we are trying to interact with Private GitHub repo from your config server.
 * - For now, lets try to connect with the public GitHub repo.
 *   . Copy the value of the Https url
 *   . In the application.yml file of config server, as of now we have profile.active=native, which is only applicable to class path and file system approaches. we have to change it to 'git' if we are using the GitHub repository approach.
 *   . spring.profiles.active=git
 *   . Now, under the spring.cloud.config.server.xxx, we have to specify 'git' and not 'native' as we did in the previous approaches. Then under this, we need to define one more child element which is 'uri' and set the value to the copied GitHub repo url.
 *   . After mentioning this Uri details, we should mention one more child element under the git which is 'default-label' and set the value to 'main' because the default branch of the GitHub repo is 'main'.
 *     Like you know, inside the GitHub repo we will be having a label name for our branches.
 *     This property is going to helpful if you have multiple branches in your GitHub repo and you want to read the configurations from a specific branch and avoid any confusions/conflicts on which branch your config server has to connect.
 *   .  I will also mention one more property which is timeout. This property is going to help us to specify the timeout in seconds. If the config server is not able to connect to the GitHub repo within the specified time, it will throw an exception.
 *    This means that my config server should only wait for a maximum of 5 seconds to connect to the GitHub repo. If it is not able to connect within 5 seconds for whatever reason, it will throw an exception immediately.
 *    This will allow my config server to fail immediately and that will give an exception to the operations team or the developers team to understand if the GitHub repository is down or if there is any issue with the network or if the configurations that we have provided that we have done related to the uri or any other properties is wrong or not hence we can validate and correct it.
 *   . The next child property we need to specify is 'clone-on-start' and set the value to 'true'. This property is going to help us to clone the configurations from the GitHub repo to the local file system location of the config server.
 *    If you don't mention this value as true, the cloning of the GitHub repos will happen only when the very first request comes to your config server and this may result into some issues like, okay, your config server may start properly
 *     but when the first request comes to your config server like in the form of accounts or cards or loans microservice then it cannot clone and cannot read the configurations and in such scenarios it is going to cause some issues in the respective microservices.
 *   That's why we need to make sure we are cloning the GitHub repo during the startup of the config server itself
 *   . The last property that I need to specify here is 'force-pull' and set the value to 'true'. This property is going to help us to forcefully pull the configurations from the GitHub repo to the local file system location of the config server.
 *   . If the value is set to true, it is going to pull the latest configurations from the GitHub repo to the local file system location of the config server.
 *     Sometimes you may have done some local changes inside the local repo that got cloned inside your config server. By mentioning this force-pull as true we are telling to override all the local changes whenever you are trying to start or restart your config server.
 *     This will make sure that your config server will always read the properties from the master location which is  GitHub repository.
 *  - After making these changes, we should be code from the code changes perspective. We have to stop all the microservices and the config server and restart them starting with the config server.
 *  - On restarting the config server, you should also clearly see in the console logs that the config server is trying to use the git profile. See the log, "The following 1 profile is active: "git""
 *  - Next, start the other microservices and test the integration between the individual microservices and the config server by randomly invoking the http://localhost:port/api/contact-info endpoint of the 3 microservices via postman.
 *  - Before doing this test, first lets try to understand if our config server is reading the configurations from the GitHub repo or not. For this, you can try to hit the http://localhost:8071/loans/default url in the browser and check if you are able to see the configurations.
 *  Yes! you should be able to see the configurations. If keen you can also see the 'name' key has a value which points to the fully qualified path of the file where the configurations are  being read from - A GitHub url link.
 *  This is the proof that the configurations are being read/loaded successfully from the GitHub repository.
 *  - As a next step, lets try testing the integration between our individual microservices and the config server by randomly invoking the http://localhost:port/api/contact-info endpoint of the 3 microservices via postman.
 *  . Yes! it worked!!
 *  - And that's all you need to know about the GitHub repository approach of maintaining configurations.
 *  Apart from the 3 approaches we have discussed above,are there any other approaches that we can use to maintain configurations? Reason, different projects may have different requirements. You can understand more about this by looking at the official documentation of the Spring Cloud Config Server.
 *   https://spring.io/projects/spring-cloud-config >> Click on the 'Learn' tab >> Click on the 'Spring Cloud Config Reference' link - latest >> Click on the 'Spring Cloud Config Server' link . If you scroll through it, there is a lot of information about Spring Cloud config server which if you asked me
 *   to talk about, I would do it for 10 more hours. This subject is very huge and that's why if you have any questions any time, visit these docs and check out the information regarding what you are looking for about the Spring Cloud Config Server.
 * - Suppose, if you are trying to look for information on how to use a private GitHub repository then you can click on the Environment Repository section >> Git Backend. If you scroll down you will see a section explaining about Authentication. Here you can see we have the information on how we can pass the
 * username and pwd of your GitHub repo. If you are not comfortable mentioning the username and pwd, you can also use the ssh key approach/standards. Please normalize reading the official documentation for more information.
 * Very similarly, if you are using Aws Code Commit, just like the way we are using GitHub repository, in such case you will see a section that can guide you through.  Similarly, the same applies to Google Cloud Source Repository, Bitbucket, etc.
 *  Git SSH configuration using properties - There is  a section also that explains how to achieve such stuff. Like what is your uri,hostKey,hostKeyAlgorithm,privateKey, ...etc
 * - Post that, if you can scroll down more, we have various approaches explained like File System Backend, Vault Backend, CredHub Backend, Aws Secrets Manager Backend, Aws Parameter Store Backend, JDBC Backend (This is in case if you may have plans to use the DB to store all your properties), ...etc.
 * - This way, the official documentation has a lot of information. Whenever you have some requirements which we have not discussed the docs will be a great resource for you to get started. Be empowered always such that you don't have to depend on others and can always refer to the official documentation to get most of the information you may be looking for.
 * - Whatever course that you may take wherever, they will sought of give you a brief overview of the concepts once if you are clear about the concepts, you have to refer to the official documentation most of the time for any complex scenarios that you have or for any scenarios which are not covered in the course. That's the cheatsheet to promoting yourself from Junior Developer to  Senior Developer.
 * - Always remember your number of years of experience doesn't matter, its the knowledge that you carry that matters.
 *
 * Encryption and decryption of properties inside the Config server
 * ----------------------------------------------------------------
 * - As of now, we have all the configuration properties inside the GitHub repo. We are in a good position because we are following the most recommended and production standard approach.
 * - But sometimes we may want to store our properties in an encrypted format. Currently, inside our GitHub repo, we stored all our properties in a plain text format. What if we have a scenario where you want to store the property values in an encrypted format
 *  so that even if someone has access to your GitHub repo they cannot see the actual values of your sensitive properties. That's why it is always advisable to encrypt the sensitive properties like passwords, url details, folder structure details, etc.
 * - We will understand how Spring Cloud config server is going to help in this scenario. Steps:
 * 1. Go to the application.yml file of the config server and add a new property called 'encrypt'. Post that, mention a child element called 'key'. To this encrypt.key property, we need to provide what is the secret that my Spring Cloud config server can use to encrypt and decrypt the properties.
 *    Always remember, whenever you are trying to provide a secret key it has to be super complex so that it is going to be very tough for the hackers to guess it.  Don't use simple keys like 12345 or password or ABCDEF or any other simple keys. Always use a complex key because the simple keys are very dangerous
 *    to use inside any application. We should use a Key which is very complex to guess. It should be a completely random key. You can create your own key as well. There are many websites online which can help you generate a complex key for your encryption process. The key can be any value, but please make sure it is complex in nature.
 * Once you have completed step 1 and mentioned the property, it is going to expose encrypt and decrypt related APIs using which it is going to encrypt or decrypt our properties.
 * 2. Stop all your applications including the config server and restart them starting with the config server. For now, just restart the config server. Once started, in the postman under the configserver folder we have 2 requests with the names encrypt and decrypt respectively. If you open the encrypt request you will see the URL is http://localhost:8071/encrypt and the method is POST.
 *    To this endpoint, you can pass any kind of plain text value in the request body, and it will return the encrypted value. If you open the decrypt request you will see the URL is http://localhost:8071/decrypt and the method is POST.
 *    To this endpoint, you can pass any kind of encrypted value in the request body, and it will return the decrypted value.
 *    This encryption/decryption process is going to use the secret key that we have provided in the application.yml file of the config server.
 *    Now, lets try to encrypt the email property value of accounts microservices prod profile. For this, open the accounts-prod.yml file and copy the email property value. Open the postman and go to the encrypt request. In the request body >> Check the Row option radio button as well as the Text option in the dropdown (Reason, we are just trying to send a simple text value) >> paste the email property value and click on the send button.
 *    In the GitHub repo,if you open the accounts-prod.yml file, you will see the email property value is in plain text and anyone with access to the repo can see the value. Think like I want to encrypt this inside the GitHub repo so that no one can see the actual value.
 *    In such scenarios, first, you need to understand what is the encrypted value of your plain text value.  That's why I am trying to take the plain text value of the email property and encrypt it using the encrypt API of the config server.
 *    On clicking the send button you will see the encrypted value of the email property value. Copy this encrypted value and go back to the GitHub repo and replace the plain text value with the encrypted value.
 *    I mean, since we have a requirement to encrypt this email value inside the GitHub repo, we need to replace the plain text value with the encrypted value.
 *    Please make sure it is in the double quotes just like the plain text value was in the double quotes. But here there is a challenge for my Spring Cloud config server?! How is it going to differentiate between a plain text value and an encrypted value? To help Spring Cloud config server around this scenario, I need to make sure that for the encrypted values I have to prefix the value with {cipher}.
 *    Now, whenever Spring cloud config server sees this {cipher} prefix, it will understand that this is an encrypted value, and it will try to decrypt it using the secret key that we have provided in the application.yml file of the config server during the time when it is trying to send these properties to the actual microservice. Like this, even if someone is trying to see my properties inside the GitHub repo they will only see the encrypted values and not the actual values. And I will be fine with that anyway haha!
 *    Because they cannot know what the encrypted value is until unless they know the secret key whatever I have provided inside the application.yml file of the config server. In real production applications, the secret key can be configured with the help of environment variables or CLI arguments or any other approach.
 *    Now, commit the changes directly to the main branch.
 * 3. Restart the configserver so that it reads these latest values. Once started, we can confirm/validate whether the configserver is able to decrypt the encrypted value(s) or not by invoking the http://localhost:8071/accounts/prod endpoint in the browser. Yeey! it worked!! You should be able to see the decrypted value of the email property. This is the proof that the config server is able to decrypt the encrypted value and provide the actual value to the microservices.
 *    By the time config server is returning the response with the properties to the respective clients/microservices it is going to decrypt the encrypted values with the help of the secret key that we have provided in the application.yml file of the config server.
 * 4. Now, as a next step, lets try starting our Accounts microservice and validate if the microservice is receiving the email value in a plain text format or not. Inside the postman invoke the http://localhost:8090/api/contact-info endpoint of the accounts' microservice. Yeey! it worked!! You should be able to see the decrypted value of the email property. This is the proof that the microservice is able to receive the actual value of the email property as a plain text value.
 *    This way, we can store any number of sensitive properties in an encrypted format inside the GitHub repo and make sure that no one can see the actual values of the properties.
 *  Just like the encrypt endpoint, we also have a decrypt endpoint. If you have any encrypted value and you want to know what is the actual value of the encrypted value, you can always use the decrypt endpoint of the config server. You can try testing this in the postman. You can pass the encrypted value in the request body, and it will return the actual value of the encrypted value. While passing the encrypted value in the request body, make sure to check the Row option radio button as well as the Text option in the dropdown.
 *   If you click on the send button, you will see the actual value of the encrypted value. This is how you can encrypt and decrypt the properties inside the config server. Here you don't have to mention the {cipher} prefix before my encrypted value because since you are invoking the decrypt endpoint, config server is going to understand that the developer/client application/enduser is going to pass the encrypted value only, and it will try to decrypt it using the secret key that we have provided in the application.yml file of the config server.
 *   You may have a question like, If someone can easily decrypt my encrypted values using the decrypt endpoint, then what is the use of encrypting the properties? The answer is very simple, like we discussed earlier, inside production applications your config server will work in a very different manner.
 *   Here, we are able to invoke any exposed config server APIs very easily but inside production applications, your platform team are going to deploy your config server behind the firewall of your organization which means, no one can invoke the exposed APIs of config server directly. Only the applications which are deployed within the firewall of the organization can invoke the exposed APIs of the config server/communicate with the endpoints of the config server.
 *   On top of that, if you want to secure your config server even more, you can always use the Spring Security framework to secure your config server endpoints just like any other spring boot application. This way, you can make sure that only the authorized applications can communicate with the config server and no one else can communicate with the config server.
 *   With this, you should now be having clarity on how to encrypt and decrypt the properties inside the config server.
 *
 * */
@SpringBootApplication
@EnableConfigServer // To enable th config server functionality
public class ConfigserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigserverApplication.class, args);
	}

}
