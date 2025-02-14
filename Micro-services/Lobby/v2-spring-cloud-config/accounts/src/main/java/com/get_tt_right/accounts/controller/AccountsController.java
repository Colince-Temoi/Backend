package com.get_tt_right.accounts.controller;

import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.dto.AccountsContactInfoDto;
import com.get_tt_right.accounts.dto.CustomerDto;
import com.get_tt_right.accounts.dto.ErrorResponseDto;
import com.get_tt_right.accounts.dto.ResponseDto;
import com.get_tt_right.accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/** Updates  as on 13/02/2025
 * 2. Generate Docker images for my microservices and the config server.
 * - We are regenerating the docker images of accounts, cards and loans as well because we made a lot of changes related to the configuration management. You can call them as V2. But for the config server since it will be our first, we can call it as V1. For the same,  in order to generate a docker image for the accounts microservice using Google Jib, we need to run the maven command from the location where the pom.xml file of accounts microservice is present. Just right-click on the Accounts application folder and open the terminal Or you could just do this in your IDE anyway if you have your project already opened. Here you need to run a command which is 'mvn compile jib:build' This will initiate generating a new docker image for my accounts' microservice with the tag name as V2. Make these changes in the pom.xml file before running the command for all the services you are generating the docker image for i.e., accounts, cards, configserver and loans. Make sure the Jib plugin is also in the respective pom.xml files with the required configurations.
 * - Jib is very fast and super convenient for local systems. That's why we will use it throughout all our sessions. Since we used the command 'mvn compile jib:build', the docker image(s) for all our microservices will be generated and pushed to Docker Hub. They will not be present inside our local Docker installation. If we had used the command 'mvn compile jib:dockerBuild', then the docker images would have been generated and pushed to our local Docker installation and this we would have confirmed by running the command 'docker images' in our local Docker installation.
 * - You can also verify the generated docker images from Docker Desktop in the 'hub' tab. In the local tab, delete the unnecessary images for accounts, cards and loans tagged V1 to save some storage inside your local system. Also, make sure that there are no unused containers - delete them. Now pull the images for cards(V2),loans(V2),configserver(V1) and accounts(V2) into your local. You can do that using the Docker Desktop GUI or using docker commands, it up to you mate. You could also choose not to pull them by yourself because when you will be running your docker-compose file even without having these images inside your local system, they will be downloaded/pulled from Docker Hub/the remote repository. It's like storing them inside the remote repository and anyone can use them because we have made them as public. If you had used the command 'mvn compile jib:dockerBuild' to generate the images then, to push them you need use the command 'docker image push docker.io/colince819/accounts:V2'. 'docker.io' because we are pushing them into the dockerhub followed by what my docker account name is i.e., colince819 then followed by the image name i.e., accounts and finally followed by the tag name i.e., V2.
 *   This image should be present inside your local system then only this 'push' command will work. And also make sure that you are logged into your Docker desktop and your docker is running whenever you are trying to run this command. You can validate the pushed docker images for accounts:v2, cards:v2, loans:v2 and configerver:v1 from the docker hub site or from your docker desktop under the 'hub' tab. For accounts for example you will see we have V1 and V2 in the 'hub' tab. If someone is trying to use either of the tag/version then they will get the respective docker image(s) which we have spun up for that version/tag. With this, we have successfully created docker images for all our services including the configserver and pushed them to Docker Hub. Next, we will try to test our docker-compose.yml file  with the default profile and verify if the changes are working.
 * 3. Use Docker compose file  to start all our containers and make sure that the configuration related changes are working even in the docker environment.
 * - Before we try to run the docker compose command, PLEASES MAKE SURE THERE ARE NO RUNNING CONTAINERS OF CARDS, ACCOUNTS, LOANS, RABBITMQ AND CONFIGSERVER. If there are any running containers or stopped containers please delete them because you need a lot of space and memory inside your local system to run the 5 different containers. Navigate to the folder location where your docker-compose file  is present i.e., docker-compose/default. Now run the command, 'docker compose up -d' and all the 5 services will get started beginning with 'rabbit' service, then 'configserver' then the 3 other services. You can run the command 'docker ps' and you should see a list of running containers. You can see that RabbitMQ started with the status(healthy) because we have provided the 'healthcheck'. Very similarly, configserver started with the status(healthy) because we have provided the 'healthcheck' details. Whereas the 3 other microservices started about a second ago but docker is not sure about there health status because we didn't provide any health instructions on how to check that. But that's fine, whenever the RabbitMQ and the configserver started, then the 3 other microservices will also start automatically.
 *
 * Note: Please make sure in the respective application.yml files of our microservices the below property has the optional flag i.e.,
 *   config:
 *     import: "optional:configserver:http://localhost:8071"
 * Otherwise you will face issues like: I/O error on GET request for "http://localhost:8071/loans/default": Connection refused. Will be trying the next url if available and 2025-02-14 06:56:57 org.springframework.cloud.config.client.ConfigClientFailFastException: Could not locate PropertySource and the resource is not optional, failing
 *  - To make sure the code is not stopping at localhost itself, we need to make sure to mention optional
 * Inside your application.yml of your microservices, please make sure to mention optional in the below property. Based on the logic present inside the framework, the code will try to connect with localhost and other URLs provided in the docker compose file. To make sure the code is not stopping at localhost itself, we need to make sure to mention optional
 * - Like this, any slight mis-configuration will not stop the code from running and will imply you make the corrections and re-generate the docker images then re-run the docker compose file. Costly! Huh! be careful!!
 * - Now we can try test the changes inside our postman. You can invoke the build-info API for any of the microservices arbitrarily, and you should see the build version that you have defined inside the application.yml file present in the config GitHub repository. In our case the response will be 3.0 because we started the containers with the default profile.  Where have we mentioned the 'default' profile details> If you check the 'common-config.yml' file we have mentioned an environment variable like 'SPRING_PROFILES_ACTIVE: default'. Since our environment variable has the highest priority compared to what we have defined inside the application.yml file, my containers will start with the default profile. You can also try to test the endpoint like java-version, and you should get a response like '/opt/java/openjdk' which is the JAVA_HOME path that has been set inside my container. Very similarly if you invoke the end-point 'contact-info' you should verify that we are getting properties from the default profile.
 * - As a next step, we need to test if the automatic refresh of the properties is happening without the restart of the containers. To get started around this, we need to make sure we have the webhook is running inside the GitHub repo like we discussed previously. Unfortunately, whatever webhook session that I created previously, I closed that. Now e have to create one more webhook session by following the below simple steps:
 *  . Go to the website HookDeck.com click the developers tab >> Click the link 'Hookdeck console under the 'tools' section >> A new window will be opened  i.e., 'https://console.hookdeck.com/' >> At the very top of this page, locate a link 'Add destination' that is if you have no 'source' already. >> If you have the 'source' already  then you are good to go. Otherwise, you could also add a new source if you wanted to do so as we discussed earlier. But let's use the existing one. You could also delete an existing source. All this flexibility you have from this page. >> You should see something like 'source hkdk.events/rrto11afrn49v4 -> /monitor Disconnected >> Now, we will re-use this source, and we just have to reconnect to it by running the login command i.e.,'hookdeck login --cli-key 0vdvw41seozahjf6fuobpkcihr227mynmuvlz6ke8a66ql13b6'. Happilly you should see a response like: '> The Hookdeck CLI is configured with your console Sandbox' If you get for example a 401 Unauthorized error, in the case where you decided to create a new 'source' It's because the previous session that you were using is still present in your local system somewhere in the cache.
 *    To resolve this, run the command 'hoodeck logout' command  which should log you out of the previous session and will clear any credentials that I have inside my local system. Now if you run the login command again, this time it should be successful. As a next step run the hookdeck listen commad i.e., 'hookdeck listen 8071 source --cli-path /monitor ' and yes! you should be happily reconnected!! With that, you can now try to change a property inside accounts.yml file and see if the automatic refresh is happening or not. Change the message property from ' "Welcome to EazyBank accounts related local APIs " to '"Welcome to EazyBank accounts related docker APIs " Once you are done with these changes and check inside your terminal where you logged in to the hookdeck session, you should see a POST request with a 200 status code. You can also see this in the hookdeck site and even in your GitHub repo in your webhook. This is a clear indication that the refresh attempt happened successfully! If you get a 500 error, haha, sad! To understand the issue incase of a 500 error, check with the log files of your configserver container either via the logs explorer or by going to the terminal of your configserver instance.
 *    There you can see that it is trying to connect to RabbitMQ with the default connection details  like 'localhost:5672' You will see a string like 'Attempting to connect to: [localhost:5672] ' This will not work because RabbitMQ is not started withing the same container of configserver. It started as a separate conatiner with a separate service name. This indicates that we missed /overriding the RabbitMQ connection details inside the docker-compose file. You may have a question like, How comes other functionality are working like, we tested accounts ms APIs and we are getting a successful response? Let's try to check the same. If you try to invoke the endpoint 'http://localhost:8071/actuator/health' you can see we are getting the response like:
 *    {
 *   "status": "DOWN",
 *   "groups": [
 *     "liveness",
 *     "readiness"
 *   ]
 * }
 * - This means that the overall health of our configserver service is down. Whereas if I try to invoke the endpoint 'http://localhost:8071/actuator/health/readiness' you should get a response like:
 {
 "status": "UP"
 }
 * Crazy, haha, If you invoke the one for liveness, you should get a response like:
 * {
 *   "status": "DOWN"
 * }
 * - It's like saying you are 'Dead but ready' interesting.  Yeah! it happens. I am getting the status for readiness as 'UP' because according to the configserver service, RabbitMQ is an optional setting and that's why it went ahead and started and its trying to  accept the requests. With this reason, our accounts, loans and cards microservices started since the 'readiness' check is passing i.e., 'UP'.  To fix this issue, it is very simple, we need to provide the RabbitMQ connection details for all our containers inside the docker-compose file. For this, we have a service 'microservice-base-config' present inside common-config.yml file and this service is being extended/imported in our microservices(accounts, loans,configserver and cards) that need the 'rabbit' service configurations. That's why we have to define environment variables inside 'microservice-base-config' service present inside common-config.yml file. The important environment variable that we need to override is the hostname of RabbitMQ. This we have done using ' SPRING_RABBITMQ_HOST: "rabbit" '
 * As of now, inside the application.yml files of all our microservices, we have:
 * spring.rabbitmq:
 *     host: 'localhost'
 *     port: '5672'
 *     username: 'guest'
 *     password: 'guest'
 * - These are the defaults. The port, username and password we should be fine, no need to override them. But if you want to override them, you can do so based upon the criticality of your application. But, for now, we are not going to override them. We will just use the defaults. We are only overriding the hostname of RabbitMQ as discussed above. We override the host name with the service name of RabbitMQ i.e., 'rabbit'. All my containers should now be ble to connect with 'rabbit' service because they all started inside the same network. With this the 500 issue should get resolved successfully. But you will have to docker compose down to delete all the containers started previously >> docker compose up -d and this should start all your containers again with the new RabbitMQ connection details that we have provided. You can also see the logs of configserver if the connection is established successfully or not. If you are comfortable using the logs explorer, then use it if you are the old-school type, then view the logs from the terminal.
 *  In the logs you can see something like this: ' Attempting to connect to: [rabbit:5672] ' You can use the log explorer feature to search this string. You can also search for the string like, 'created new connection ..."  and you should see a log like, 'Created new connection: rabbitConnectionFactory#6d2db15b:0/SimpleConnection@320efff5 [delegate=amqp://guest@172.18.0.2:5672/, localPort=54646]' which implies that a new connection was created successfully. Now if you try to test the health of the config server by invoking the endpoint, 'http://localhost:8071/actuator/health' you should see an output like:
 *  {
 *   "status": "UP",
 *   "groups": [
 *     "liveness",
 *     "readiness"
 *   ]
 * }
 * - You can also invoke the readiness endpoint, 'http://localhost:8071/actuator/health/readiness' and you should see an output like below:
 * {
 *   "status": "UP"
 * }
 * - You can also invoke the liveness endpoint, 'http://localhost:8071/actuator/health/liveness' and you should see the output like:
 * {
 *   "status": "UP"
 * }
 * - With this, all our containers should get started properly as defined and as expected. This you can confirm from the logs as well.
 * - Before invoking the contact-info API for accounts microservice we had a message property with the value '"Welcome to EazyBank accounts related local APIs " but after invoking it, we will get the value '"Welcome to EazyBank accounts related docker APIs " because all the services have been restarted, and we are getting the latest property as defined inside the GitHub repository. What we can do is, we can do a DML to this property inside our GitHub repo for the default profile of accounts microservice accounts.yml file. We will now change it from existing value, '"Welcome to EazyBank accounts related docker APIs " ' to '"Welcome to EazyBank accounts related local APIs " '.  Meanwhile make sure the webhook related terminal is running and the session is not closed.
 *   In your terminal you should see a 200 status for the POST request. The same you can see both in wour webhook in GitHub and in your hookdeck console website. You should be proud of yourself because this is working!! Hurreeey! Now validate inside your postman that the changes are reflecting automatically by invoking the contact-info API of your accounts' microservice. Yeey!! previouslu we had ' "Welcome to EazyBank accounts related docker APIs " and now we have the current change which is '"Welcome to EazyBank accounts related local APIs ". This indicates that the refresh functionality is working automatically without invoking any URL manually. This confirms that our docker compose set-up is working for the default profile
 * - Now, let us quickly set up the same for 'prod' and 'qa' profiles as well and validate them if everything is working.
 *
 * Preparing docker-compose files for qa and prod profiles
 * --------------------------------------------------------
 * - As of now we have the docker-compose file created for the 'default' profile under the default folder. The very similar kind of docker compose file we want to create for 'prod' and 'qa' profiles so that based upon your requirements you can always try to run the corresponded docker-compose file. Before creating a new docker compose file, first stop all the running containers from the terminal where you executed the 'docker compose up' command. Run 'docker compose down' - This will stop and delete all the running containers/services defined inside my docker-compose file from my system.
 * - Creating docker-compose file for the new profiles is very easy! Just copy the content inside the default folder into a new folder named docker-compose/qa as well as docker-compose/prod. Do you know how many changes I have to make inside the docker-compose file(s) that I have pasted inside the docker-compose/qa and docker-compose/prod folders? Haha! Only one fuckin! change which is under the common-config.yml file and that change is to modify the environment variable 'SPRING_PROFILES_ACTIVE: default' to 'SPRING_PROFILES_ACTIVE: prod' and 'SPRING_PROFILES_ACTIVE: qa'. This way, WITH JUST ONE ENVIRONMENT VARIABLE WE ARE TRYING TO CONTROL THE BEHAVIOR OF ALL OUR CONTAINERS/MICROSERVICES EXTERNALLY! ISN'T THIS BEAUTIFUL IMAGINE WE ARE USING THE SAME DOCKER IMAGE(S) TO RUN THE CONTAINERS IN QA ENVIRONMENT, PROD ENVIRONMENT AS WELL US INSIDE THE DEFAULT/LOCAL ENVIRONMENT. THIS IS THE POWER OF DOCKER IMAGES AND DOCKER CONTAINERS ALONG WITH THE SPRING BOOT FRAMEWORK WE ARE ABLE TO USE THE SAME IMMUTABLE DOCKER IMAGE ACROSS ALL THE ENVIRONMENTS.
 *   IF YOU HAVE ANY SPECIFIC REQUIREMENTS BASED UPON YOUR ENVIRONMENT FOR EXAMPLE; INSIDE THE 'PROD' ENVIRONMENT, IF YOU WANT TO GIVE MORE SPACE AND RAM FOR YOUR CONTAINERS YOU CAN TWEAK THAT INSIDE THE COMMON-CONFIG.YML FILE. LIKE FOR EXAMPLE, INSTEAD OF 700MB, YOU CAN GIVE MORE MEMORY. THIS WAY WE HAVE THE FREEDOM TO MAINTAIN DIFFERENT REQUIREMENTS AND CONFIGURATIONS FOR VARIOUS ENVIRONMENTS. You can use PT wisely here to understand more things that you can specify inside the common-config.yml file that you can controll externally!
 * - Now we have two more different docker-compose.yml files for qa and prod. You can test one or all of them to make sure that everything is working as expected. Just navigate to the respective folder where your qa or prod docker-compose.yml file is present and run the command 'docker compose up'. Also make sure your hookdeck session is up and running in order to test the automatic refresh of the configuration properties present inside the GitHub repo. After running the docker compose up command, make sure to always check with the logs of your containers to verify that the containers have started successfully and then post that you can try testing the various APIs. Hurreey!!
 * With this, we have successfully tested all the configuration management related changes both locally and using the docker compose. All the changes and discussions you can find in the GitHub repo for any doubts. And that's the foundational discussions about configurations management.
 * */

