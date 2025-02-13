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

/** Updates  as on 10/02/2025
 * As of now, with the help of config server and other microservices instances, we have been able to test all the configuration related changes inside the local system. As a next step, we need to containerize all our microservices applications including the config server and post that we should try to start all our microservices along with the config server with the help of Docker Compose.
 * That's why in this session we will put effort to make these configuration related changes to work even in the Docker environment. Because with the help of docker containers only, we are going to deploy into the production environments like into K8s cluster. To get started:
 * 1. Write Docker compose files. Once we are clear with the set-up of the Docker compose files then can proceed to generating docker images and containerizing the microservices.
 *   - Actually, we will need to do few more changes in the config server in order to make it work inside the docker compose. That's why we will first focus on preparing the Docker compose files.
 *   - I can prepare a single docker compose file but as of now we have 3 different environments/profiles like default, QA and production. To support these 3, we need to write 3 different docker compose files for each environment so that they will give flexibility to make any changes specific to a particular environment.
 *   - Under the V2-spring-cloud-config folder, create a new directory called docker-compose. Inside this directory, create 3 more new directories named default, qa and prod. Inside each of these 3 directories, I can create docker compose files specific to those profiles or environments. As of now, in our v1-springboot/accounts/ project, we have a docker-compose.yml file which we prepared in our previous sections. Copy it and paste it inside the V2-spring-cloud-config folder inside the docker-compose/default directory.
 *   - As of now in that file, we have defined 3 services, define a services related to config server as well. Just copy one of the services and edit it. easy!
 *   - We know that when all microservices start in the same network as defined in the docker compose file, they will be able to communicate with each other with the help of the service name.
 *   - As a next step, we need to inform our microservices on how to connect with the config server inside a docker environment. For the same, if you go and observe in the application.yml file of any microservice, either cards, loans or accounts, there is a property that were trying to use in our local development environment to establish the link between configserver and the individual microservices. This property is spring.config.import.
 *     To this property, we mentioned a value which is "optional:configserver:http://localhost:8071". This same configuration we need to provide inside the docker compose files. You may have a question like; why can't my accounts microservice or any other microservice directly use what we have already mentioned in there respective application.yml files? They cannot use this property because we have mentioned  we have mentioned 'localhost' in the value. For example, if my Accounts docker container tries to use the localhost
 *     to communicate with the config server, this will unceremoniously! not going to work because my accounts microservice is going to start in its own isolated network. And, when I say localhost, it will try to connect with the config server within its own network which will never be successful. That's why we need to externalize this property inside the docker compose files and we need to override the values inside the application.yml files with the help of environment variables. Check the docker compose files to understand what how this has been done. It is very straight forward to do this.
 *     Under the accounts service or any other service, I am going to mention a child element called environment inside which we can define any number of environment variables. You know the rules on how to convert an application.yml file property value to an environment variable. Words should be converted to upper case and words should be separated by an underscore. To that environment variable, you need to pass the value of my config server details. I gave the value as "optional:configserver:http://localhost:8071" but we know very well this won't work.
 *     Remove the 'optional' string because we want our accounts microservice to fail in case it is not able to connect with the config server. In local development environment you can use it but when deploying your microservices into higher environments using docker or docker compose or K8s, it is a good standard to not mention the 'optional' string because most of the time our applications will depend on the config server to provide the configuration details.
 *     The 'configserver' prefix - that we need to follow always. Now, in place of 'localhost' mention the host name as the service name of the config server. I am going to use the service name, 'configserver'. In this manner my account microservice should be able to connect with the config server without any problem. Like this, behind the scenes docker will make the communication work because we are trying to start all our containers inside the same network which is 'eazybank' and with that all my services/containers can communicate with each other by using the service name.
 *     Here the service name is 'configserver'. Conicidentally same as the 'configserver' prefix. Don't confuse this. If I had defined some other service name for the config server then, that is what you would be required to mention in place of 'localhost' string but the 'configserver' prefix string should always remain the same.  Regardless of what your service name is, the prefix string should always be 'configserver'. Actually, you can set the service name based upon your requirements.
 *     The next environment variable that I need to pass to my accounts microservice is, what is the profile that it has to consider while starting the microservice application. For the same we need to mention one more environment property which is SPRING_PROFILES-ACTIVE. To this we need to mention/specify the profile that we need activated whenever we try to run this docker compose yml file. Right now you can see that this docker compose file is present inside the default folder which means, whenever someone tries to run this docker compose file, we need to start in a default profile mode.
 *     The next environment variable that I want to provide here is SPRING_APPLICATION_NAME. This should match whatever name that you have considered for the naming convention of the files that you stored inside the config server location. i.e., accounts-prod.yml, accounts-qa.yml and accounts-default.yml files. For example, you can see for accounts microservice configuration files, we are using the prefix 'accounts'. That's why as a value to this environment variable, I am going to mention 'accounts'. We also followed the same in the application.yml file to accounts microservice. The reason why I am trying to mention this same as an environment variable  inside the docker compose file is, there is
 *     a defect inside the spring cloud config server as of 2021. Due to that bug we need to mention this same environment variable, SPRING_APPLICATION_NAME, inside the docker compose file as well even though it is the same name that we have used in the application.yml file. For this, it is worth noting that we are not trying to override the application name with a new value. We are just mentioning this to overcome that bug as a workaround for the defect inside the Spring cloud config server. In future, you can try test the things without mentioning this environment variable. If it work for you, then that will mean that the bug is fixed inside the spring cloud config server future versions. Otherwise, you can continue to mention this environment variable inside the docker compose file under the environment variables section.
 *     Make sure to mention these same kind of environment variables that we have defined for accounts service under all the other services i.e., loans and cards. Now, we have the docker compose file almost ready but there is a challange here. As of now we have a condition inside our microservices which is, before the individual microservices try to start we need to make sure that the config server is completely started and ready to accept the requests. But with the current set-up, if I try to run the docker compose up command, docker is going to create the containers in the same order as we have defined the services inside this docker-compose.yml file i.e., configserver, accounts,loans and finally cards. The problem with this set-up is, Docker will not wait for the configserver to start completely, it
 *     will just initiate the start process of the config server, and it will immediately jump into accounts,loans and cards and this may create issues because, if accounts microservice tries to start before the configserver is readily available then it will be an issue. That's why we need to make some changes inside this docker-compose.yml file to communicate to the docker on how to identify whether my config server is completely started or not. Once we provided that information, we should also mention the dependency information of config server under all our accounts, loans and cards services. That way my docker will have complete information and during the docker compose up command it will start the config server first and on completely starting with good health to accept the requests the only it is going to start the accounts, loans and cards microservices.
 *     To make these changes, first we need to understand two important concepts; Liveness and readiness. Check slides for a discussion on this.
 * -During the scale up scenario, the responsibility of the of platforms like K8S is not only to create a container and leave it! They should make sure it is working properly and started with proper good health, otherwise it will try to scale up one more instance. First, as discussed in this slide, you will understand what liveness and readiness are. Liveness is a concept using which we can send signals from the container or application indicating whether my application/container is running properly or has some health issues. If the output is that the container is alive and that it is working properly, then there is no action required because the current state/health is already good. If the container is dead, then an attempt should be made by the products like K8S or any other products to heal the application by restarting or by creating a new container.
 *   So, it is the responsibility of the products like K8S or any other platforms to regularly invoke these liveness probe to get the health of my running container. A readiness probe is used to know whether the container or app that we are trying to probe is ready to start receiving the network traffic from its clients. Sometimes, especially during the startup your container maybe alive and the output from the liveness probe maybe positive, but it may not be ready to accept any new traffic. It might be doing some background work, or it might be warming up to accept the requests. Behind the scenes it might be doing some DB initializations and all that. So to get ready, and accept the traffic from the clients, your container may take some time especially during the startup time. So to avoid any scenarios where my K8S or any other platforms trying to send requests
 *   to my container before it is completely ready, we can invoke this readiness probe to send an output saying that I am not yet ready, please give some more time. This way we can avoid scenarios where the liveness is giving a positive response but the application/container is not yet completely started. This is a very common scenario especially during the startup of the container. So, platforms like K8s will make sure that both liveness and readiness probes are giving a positive response in order to send a request that is being received from the client applications. In simple words, this readiness probe answers a true or false question like, is this container ready to receive the network traffic?  This liveness and readiness concept you may see in many places like, inside Docker, K8S, Cloud environments. Overall, these are just general concepts using which we can make sure that our config server starts completely, and it is accepting the traffic.
 *   post that, we need to try starting the containers like accounts, loans and cards services. To help in these scenarios, Spring boot has some actuator endpoints exposed under the health endpoints. Whenever you are trying to invoke the endpoint like /actuator/health you will receive both of the liveness and readiness indicators in the output. If you are looking for a specific indicator, then you can try to invoke the endpoint like /actuator/health/liveness or /actuator/health/readiness. This liveness and readiness internally give the output based upon the health indicators from the liveness state indicators available inside the Spring Boot framework and very similarly, readiness state indicator. Using these indicators, behind the scenes my Spring boot application can expose the health information by using the actuator endpoint URLs discussed.
 * - As a next step, lets enable these endpoints inside our config server. Make sure inside the pom.xml of the configserver we have the actuator dependency. As a next step, inside the application.yml file of the config server we need to add few properties to enable and expose the health related information. These new configurations we need to mention under the management parent property, just under the same position where we have mentioned the endpoints (plural) child element. The properties that we are creating are, first, using the health element under the management, I am trying to invoke 'readiness-state' under which I am invoking one more child element which is 'enabled' and assigning a value 'true' to it. Similarly, under the health, I am trying to invoke a child element like  liveness-state under which I am trying to invoke 'enabled' and aslo assigning it the valur 'true'
 *   With this, I am trying to tell to my actuator to please enable the health related information which will give me the details about the readiness state and the liveness state. And since we need to read these health information using endpoints, we need to make sure we are mentioning a child element under the 'management' and this child element is 'endpoint' (Singular). Under this 'endpoint' we need to mention one more child which is 'health' under which we have 'probes' under which we have 'enabled' and to this assign the value 'true'. Once we define these properties inside the config server we can try test this inside the local whether the health information of my config server is being communicated correctly or not with the help of actuator URLs. For this, start config server alone and try to invoke the path localhost:8071/actuator/health which will give the overall health details as below:
 *   {
 *   "status": "DOWN",
 *   "groups": [
 *     "liveness",
 *     "readiness"
 *   ]
 * }
 * - Like you can see, we are getting the status as 'DOWN' initially and the reason for this is that if you try to understand the logs, I am getting exception because there is no RabbitMQ server or Container running inside my local system. The RabbitMQ container that I started previously, I stopped it behind the scenes. So make sure to start it again by running the command: docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management which you can easily get in their website. Once the RabbitMQ starts successfully, post that my Config server will also start successfully because it will now be able to connect successfully with the RabbitMQ server.
 * - To confirm that my RabbitMQ started successfully you can again invoke the endpoint http://localhost:8071/actuator/health and you should see an output like the one below:
 * {
 *   "status": "UP",
 *   "groups": [
 *     "liveness",
 *     "readiness"
 *   ]
 * }
 * - This time the status is 'UP'. If you are looking for only the 'liveness' information, you need to invoke the endpoint http://localhost:8071/actuator/health/liveness. Once you invoke this, you will see it is giving the status as 'UP' which means it is giving positive information about it's health whereas 'DOWN' means the container or application is down. Very similarly we can invoke the 'readiness' actuator endpoint i.e., http://localhost:8071/actuator/health/readiness. It is also giving the status as 'UP' which confirms that our config server is ready to accept the requests from the client applications like accounts, cards and loans microservices. This way, we have set up the liveness and readiness inside the config server.
 *   In future whenever we have some dependent components on our service(s) then definitely we need to implement this liveness and readiness concept as we have done for the config server so that by looking at this information only my dependent components will try to start. In the next session, using this information we will update the docker-compose file.
 * - Now we have our config server exposing the liveness and readiness information with the help of actuator.As a next step, we need to make some changes inside this docker-compose files to communicate with docker or docker-compose on how to evaluate whether my config server is in healthy status. For the same we need to create one more child element,'healthcheck' under the 'configserver' service. Under this 'healthcheck' you can see automatically a child element 'test' is created. To this 'test' property we need to give some instructions on how to evaluate whether my config server is started and ready to accept the requests. For the same we are going to assign the value 'curl --fail --silent http://localhost:8071/actuator/health/readiness | grep "UP" || exit 1'
 *   This is a curl command using which I am trying to invoke the path http://localhost:8071/actuator/health/readiness. So, whenever docker-compose tries to start my service 'configserver' it will try to check the health check if started successfully or not by invoking that readiness actuator URL. Once we invoke that actuator URL we can get the output as 'UP' or 'DOWN' and since we only want to consider the status 'UP' as success and otherwise a failure! That's why using this 'grep' command I am trying to search inside the response the value 'UP', If it is there, this indicates that my service started and it's health is perfect, otherwise I am telling to simply 'exit' this health check command which means my docker-compose will consider that as my configserver health is not proper.
 *   After this 'test' command configurations, we need to provide more configurations for this 'healthcheck'. If you stop at this 'test' command alone, my docker-compose will try to run this 'test' command only once. But in the scenarios where my configserver may have taken some good amount of time - maybe 20s -30s - to start the configserver application completely, in such situations we will always get the output as a failure. That's why we need to provide some 'interval','timeout', 'retries', and 'start-period' metrics as child elements of this 'healthcheck' element. With 'start-period' we are trying to tell that, 'please try to execute this healthcheck test command only after 10s. If there is a failure, I am telling using the 'retries' that please do a retry for 10 times with an 'interval' of 10s and inside each check it has to wait for a maximum '5s' to get the response from the URL and if this 'timeout' is reached, I am telling to fail this health check test.
 *   This is what we are exactly trying to achieve with the configured healthcheck child attributes inside the docker compose file for configserver service.  With this now, my docker or docker-compose is smart enough to identify is the service configserver is started completely or not. As a next step, we need to define the dependency details of this 'configserver' service into my accounts, loans and cards microservices. For the same, under this accounts microservice, just under the parent element 'account', I need to mention the child element 'depends-on' and in this 'depends-on' I need to mention the service name on which my accounts service depends on. For our case it's the configserver as of now. If you mention only, depends_on: "cofigserver", your accounts microservice will never wait for your configserver to get completely started and available with the healthy status, instead it will only wait until the config server initiated the starting process and
 *   it will never wait on whether the starting process completed successfully or failed successfully. It will never wait for such output, instead as soon as the configserver starting process is initiated, immediately my accounts microservice will try to start. But this is not what we want, we want our accounts microservice to wait till the config server is started completely with a good health. That's why we need to bring the value 'configserver' as child element to 'depends-on' attribute. Now under this, 'configserver' element, mention a child element which is 'condition' . There are multiple conditions that we have like 'service_completed_successfully', 'service_healthy' and ' service_started'.  If you mention 'service_started' it is going to wait till the configserver service is started, but it will never care for your healthy status which you have defined under the 'healthcheck' 'test' of your configserver service. If you want this 'healthcheck' 'test' to be passed
 *   then only, your accounts microservice needs to be started with the 'condition' service_healthy. Instead of 'service_healthy', in some scenarios we can assign the 'condition' as 'service_completed_successfully'. This was introduced in the latest versions of docker and docker compose and this means that your accounts service will have to wait for configserver service to get started completely. Behind the scenes, it will try to do some checks to make sure that the service is started completely. But of you don't provide the 'healthcheck' related information for the configserver service, then it, accounts service, will never know how to perform the healthchecks that are specific to our configserver service. That's why it is recommended to define your own healthchecks and mention the 'condition' as 'service_healthy'. With this, my 'accounts' service is going to wait until my 'configserver' started with a status/condition service_healthy.
 *   This same dependency information we need to define inside our loans and cards microservices also. Now, we have the complete set-up like; configserver service is going to start first and once the healthcheck is satisfied then only my accounts, loans and cards microservices will start parallely since I don't have any interdependncy between loans, accounts and cards microservices. As a next step, we need to add one more service inside our docker compose which is related to RabbitMQ. We know that our configserver amd all the other 3 microservices will depend on the RabbitMQ since we are using Spring Cloud Bus inside the dependencies. To make this work, we need to create a new service element  with the name i.e., rabbit. You can use any preffered name so long as it make sense. Now lets see the other child elements of this 'rabbit' service and try to see how they mirror the docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management that you can easily find on the RabbitMQ website.
 *    - Under this 'rabbit' service, first I need to specify what is the 'image' name i.e., image: rabbitmq:4.0-management. This value you can clearly see it in the docker run command.
 *    - Next, we need to mention the 'hostname' which is i.e., hostname: rabbitmq. This value you can also easily see in the docker run command. You will notice that, the 'hostname' property for RabbitMQ service is specific to RabbitMQ container and that's why we don't have to mention this for the other services i.e., accounts, configserver, loans and cards that we have defined inside the docker-compose file.
 *    - Post that for 'rabbit' service, we need to mention 2 ports mapping as can also be verified from the docker run command - 5672 and 15672. Why 2 ports? It is because inside RabbitMQ there are 2 components. One will take care of the management of the RabbitMQ and the other one will take care of the core activities. That's why since they are 2 components, they are going to start at 2 different ports.
 *    - Post that, we are also defining the 'healthcheck' related configurations for this service. It's because my configserver, and other microservices are dependent on my RabbitMQ. Until, my RabbitMQ starts completely with a good health, I cannot really start my configserver or any other service. Under the 'healthcheck' attribute of 'rabbit' service, I have a child attribute 'test' with the value 'rabbitmq-diagnostics check_port_connectivity' which is mentioned by the RabbitMQ inside there official documentation. So, whenever we want to test whether the RabbitMQ health is fine or not, we need to run this 'test' command i.e. test: rabbitmq-diagnostics check_port_connectivity, inside the container/service of RabbitMQ.
 *      About, 'interval', 'timeout', ' retries' and 'start_period', these you already know as we discussed earlier.
 *  Now, we have a service definition with the name, 'rabbit', as a next step, we need to define the dependency inside my 'configserver' service  by invoking a child element 'depends-on'  and to it I am going to mention the value 'rabbit'. And since we want to make sure that the rabbit is completely started with good health, we need to make sure the value 'rabbit' now becomes a child element to 'depends-on'. Under this 'rabbit' element we need to mention the child element 'condition' and assign it the value 'service_healthy'. With this, now my 'configserver' will wait for my RabbitMQ to get started and once it gets started, the healthcheck that we have defined the 'rabbit' service should be successful! then only the 'configserver' service will try to start and post that, accounts, loans and cards microservices will get started parallelly.
 *  You may have a question like, my cards, loans and accounts microservices they also depend on this RabbitMQ, then why not mention the 'rabbit' service under the 'depends-on' of accounts, loan and cards?  Its very simple, the reason why I am not mentioning is, we have anyway mentioned the dependency on config inside the accounts, loans and cards microservices. When my configserver is waiting for the RabbitMQ to get started, means indirectly my accounts, loans and cards will also be waiting for this RabbitMQ to get started. That's why we don't have to again mention the RabbitMQ dependency inside the accounts, loans and cards microservices. But if you got time, haha, you can mention as there is no harm in that.
 * - Now, our docker compose file for our default environment is ready!! But before we try to test this docker-compose file, first we need to make sure we are following a standard here! As of now, you can see that our dcocker-compose file has a lot of repetitive information. As you can see in many places I have metioned:
 *   . deploy related instructions.
 *   . Networks related information.
 *   . ...etc
 *  We need to move these repetitive configurations into another common file from where we can try to import so that the content will not be duplicated multiple times. At the same time, if you have the content under a single place, changing to different values is going to be super easy because such modifications you will be performing at a single place but not under all the services in the docker-compose file. That's why as a next step, lets try to optimize this docker-compose file for that.
 *  Before we do forget, make sure to also mention the networks information under the 'rabbit' service as well. If you don't mention that, 'rabbit' service is going to start under a different isolated network and this means the connection between 'rabbit', 'configserver', 'accounts', 'loans' and 'cards' will not work.  So make sure to mention:
 *      networks:
 *       - eazybank
 *  And this same 'eazybank' network we created in the docker compose file towards the end like:
 *  networks:
 *   eazybank:
 *     driver: bridge
 * - Which communicates nothing but, 'We have created a network with the name 'eazybank' which supports the driver 'bridge'  and this same network we are trying to refer in all the services as can be seen in the docker compose file.
 *
 * Optimizing Docker compose file to not have any repetitive content
 * ------------------------------------------------------------------
 * - For this, in the same folder where my docker-compose.yml file is present. i.e., docker-compose/default, I am creating a file 'common-config.yml'. Inside this file, the very first element that we need to mention is 'services' post which I am going to mention a service named 'network-deploy-service:' under which I want to mention the 'networks' related configurations. If you check the docker-compose file under all the services we have the 'networks' configurations. I want to remove this repetitive configurations from the docker-compose file and maintain that in a single place which is the common-config.yml file. Just cut that 'networks' configurations from one of the services defined inside the docker-compose.yml file and paste it inside the common-config.yml file under the service 'network-deploy-service:'
 *   Very similarly, I am going to create one more service with the name 'microservice-base-config:' This I am creating because, I want that whenever I am creating any microservice inside my docker-compose they should extend this service named 'microservice-base-config:' as the name even communicates. To this 'microservice-base-config:' service defined inside the common-config.yml, I am trying to 'extend' the previous service that I have created in this same file i.e., 'network-deploy-service' . Also, inside this service 'microservice-base-config',on top of the extended network related configurations defined under 'network-deploy-service', I am also creating/defining 'environment' variable(s) which is 'SPRING_PROFILES_ACTIVE: "default"' because this environment variable is going to be common for all the microservices like cards, loans, accounts and configserver. Okay, haha, 'of course configserver does not need this,'SPRING_PROFILES_ACTIVE: "default"', environment variable but there is no harm providing this value.
 *   You may have a question like, 'Why can't you mention this environment variable, 'SPRING_PROFILES_ACTIVE: "default"', directly under the service 'network-deploy-service'. The reason I am not mentioning this environment variable, 'SPRING_PROFILES_ACTIVE: "default"', directly under the service 'network-deploy-service' and am trying to place it under the child service, 'microservice-base-config' is because, our 'rabbit' service defined inside the docker-compose.yml file does not require this spring related environment variable(s) configurations. It only requires the 'networks' related configurations and that's why for 'rabbit' service, I am going to import the service ''network-deploy-service' and for the other services/microservices i.e., cards, loans, configserver and accounts, I am going to import the service 'microservice-base-config' which means, I am going to get both the network related configurations and at the same time I am going to get the 'SPRING_PROFILES_ACTIVE: "default"' environment variable.
 *   Apart from this, 'SPRING_PROFILES_ACTIVE: "default"' envrionment variable(s) related configurations, there is one more configurations that is getting repeated inside all our microservices i.e., cards, loans and accounts including the config server. This is the 'deploy' related configurations. Copy that and mention under the 'microservice-base-config' because my 'rabbit' service does not need these configuration. If you analyze more the docker-compose file for any other configuration that is getting repeated for all the microservices i.e., cards, configserver, accounts and loans. Okay, if you observer for loans, cards and accounts microservice the 'depends-on' information/configuration is getting repeated. On top of that, the environment variable like 'SPRING_CONFIG_IMPORT: "configserver:http://cofigserver:8071"' is also getting repeated for these 3 microservices. That's why I am going to create one more service 'microservice-configserver-config' which will act as a child service to 'microservice-base-config' service.
 *   So, any microservice instance inside my application which depends on configserver, I am going to import this service named ' microservice-configserver-config'. This service, ' microservice-configserver-config' is going to extend the previous service that I have created which is 'microservice-base-config' because our individual microservices like accounts loans and cards need the 'networks' related configurations, 'deploy' related configurations, 'SPRING_PROFILES_ACTIVE: default' configurations as well as the 'dependency'/'depends-on' details of the configuration server/configserver. Post that, I am also going to create 'environment' variable(s)  inside whicb I am goint to mention 'SPRING_CONFIG_IMPORT' as well as I am going to move the environment variable 'SPRING_PROFILES_ACTIVE' from the service 'microservice-base-config' into 'microservice-configserver-config' becuase this environment variable is only needed by our 3 microservices.
 * - Now, inside this 'common-config.yml' file we have 3 services defined:
 *  1. 'network-deploy-service' - We will import this inside the 'rabbit' service because we know that It only requires the 'networks' related configuration.
 *  2. 'microservice-base-config' - We will import this inside the 'configserver' service because we know it requires the 'networks' related configurations as well as the 'deploy' related configurations.
 *  3. 'microservice-configserver-config:' - We will import this inside all our 3 microservices i.e., cards, loans and accounts because we know that they require not only the 'networks' and 'deploy' related configurations but also the 'depends-on' and the 2 environment variables i.e., 'SPRING_PROFILES_ACTIVE' and 'SPRING_CONFIG_IMPORT'
 *  Make sure all the service(s) definitions are separated by an empty line both inside the docker-compose.yml and common-config.yml files for readability.
 *  How to import?
 *  1. For 'rabbit' service, everything is unique i.e., image, hostname, ports and healthcheck, the only repetitive configurations is 'networks' and instead of that I can try to mention 'extends' and what is the 'file' name like i.e., 'common-config.yml' which is present inside the same folder structure and post that, what is the 'service' that I want to import which in this case is 'network-deploy-service'
 *  2. For the 'configserver' service, the repetitive information is the networks and deploy related instructions. These configurations we have defined under the service which is 'microservice-base-config' . Whenever we are extending this service, 'microservice-base-config' we will get both the network related and the deploy related configurations.
 *  3. For the 'accounts, cards and loans' services, the repetitive configurations are the 'networks and deploy' related information/configurations and inside the environment related configurations, we have  SPRING_PROFILES_ACTIVE and SPRING_CONFIG_IMPORT as the repetitive information. Delete those and extend 'microservice-configserver-config' service which we have defined inside the common-config.yml file. 'depends-on' related configuration is also repetitive but as the note below says, let it just stay repetitive for the time being.
 *  Note: In recent versions of Docker compose, it is no longer possible to utilize both the 'extends' and 'depends-on' directive within a service. That's why I make sure to mention the depends-on details directly inside the docker-compose file rather than the common-config file
 * - With this, now our docker-compose file looks ver short and at the same time we also separated all the repetitive information into a separate common-config.yml In future, if I want to change the 'networks' name I can do this in a single place. Very similaryly if I want to change the memory consumption I can do so inside a single place. Very similarly we can try to play with any other information inside this single 'common-config.yml' file instead of maintaining instead of repetitively maintaining and trying to modify those under each service defined inside the docker-compose.yml file which can be a tedious task. #Stay smart!!
 * - Perfect!!, next we will try to generate the docker images so that we can test the docker-compose changes.
 * 2. Generate Docker images for my microservices and the config server.
 *  # To be continued in the next commit!
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
