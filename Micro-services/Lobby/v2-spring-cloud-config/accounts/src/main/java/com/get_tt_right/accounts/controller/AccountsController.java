package com.get_tt_right.accounts.controller;

import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.dto.AccountsContactInfoDto;
import com.get_tt_right.accounts.dto.CustomerDto;
import com.get_tt_right.accounts.dto.ErrorResponseDto;
import com.get_tt_right.accounts.dto.ResponseDto;
import com.get_tt_right.accounts.service.IAccountsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeoutException;

/** Updates  as on 15/02/2025
 * Create MySQL DB containers for Microservices
 * ---------------------------------------------
 * - We will be fixing our code to point to a real DB like MySQL. As of now, we are using an internal H2 DB which is not recommended for real projects/production applications. That's why we will focus on how to use MySQL as a backend for our microservices. As of now we have 3 different microservices like accounts, loans and cards.  We will make code changes so that these microservices will utilize MySQL DB.
 *   Before making the MySQL related changes, you can delete all the changes that we have done regarding Spring Cloud Bus and Spring Cloud Config Monitor. The reason I don't want to carry along these changes is I want to get rid of RabbitMQ container as a dependency inside our microservices. There is no harm with you carrying that, since we are trying to learn by building and running these projects inside our local system, running many containers and many dependencies inside your local system may  not be a good idea because we will be onboarding more services and containers in the next sessions and if I try to start all of them with the help of docker compose inside my local system, it will make my system slow.
 *   The worry is about those guys who are using 8GB Ram laptops. There laptops will be even slower compared to 16GB RAM laptops. That's why, since we are already clear about Spring Cloud Bus and Spring Cloud Monitor we don't have to carry the RabbitMQ service to all the future sessions that we are going to be discussing.  To remove the Spring Cloud Bus and Spring Config Monitor related changes:
 *   . Inside the configserver pom.xml - Remove the dependencies spring-cloud-starter-bus-amqp and spring-cloud-config-monitor
 *   . Inside the application.yml file of configserver - Remove the RabbitMQ related connection details. Don't remove the actuator related management properties because if needed we can always refresh our configurations by manually invoking the actuator 'refresh' api against each instance. And on top of that, we anyway still need to have the readiness and liveness related probes configurations.
 *   . Inside Accounts, Cards and Loans ms pom.xml files - Delete the dependency spring-cloud-starter-bus-amqp
 *     In there application.yml files - Delete the connection properties related to the RabbitMQ.
 * - With these changes, we don't need to run RabbitMQ inside our local system because we don't want the automatic refresh of the properties with the help of actuator's 'busrefresh' or with the help of GItHub's WebHook's process that we have discussed earlier.
 * As a next step, if I want to get rid of the H2 DB from all my 3 microservices, 1st I need to have a running MySQL inside my local system. If you are a traditional developer or if you are working in a monolithic application, the obvious approach is, you will install the MySQL server inside your local system by following all the steps and post that you will try to create the DB , once the DB is up and running you will start the server and after starting the server only you can try integrating into the microservice application. But now, we are smart developers/microservices developers who know docker. Like I said, one of the primary advantages of docker is, we don't have to install any component/software inside our system, with the help of docker images we can run any component/dependency/backing service inside our local system as a container and once done using that software component, you can simply stop/delete the container associated to it.
 * - That's why don't use the traditional approach of doing things as it will unnecessarily take a lot of space inside your system. To have a running MySQL DB for my 3 different microservices? Like we discussed, the best practice is to have a separate DB for each of the microservice. For the same, to create a local running MySQL DB inside your system with the help of docker, 1st make sure docker is started in your system as we are going to run a docker command to achieve this. Inside your terminal, run the command which is 'docker  run -p 3306:3306 (for port mapping, we know by default MySQL DB is going to start at the port 3306, the same port I want to expose to the local microservice which is running inside my system). When trying to create a docker container, we know that its going to run inside its own network and in order to get that exposed into our local network inside our system, we need to do this port mapping.
 *   After the port mapping I am going to provide a name to this DB that we are going to create using the --name flag i.e., docker run -p 3306:3306 --name accountsdb. Now, whenever we are trying to use a MySQL docker image to create a MySQL docker container, we need to provide certain properties like what is the root password that you want docker to consider while creating the container, for this we need to provide some environment variable by leveraging the -e flag. docker run -p 3306:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root. By default, the root username will be 'root' and I don't want to change that but since we want to define our own password that's why I am providing this environment variable. After this, I am going to provide one more environment variable i.e., MYSQL_DATABASE. Like we know, whenever we  install a MySQL DB server, you will get an empty server, by default there won't be any DB or schema installed inside the DB server.
 *   That's why using the environment variable MYSQL_DATABASE I want to create my own DB  or schema inside this MySQL Docker container that we are going to create. I mean by the time the docker container of MySQL is ready, inside that, I am going to have a database with the name 'accountsdb'. docker run -p 3306:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accountsdb. After mentioning this environment variable, I am going to mention a flag -d which indicates I want to start this command in detached mode. At last, we need to mention the docker image name of MySQL which is 'mysql'
 *   docker run -p 3306:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accountsdb -d mysql
 * - On running the command, you sould get a container id as an output and so, if you go check your docker desktop, you will be able to see a running container inside your local system with a coonatiner name 'accountsdb'. Now, we can connect to this database and see if it is properly created. To connect to this DB, we need some client i.e, SQLElECTRON which is a super light-weight application. Just download the GUI and from it, you can connect to any Database. Not only the databases present inside your local system, you can connect to any database even that present inside the cloud. By clicking on the ADD button, you can add/create a new Database connection. For this connection I want to give the Name field a value - LocalAccountsDB, you can give anything.  Under the Database Type dropdwon you should be able to see that there are many types of DB's supported by this client application i.e., MySQL, MariaDB, PostgresSQL, RedShift, Microsoft SQL Server, SQLite and Cassandra. For now, I selected MySQL because that's what we want to connect to.
 *   For the  'Server Address' field, mention 'localhost' the reason being, the database container which is running is exposed to my local machine/host at the port 3307. For username give the default 'root' and password, give 'root' as that's what we have mentioned during the container creation. After that, you can click on the 'test' button which will show whether connection is successful or not. Yess! It was successful. Now click on the save after which you can try to connect to your DB and you should be able to see an 'accountsdb' DB created inside the MySQL server docker container. As of now this DB is empty and has no objects/table. We will create them later and so don't worry. Now, we have a MySQL DB ready inside my local system that will be utilized by my accounts ms.  With only a single command this was made possible. It did not take even 10secs whereas if you compare with the traditional approach, it is going to take a lot of time and a lot of space inside your local system. In real projects, we usually don't install MySQL inside our local system
 *   The project infra team, they will deploy into a dev server or QA server and will ask us to use the same inside our local code. But since right now we are trying to learn everything on our own, we are using mysql image to create DB's very easily with the help of docker.
 * - In the same fashion, create DBs for cards microservice as well as loans microservice using the commands below:
 *   docker run -p 3308:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb -d mysql
 *   docker run -p 3309:3306 --name loansdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=loansdb -d mysql
 *  Like this, we will be having 3 database containers up and running and ready for integrating into our microservices. If for example you ran the command: docker run -p 3307:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb -d mysql, you would run into the below error:
 *  docker: Error response from daemon: driver failed programming external connectivity on endpoint cardsdb (fb7d74d495f0df4235f483eb66dcb344ba0f64e1eb30852f8c182
 * 4a788461cf7): Bind for 0.0.0.0:3307 failed: port is already allocated.
 * - This is because, the port 3307 inside my local system is already in use by the accountsdb. That's why I should expose the cardsdb and loansdb inside the local system with different port numbers.
 * - You may have some question that I am using the same 3306 in the port mapping for loans, accounts as well as cards. How? Why is docker not complaining? Well, each container will have its own ecosystem/isolated network and that's why since this loansdb, accountsdb and cardsdb being 3 different containers, they will work perfectly without any conflicts. Whereas, when we try to expose these 3 containers to the outside world, our local system, we can't use the same port for all of them. They have to be unique.
 * - Now, we have 3 different MySQL DBs ready and running inside our local system. Create there connection inside the SQLELECTRON client application and see if everything is working fine just like we did for accountsdb MySQL DB. For the same we have a power button on the top RHS of SQLELECTRON GUI. Click on that to logout and then Click on the Add button to add and test connections for cardsdb and loansdb. Post that, Connect to the respective DB's to confirm that whether the respective DB's are created or not. If you connect to the LocalCardsDB, you should be able to see that we have a cardsdb DB created inside the MySQL server docker container. Similarly, for the LocalLoansDB, you should be able to see that we have a loansdb DB created inside the MySQL server docker container.
 * -  With this, we have 3 different MySQL containers running inside our local system and ready for integrating into our microservices. As a next step we need to make code changes inside our microservices to use these MySQL containers instead of H2 DB. We will follow the below steps for accounts, cards and loans microservice:
 *  . Delete the h2 related dependency from the pom.xml file. Because, whenever Spring boot sees h2 DB inside the pom.xml it is going to do some initializations during the startup, and it will try to use the same. Instead of the h2 dependency we need to use a 'mysql' dependency whose artifact id is 'mysql-connector-j'. Once you made these changes, save and load the maven changes. Our config server is not using any DB and that's why we are not going to make any changes to it.
 *  . In the application.yml file of accounts, cards and loans ms, we need to make some changes to the properties that we have defined inside the application.yml file. The properties that we have defined are:
 *    - spring.datasource.url: jdbc:h2:mem:testdb
 *    - spring.datasource.driverClassName: org.h2.Driver
 *    - spring.datasource.username: sa
 *    - spring.datasource.password:
 *    - h2.console.enabled: true
 *    - spring.jpa.database-platform: org.hibernate.dialect.H2Dialect
 *    - ..etc
 *   . As of now, we have mentioned properties related to the H2 db inside our application.yml files. Now, we need to make changes to the properties related to the MySQL DB as below:
 *    - spring.datasource.url: jdbc:mysql://localhost:3307/accountsdb  => The prefix is 'jdbc:' In the place of 'h2:' I am going to mention 'mysql:' then 2 forward slashes and what is the hostname i.e, 'localhost' since we have the DB running inside our local system and the port number is 3307. And after the prot number we need to mention what the DB schema name or some people refer to it as database name is 'accountsdb'. This is what we want our application to connect to or to use to store the data.
 *    - spring.datasource.driverClassName: com.mysql.cj.jdbc.Driver => This is optional, we don't need to mention this property.
 *    - spring.datasource.username: root
 *    - spring.datasource.password: root
 *    . In real microservices you can use the DB credentials of Development DB or QA DB. No one is going to expose the DB credentials of your production DB by hardcoding them inside the application.yml file. Instead, they are going to use the externalized approaches like Environment variables, CLI arguments, JVM system varaibles or inside the docker-compose file, they have an option to mention this url, username and password. The approach when we use K8S is going to be different. Inside K8S there are concepts like configmaps, secrets, etc. using which we are going to provide the sensitive credentials like DB credentials to the containers during the start-up process.
 *    . For now, since we ar using the local db, hardcoding the username, password along with the url inside the application.yml file should be fine. Later on we will see how to provide these values using environment variables and docker compose file approaches.
 *    - h2.console.enabled: false  -> Remove these kind of H2 related properties from the application.yml file.
 *    - spring.jpa.database-platform: org.hibernate.dialect.MySQL8Dialect -> This is optional, we don't need to mention this property.
 *    - spring.jpa.hibernate.ddl-auto: update -> Remove this as well.
 *    - spring.jpa.show-sql: true -> Keep this so that we can be able to see the executed SQLs inside the console.
 *   . With this we should be good but there is one more property that we need to mention. As of now, if you see the schema.sql you can see we have mentioned the database DDL related information. In the case of h2 DB, during the startup, Spring boot will automatically look for the sql file with the name schema.sql and execute all the DDL commands that are present inside that file. But in the case of MySQL DB or any other real DB. Spring boot will not follow the same behavior and will expect the developer to have the completely prepared DB available by the time Spring boot is trying to start.
 *     At the same time, we don't want these DDL scripts to be executed manually whenever I am trying to create a new DB Container. To overcome this challange, there is a property that we can mention. Under the 'spring' I am going to mention a child element which is 'sql' and under the 'sql' I will mention a child 'init' under which I will mention 'the element 'mode' with a value of 'always'. This will tell Spring boot to execute the DDL script inside the schema.sql file whenever Spring boot is trying to start the application.
 *     Inside the DDL scripts, you can see I am using 'IF NOT EXISTS' and so my Spring boot framework will only create the DB objects/execute the DDL scripts if those DB objects like tables are not present inside the DB. This is a very common pattern that developers follow in real projects. During the very first time while Spring boot is trying to start, it will try to create the DB objects/execute the DDL scripts. But after that if I am using the same DB docker container, Spring boot will not try to create the DB objects/execute the DDL scripts again because I have mentioned this 'IF NOT EXISTS' in the DDL scripts.
 *     If you don't have this, 'IF NOT EXISTS' in the DDL scripts, during the startup of the application it will throw an error that it tried to create the DB objects but the DB objects are already present inside the DB. To avoid such issue, make sure to always have this 'IF NOT EXISTS' in the DDL scripts.
 *   . Make the very similar changes inside the cards and loans microservices. Please make sure the indentation inside your application.yml file is correct always while doing these changes.
 * - With these changes we should be good to go. First, start your config server, then start the accounts, cards and loans microservices because our microservices have a dependency on the config server. That's why we will first try to start the config server and then start the accounts, cards and loans microservices. Now, check how you DB's to respective microservices look like. We expect to see some DB objects to be created during the startup process but to our suprise, there are no database objects created inside the respective databases. What could be the issue?  The sql.init.mode property should be under the spring element and not the 'jpa' element. That's the mistake we made. Fix this amd restart your 3 microservices. Now, check how you DB's to respective microservices look like. We expect to see some DB objects to be created during the startup process and we should be able to see the data inside the DBs.
 * - Do these corrections in all the 3 microservice's application.yml files. Now, if you go the DB and try to reconnect to the respective DBs/schemas you should be able to see the data objects i.e., Tables inside the DBs that have been created during the startup process as a result of executing the DDL scripts present inside the schema.sql file. WIth this, our 3 microservices are correctly connected to the respective DB Containers in our local system. Now lets test some few scenarios inside our postman i.e., Invoke the 'CreateAccount' POST endpoint. You should be able to get a successful response. Using the same phone number I am going to create a new 'Card', So invoke the 'CreateCard' POST endpoint. You should be able to get a successful response. Now, invoke the 'CreateLoan' POST endpoint. You should be able to get a successful response. Finally, validate if the Created data is present inside the respective DB's.
 *   Yes!! All the information that we have submitted is getting saved here, and we should be able to see the data inside the DBs. Under the accounts DB, we have 2 tables. 'customer' where the customer details are stored and 'account' where the account details are stored. Under the cards DB, we have 1 table where the card details are stored. Under the loans DB, we have 1 table where the loan details are stored. This is a confirmation that our 3 microservices are able to talk with the local DB containers and they are able to save the data. Since right now we are using the docker containers, whenever you delete a container all the data that you saved in any of the particular DB container will be lost forever. This works very similar to a local installation, If you install  and store some data in it you should always be able to see it, If you someday delete/uninstall a locally installed DB, then everything you had in it will be lost forever. Very similarly, you need to make sure that these containers you are not deleting them. YOU CAN STOP THEM BUT DON'T DELETE THEM!
 *   If you delete them, you pay the price of loosing all its contained data forever. Simple!! We can do a demo of this. First, Stop all your 3 running microservices then 'STOP' the loansdb, cardsdb and accountsdb containers. So, as of now, all my containers are in 'stopped' status. Now If I try to start my microservices then definitely they will throw an error that they are not able to connect to the respective DB containers. To show you a demo, what I can do here is, I will delete the 'cardsdb' DB container and this means I am loosing this container FOREVER, they will even show you a RED BUTTON FOR THIS ACTION. Don't do such things especially if you didn't create that container or even if you created it and  is in some real-time projects without consulting. Now, the other 2 containers loansdb and accountsdb, I will try to restart them by checking their respective checkboxes in the Docker desktop UI. We don't have a 'cardsdb' and we have to create it again  using the command: docker run -p 3308:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb -d mysql
 *   Now, in the docker desktop, you should be able to see all the DB containers are up and running. The output that we can expect in this proof of concept is, the data present inside the accounts and loans DB containers should be there whereas cardsdb DB container DDL objects and data will not be there. This is how costly it can be! Whatever I am talking about you can visualize in the SQLElectron. You can connect to the respective DB containers and verify this!! In the accountsdb and loansdb containers you should be able to see the DDL objects and data present inside them as you just stopped and restarted the respective containers. You did not hard delete/uninstall them. You just stopped them then restarted them. Even if you restart your laptop or docker server the containers and its respective data will be there and will never be lost. Now, restart the 3 microservices and in the postman run only the fetch API.
 *   Under the accounts we have the 'FetchCardDetails' on invoking it, you should happily get a 200 ok response because the data is present inside the DB.  If you invoke the 'FetchCardDetails' you will get a 404 NOT_FOUND because all the data that we had saved inside this DB is lost because we hard deleted the container. When you invoke the 'FetchLoanDetails' you should happily get a 200 ok response. I hope you are seeing the drawback when using docker containers inside our local system. The advantage is you can always spin up the DB very quickly and on the disadvantage side, we need to make sure that the container(s) are NEVER deleted even by mistake as you will lose all its data forever. In real production, MySQL DBA's will attach a storage or volume where the data can be stored by the MySQL container. That's why in real prod MySQL containers we never lose data even if we delete or replace the MySQL container. Please! be equipped with this information and accordingly use the docker MySQL containers.
 *
 *   Update docker-compose file to create and use MySQL DBs.
 *   --------------------------------------------------------
 *   - As of now, inside our local system, our microservices are able to connect with the MySQL DB containers that we have created. But when you try to run your microservices in a container environment like Docker Compose or K8S whatever properties that we have mentioned inside the application.yml files of the respective microservices are not going to work. Reason: We have hardcoded the host name as 'localhost'. That's why whenever we are suing docker-compose file or whenever we are trying to deploy our microservices in a K8S environment we should provide this datasource url, username and password properties with the help of Environment variables. We are going to update our docker compose files for the 3 profiles and using those docker compose files we are going to start all our DB containers along with all our microservices and we will establish a communication link between them so that they can communicate wih each other.
 *     For this first, stop all your locally running microservices instances. Along with that, stop all the running containers and delete them because we need to create these containers with the help of docker-compose file but not by runing the docker command manually. As a next step b4 I try to change the docker compose file, 1st we need to regenerate the docker images based upon the latest changes that we have done inside our microservices. As of now, we have removed the h2 related dependencies, and we have added the MySQL dependencies and properties (This is what is referred to as DB migration) and that's why we need to regenerate the docker images based on these migration changes that we have done. Inside the pom.xml files of accounts, loans, cards change the tag name of the to be generated docker image from V2 to V3. We don't have to regenerate the docker image for configserver because we have not done any changes in it. As a next step, go to the terminal in the location of each of your pom.xml file for the respective accounts, cards and loans microservices and run the Maven command mvn compile jib:build
 *     This will generate docker images and push them into your hub repo. Make sure to also make it a habit to always be deleting unused images and containers inside your local system to get some space/storage and memory inside your local system. Make sure to delete all V2 images as we won't use them anymore. With this, your system should work fine even if you try to start multiple containers with the docker compose command. Now, lets update the docker-compose file present inside the default folder. If you had reverted/removed all the Spring Cloud Bus related changes, first you have to remove the 'rabbit' service from your docker-compose file as its not being used anymore. Otherwise, don't remove it. For myself I didn't revert/remove Spring Cloud Bus related changes and so I am not going to remove the 'rabbit' service from my docker-compose file. Next, we are going to create 3 more services,for the accountsdb, cardsdb and loansdb containers. Lets do it for accountsdb:
 *     Service name - accountsdb. For this service I want to use the image which is 'mysql' The 'container_name' we can use as 'accountsdb ' After the container_name, we need to mention the ports mapping and here for the accountsdb docker container, we need to mention -3307:3306. After this, we need to mention the healthcheck details on how to test the health status of this accountsdb docker container. Once this accountsdb is completely started then only I should try to start my accounts service. Since we have that kind of dependency, we need to define healthcheck details so that my docker compose can try to start my accounts microservice only after accountsdb docker conatiner becomes healthy. The 'test' command that we need to mention here is, '["CMD", "mysqladmin", "ping", "-h", "localhost"] ' With this command, I am trying to tell to my docker that please run this 'ping' command  where it is trying to ping the 'host' with the value 'localhost'. Whenever it gets a successful response will imply that the health of this container is good.
 *     You can always get all these healthcheck commands from the official documentations. You can always search inside the web like, 'What is the healthcheck command for MySQL docker container' Similarly, when you are using some other docker conatiner like 'Oracle' or 'Redis' ...etc.  You can always do a search for these healthcheck related commands inside their official documentations. After 'test' I am mentioning the 'timeout' as 10s - Implying it has to wait only for 10s and within that if it is not getting the response for the ping it will consider it as a failure. I am also mentioning 'retries' as 10 times. So that means my docker will try to ping the 'localhost' for 10 times and if it is not getting any response for 10 times then it will consider it as a failure. Apart from this timeout and retries, we can also mention we can also mention 'interval' with a value 10s. Similarly, we can mention 'start_period' as 10s. With this, we have defined the healthcheck details for the accountsdb docker container. After the healthcheck details, we need to provide the environment variables for my accountsdb docker conatiner.
 *     For this, we are providing the 'MYSQL_ROOT_PASSWORD=root', and 'MYSQL_DATABASE=accountsdb' environment variables. While we were creating this accountsdb container using the docker run command, we passed a similar set of environment variables and so the same is what I am providing here. We are creating a container right? As a next step,We need to make sure that this container is tagged to the same network where other containers are trying to start then only we can be assured that they can communicate with each other with the help of service names. Otherwise, the comminucation between our microservices and the DB containers will not happen. For the same we can extend 'network-deploy-service'  present inside common-config.yml. With this, now my accountsdb service is ready. Very similarly, create the accountsdb service and cardsdb service. Everything will be same as discussed above, the only changes are you edit the service name, container name,the ports mapping and the environment variables values. With this we should be good in terms setting up the DB container services. Next, we need to establish a link between these DB containers and the microservices. I.e., accountsdb service should be linked to accounts service, ..etc.
 *     For the same if you go to the common-config.yml here, already all the microservices like, accounts, loans and cards they are already using/extending this 'microservice-configserver-config' That's why inside this 'microservice-configserver-config' I am going to create new environment variables like, SPRING_DATASOURCE_USERNAME: 'root' and SPRING_DATASOURCE_PASSWORD: 'root' These credentials as of now we set as the same values for all the 3 microservices, that's why we don't have to repeat these environment variables again and again for each of the 3 microservices accounts, cards and loans inside the docker-compose.yml file. The other important environment variable that we need to mention inside our microservices is the SPRING_DATASOURCE_URL details. Since this will differ for different microservices, we have to create a separate environment variable to capture this inside each of the services like accounts, cards and loans. To this, SPRING_DATASOURCE_URL, environment variable we need to pass the complete endpoint details of the respective DB that your service will connect to. This value you can get inside your application.yml files of the respective microservices as we mentioned there while doing local testing.
 *     For accountsdb we have the url information as 'jdbc:mysql://localhost:3307/accountsdb' But here the communication will not happen with the help of localhost instead the communication between the containers will happen with the help of service name that we have created. So this value will instead be 'jdbc:mysql://accountsdb:3306/accountsdb' and the port should be 3306 and not 3307 as 3307 is the port exposed to the outside world and 3306 is the port of the isolated container and that's what we need in this case. Do the same for the loans as well as cards services. With this, now the link establishment between the microservice containers and the DB containers is completed. The next configuration that we need to do here is we need to to communicate to the docker compose that my individual microservices are dependent on the accountsdb or cardsdb or loansdb respectively. For the same, just after our environment variables definition for the respective microservices services, we can create one more element which is 'depends-on' >> 'servicename' i.e., 'accountsdb' >> 'condition' with a value 'service_healthy'. This means that the docker compose has to make sure that the accountsdb in this case is started and is healthy before it tries to start the accounts' microservice. Do the same for loans as well as cards services.
 *  - With this, all our docker compose file changes are completed, but if you clearly see the docker-compose file for the DB containers, we have a lot of repetitive stuff like image name, healtcheck details, MySQL root password. So, this information we can optimize by moving it inside the common-config.yml file. For this, I will create a new service,microservice-db-config, that extends 'network-deploy-service' and under this service, I am going to mention all the repetitive information i.e., 'image' - Will be the same for all my MySQL DB containers, port mapping is unique, container name is unique, healthcheck details are repetitive so move that. The 'MYSQL_ROOT_PASSWORD' environment variable is repetitive so move that as well. Now, extend this 'microservice-db-config' service inside our docker-compose.yml file under accountsd, loansdb and cardsdb. Before that, make sure to remove the duplicate information that we have moved into the 'microservice-db-config' service from all the accountsdb, cardsdb as well as loansdb. Now, instead of extending the 'network-deploy-service' extend the 'microservice-db-config' With these changes, we should be good to go. These very similar changes you have to make inside the qa and prod docker-compose files.
 *    Before, forgetting make sure to change the image name tag for all the 3 microservices i.e., accounts, cards and loans to V3 as this are what have the MySQL related changes. V2 have the h2 DB related configurations. Inside your terminal navigate to the docker compose folder under the default folder and run the command docker compose up. Since I started this command without -d I am able to see all the logs in the current terminal itself and that's why it is very important to run in a detached mode so that your terminal will get free and you can run other commands.
 *  - In the docker compose file, for the DB docker container services, the port mapping configurations are not necessarily needed when my containers are trying to communicate with each other. I mean no need to expose my databases to the outside world. If you remove the port mapping configurations from the docker compose file for the MySQL DB containers , your microservices and DB containers/services are going to communicate with each other without any issues. The main reason I am maintaining these port mapping configurations to expose the db services to the outside world is, we will need to validate whether the data is saved or not by using a client application like SQLELECTRON. Since I will be using SQLELECTRON which will outside the docker network, I need to make sure I am exposing these DB containers to the outside world by using the port mapping configurations inside the docker-compose file. Hope you are now getting clarity on the conecpts of Docker network.
 *    If you started your docker compose up in detached mode and press control + C this will stop all your running containers. It won't delete them. To delete all the running conatiners, I need to run the command, docker compose down. DON'T BE USING this command in real time. Else be ready to pay the price of loosing all your data present inside the containers you are hard deleting/uninstalling.
 *  - Also create the docker compose files for qa and prod environments which is super easy as we already know. Just copy what we have currently inside the default package and paste that inside the qa and prod folders. The only change we have to make is the environment variable like SPRING_PROFILES_ACTIVE: default that is present inside the common-config.yml file. With these, we have also updated the qa and prod docker compose files as well. Now if you go to the docker desktop, all of my 8 containers started successfully. You can confirm this by clicking on for example cards-ms . You should see logs like: "Added Connection ..." which means the connection with the DB is successful!! and finally you should see another log like " Started CardsApplication in ..." The same kind of logs you will see inside your loans as well as accounts microservices indicating that the DB connection was successful and the applications started successfully!! Now lets quickly test our REST APIs i.e., Create Account, Create Card, Create Loan. In these 3 scenarios we should happily get a successful response.  Finally, you can go to the SQLELECTRON to verify if the data is saved or not. Yeey! everything is working as expected!! This way we established communication between microservices and MySQL DBs even with the docker-compose as well.
 *  You may have a question like, in my day-to-day work, we are not using Docker DB containers, we are using some Dev database, or we are using some DB inside the cloud. So, for whatever reason, you are using some database which is not inside your local system. In such scenarios, in our docker compose file, the change is very simple. You don't have to mention all these accountsdb, loansdb, cardsdb docker service containers. You can directly go to your individual microservices services and mention your exact datasource url under the envrionment variable like: SPRING_DATASOURCE_URL. You can give the 'hostname' of your development environment or the public IP which you got from the Cloud environment. Provide those details directly as value of the SPRING_DATASOURCE_URL environment variable and with that, your microservice will be able to connect with an external DB. i.e., SPRING_DATASOURCE_URL: "jdbc:mysql://IPtoexternaldb:port/databasename"
 *
 *  Demo of Docker Network concept
 *  -------------------------------
 *  - As of now we made sure that all our containers/services are getting started inside the same network. If you can clearly see all our services extend the 'network-deploy-service' and the network that we attached to all our containers is 'eazybank'. Now we will see a demo where we will try to detach the DB containers/services from this network and with that we will verify that the communication will fail. Post that, I will also show that the communication between your microservices and databases will fail. We will do this demo with the qa profile docker compose file so that we will at the same time validate that the qa profile is working fine as well! For default profile everything is good!! If you open the common-config file you can see that a of now, our 'microservice-db-config' service extends a service with the name 'network-deploy-service' which means all the conatiners which are using/extending the service 'microservice-db-config' inside the docker-compose file will all get started inside the network like 'eazybank'. In the common-config file if I try to remove the extends 'network-deploy-service' from 'microservice-db-config' will imply that my DB conatiners will not start in the network of 'eazybank' and with that the communication between my microservices and the DB services will fail ceremoniously!!
 *  - Now, stop/hard delete/unistall all the running containers using the 'docker compose down command' With this, all the containers that we have started with the help of default profile will get uninstalled/permanently deleted with all there contained data. Now, navigate to the qa folder and run the docker compose up -d command. We can expect that accounts, cards and loans microservices will not start successfully because they will not be able to communicate with the DB containers. Now, you can validate how the docker containers are starting inside the docker desktop. Under the qa, you should see that, my loansdb, accountsdb as well as cardsdb all started successfully and are even 'healthy' as can be seen from the terminal where you executed the 'docker compose up -d' command. Config server microservice also started successfully and is also 'healthy' but coming to other containers like accounts, cards and loans microservices, initially they are in the 'running' status as can be briefly seen from the docker desktop but after some time they turned into 'exited' status. Why? If you open any one of them, you will see log messages like, 'Communication link failure' , 'Name or service not known'. This is a clear indication that the communication is failing with the DB, they are not able to create a connection! Hope you are now getting much more clarity on how docker networks work!
 *  - To get a list of docker networks created behind the scenes, run the command ' docker network ls '. This command will list you all the docker networks that have been created. You should see networks like, 'bridge', 'host','none', 'qa_default', 'qa_eazybank'. You can see we have a network created with the name 'eazybank' for the qa profile. At the same time we have other networks that got created. To understand to which network our container is attached. You can first run the docker ps -a command so that you are able to see the container Id's of all the running and stopped containers.  Now if you run the command ' docker inspect <contaiiner_id> '. I passed the container_id for accountsdb. Here you should be able to see the 'NetworkID' key with some value. In my case I saw a value starting with the string, 'f0ca...'. If you try to understand what network name that has this network id by invoking the docker network ls command, you will see that it is 'qa_default'. What does this tell us? Inside a docker compose network, whenever you are not mentioning any network/attaching any network to a service definition, they will all get assigned to a 'default' network. For my accounts microservice and all the rest other 4 services like : rabbit-1, configserver-ms, loans-ms and cards-ms since we have created and attached to them the 'eazybank' network which is completely different from the 'default' network, the communication between the containers in the different networks is not going to work.
 *    This is the power of docker! As you will have complete isolation between separate networks until unless you allow the network communication, the communication will never happen!! as we've seen in our simple demo above. Now, run the 'docker compose down command' to uninstall all the conatiners that we tried to start for the qa profile. In you common-config file revert back the extend changes so that 'microservice-db-config' service is able to extend 'network-deploy-service'. This time if you now run the 'docker compose up -d ' command, all your 8 containers should now start successfully and be able to connect/communicate with each other as they are in the same network, 'eazybank'. This you can validate! With this discussion, you should have the base clarity on how you go about connecting your microservices with  docker running DB containers. It could be using docker compose as we have just discussed or it can be using your local host configurations as we earlier on saw. Both approaches we have discussed.
 *  - From the next sessions, we will not be using MySQL DB again, we will revert back to using the Internal h2 DB only for our development purposes' You maybe wondering wby we are doing this as we already migrated our code from h2 DB to MySQL. The big reason is because, inside our docker compose file, whenever we want to start our microservices along with the relevant dependencies, there will be 3 DB containers, there will be config server, rabbit, loans, cards as well as accounts services, a total of 8 services/running containers inside your local system, and we will be onboarding more composents as we proceed like; Eureka Server, Gateway Server, Grafana related containers, ...etc. With so many conatiners running inside your system, it is going to slow down your laptop and eventually ypou will get demotivated when you see some runtime issues which you are not able to debug! But if you are confident that your laptop is a higher laptop and is going to work without any issues than keep using as many containers as you can!
 *
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
    private Logger logger = LoggerFactory.getLogger(AccountsController.class);

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
    @Retry(name = "getBuildInfo",fallbackMethod = "getBuildInfoFallback") // Retry instance configurations
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() throws TimeoutException {
        logger.debug("getBuildInfo() method invoked");
//        throw new NullPointerException();
//        throw  new TimeoutException();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    // Fallback method
    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
        logger.debug("getBuildInfoFallback() method invoked");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("0.9");
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
    @RateLimiter(name = "getJavaVersion",fallbackMethod = "getJavaVersionFallback")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    public ResponseEntity<String> getJavaVersionFallback(Throwable throwable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Java 17");
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