@Tag(
        name = "CRUD REST APIs for Accounts in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE account details"
)
@RestController
//@AllArgsConstructor
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated // This annotation will tell to my spring boot framework to perform validation on all the REST APIs that I have defined in this AccountsController.
public class AccountsController {

    private final IAccountsService iAccountsService;

    @Autowired
    private Environment environment;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;
    public AccountsController(IAccountsService iAccountsService) {
        this.iAccountsService = iAccountsService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new Customer &  Account inside EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        iAccountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch Account Details REST API",
            description = "REST API to fetch Customer &  Account details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                               @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                               String mobileNumber) {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @Operation(
            summary = "Update Account Details REST API",
            description = "REST API to update Customer &  Account details based on a account number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Account & Customer Details REST API",
            description = "REST API to delete Customer &  Account details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                                @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(
            summary = "Get Build information",
            description = "Get Build information that is deployed into accounts microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Get Java version",
            description = "Get Java versions details that is installed into accounts microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Get Contact Info",
            description = "Contact Info details that can be reached out in case of any issues"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    /** Any client that invokes this REST API, can get the contact information.
     * In order to write the business logic inside this method, first we need to make sure we are injecting the bean of AccountsContactInfoDto record class.
     * This is a record class that we have created to map all the properties that we have defined inside the application.yml file.
     * Now inside the body of this method, we are returning the entire object of AccountsContactInfoDto record class.
     * This way, you can create 100s of properties inside application.yml and for all of them you can define Java fields inside a Dto class - Whether you are following a Record class or a normal Pojo class. It is completely up to you.
     * But please make sure the field names in the Pojo/Record class should match with the property key names that you have defined inside the application.yml file. Also, the Return data types of the fields should also match with the property value data types that you have defined inside the application.yml file.
        * This is how we can read the properties from the application.yml file using the @ConfigurationProperties annotation.
     * Run the accounts' microservice and hit the REST API /contact-info to get the contact information. This information can be used by my client application in case of any issues.
     * Here we are just passing the dto object directly in the response but in real projects its up-to you on how to read the data and utilize it present in the dto object. This is just a Demo on how to map multiple properties from application.yml to a single Pojo class and that's why we have used this 3rd approach which is @ConfigurationProperties annotation.
     * Since this is a more mature approach, Spring team recommends the usage of this approach compared to @Value annotation.
     * Of course, we need to use Environment interface to read the environment configurations. But in real applications, we will not be having multiple environment configurations/properties. They will be handful, just 2 or 3.
     * Normal properties and configurations that you may need to configure may be multiple/many and that's why using this 3rd approach which is @ConfigurationProperties annotation is recommended and is going to make more sense using.
     * As of now, we have explored all the 3 approaches but here I have a question for you. As of now, we have created an application.yaml, and we have defined all the properties. What if I want to have different values for different environments?
     * Maybe I want to follow, whatever values I have defined inside the application.yml file for the development environment and I want to have different values for the QA environment and production environment. How do I achieve this?
     * If this is my requirement, to maintain different values for different environments, then definitely all the 3 approaches we have discussed above are not going to be helpful. We need something advanced that is supported by the Spring boot framework.
     * Check the class level documentation of this class to see how we can achieve this requirement.
     * */
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }

}
