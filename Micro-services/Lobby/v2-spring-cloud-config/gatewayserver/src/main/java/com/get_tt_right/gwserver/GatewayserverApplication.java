package com.get_tt_right.gwserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

/** Update as of 26/05/2025
 * Implementation of log aggregation With the help of Grafana, Loki and Promtail
 * ------------------------------------------------------------------------------
 * 1. Inside the pom.xml files of all our services, change the docker image tag name to V7.
 * 2. Inside the .yml file of gateway server:
 *   As can be seen we have the configuration httpclient.response-timeout set to 2 seconds. Which means my gateway server is going to wait a maximum of 2 seconds to get a response from the actual ms. But since we are going to set up more and more containers inside our local system , definitely inside my local system I will not be able to get the response within 2 seconds due to low availability of memory. So, to avoid any inconveniences inside the local testing, increase this timeout to 10 seconds. With this my gatewayserver is going to wait a maximum of 10 seconds to get a response from the actual ms. And with this we should be able to get any kind of response from the individual ms's and will not face any kind of issues inside the local system regardless of how many containers or supporting components you are trying to set up.
 *    . Hope you have clarity about this change and in future if you are facing any timeout issues, feel free to increase this timeout based upon your local testing requirements.
 * 3. Now, let's go to the official doc of grafana - https://grafana.com/docs/loki/latest/get-started/quick-start/quick-start/. If you scroll a bit down, past the architectural diagram proposed by Grafana team, they are going to tell you what are the prerequisite which is, you should have a running docker + docker compose inside your local system. These we already set up long back. Post that, you will see a label , 'To install Loki locally, follow these steps:" where at first they are telling you to Create a directory called evaluate-loki, mkdir evaluate-loki,for the demo environment and make it your current working directory, cd evaluate-loki. But we don't have to follow this because we will try to implement all these changes inside our docker-compose folder present inside our workspace location.
 *    After this, they are asking you to download 3 different .yml files i.e., loki-config.yaml, alloy-local-config.yaml, and docker-compose.yaml. They have given url's for each of these files i.e.,
 *      wget https://raw.githubusercontent.com/grafana/loki/main/examples/getting-started/loki-config.yaml -O loki-config.yaml
 *      wget https://raw.githubusercontent.com/grafana/loki/main/examples/getting-started/alloy-local-config.yaml -O alloy-local-config.yaml
 *      wget https://raw.githubusercontent.com/grafana/loki/main/examples/getting-started/docker-compose.yaml -O docker-compose.yaml
 *    Like this they are trying to extract the 3 files using the same respective names.
 * Now, lets try to understand what is present inside these 3 files.
 *  docker-compose.yaml
 *  ----------------------
 *  - Because we are already familiar with the docker-compose syntax, understanding the below should not be challenging for you.
 *  ---
 * networks:
 *   loki:
 *
 * # Above they are creating a network with the name Loki. So that, they are going to start all the services under the same network.
 * # Inside our docker-compose, we also create a docker-compose file with where we defined a network with the name 'eazybank'. Very similarly they are also following the same strategy.
 * # Under the services, first they are trying to define the 'read' service/component of Loki. That's why you can see the Image name is related to Loki.
 *
 *
 * services:
 *   read:
 *     image: grafana/loki:latest
 *     command: "-config.file=/etc/loki/config.yaml -target=read"    # They are providing a command which will be executed when we try to convert this docker image as a container. If you see, -target=read, they are trying to start this image with the target as read. With that, it is going to become a read component of Loki. And that is going to start at the ports defined below.
 *     ports:
 *       - 3101:3100   # They are exposing the read component to the outside world at the port 3101.
 *       - 7946
 *       - 9095
 *     volumes:    # After the Lists of ports above, you can see here we have volumes defined. We've not used this kind of definition so far. So, what are Volumes? Using Volumes we can try to map a file which is present inside the localhost system into the docker container.
 *       - ./loki-config.yaml:/etc/loki/config.yaml   # If you see here, there is a Volume detail that they have mentioned. This way, we can create any number of volumes. The configuration here is very simple, they are saying, 'The place/root location where we have this docker-compose file, inside the same folder there  will be a .yml file with the name loki-config.yaml'. If you check the other Link i.e., https://raw.githubusercontent.com/grafana/loki/main/examples/getting-started/loki-config.yaml, you can see, this is the same file. So, they are asking to download this .yml file and paste it into the same folder location where you have this docker-compose file. This same file we are trying to map or copy to the, :/etc/loki/config.yaml, docker container under the location like /etc/loki/config.yaml. The same is being referred inside the 'command' configuration that we have just seen above.
 *                                                    # You can see that for the  -config.file variable they are passing/assigning the same config.yml file
 *     depends_on:   # Post that, they are also saying, 'depends on minio, which is another service defined inside this docker-compose.yml file.' This we will also explore at the end of this docker-compose.yml file.
 *       - minio
 *     healthcheck:  # Healthcheck you are already aware. Using this health-check, the docker-compose is going to evaluate whether the read component or service of Loki is successfully started or not.
 *       test: [ "CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:3100/ready || exit 1" ]
 *       interval: 10s
 *       timeout: 5s
 *       retries: 5
 *     networks: &loki-dns  # At last you can see that this read service we are trying to tag it to the network which is 'loki'. And we are also trying to create an alias name for this network with the name 'loki'. Similar to the defined network name.
 *       loki:
 *         aliases:
 *           - loki   # If needed you can also give different alias name also. They are trying to create an alias name so that they can use it in some other configurations.
 *                    # And btw, what is '&loki-dns' haha. Inside this .yml file whenever we see '&' that indicates an 'anchor', what is an anchor inside yml? Means we are trying to create a variable name ' loki-dns' and to this 'loki-dns' name we are trying to assign the child element present under the 'networks' tag. i.e., loki.aliases="loki". So, we just created a variable with the help of the anchor symbol inside yml.
 *
 *   write:      # This is another service with the name write.
 *     image: grafana/loki:latest   # Using the same image as discussed above.
 *     command: "-config.file=/etc/loki/config.yaml -target=write"  # But this time the command target is going to be write
 *     ports:
 *       - 3102:3100  # And it is going to expose the traffic at the port 3102
 *       - 7946
 *       - 9095
 *     volumes:  # This we have already discussed what it is.
 *       - ./loki-config.yaml:/etc/loki/config.yaml
 *     healthcheck: # This we are also already aware.
 *       test: [ "CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:3100/ready || exit 1" ]
 *       interval: 10s
 *       timeout: 5s
 *       retries: 5
 *     depends_on: # And depends on the minio service
 *       - minio
 *     networks:
 *       <<: *loki-dns     # Now, under the networks definition of this 'write' service, we are using here a special symbol i.e., less than symbol repeating two times.  This double less than symbol indicates merge operation inside the yml. Post this merge operation, we are using asterisk symbol which is a special symbol inside yml  which we can use to refer to any of the anchor variables that we have created/defined anywhere inside a yml configuration file. Previously we created an anchor variable with the name, 'loki-dns' and we are trying to use it here. So, what is the meaning of '<<: *loki-dns'? It means, 'Merge the contents of the loki-dns anchor variable into this write service under the 'networks' tag.'
 *
 *   alloy:  # After the write service, we also have the alloy service.
 *     image: grafana/alloy:latest  # It is going to use the alloy image present inside the grafana organization.
 *     volumes:  # Here they have defined two volumes related configurations
 *       - ./alloy-local-config.yaml:/etc/alloy/config.alloy:ro   # The file 'alloy-local-config.yaml' is going to be copied to the docker container, and it is going to be read-only. This you can be able to see in the link - https://raw.githubusercontent.com/grafana/loki/main/examples/getting-started/alloy-local-config.yaml. This file they are asking to download and paste into the same folder location where you have this docker-compose file. And copy the same using volumes to the docker container under the location like /etc/alloy/config.alloy. Here 'ro' indicates read-only volume. So, my docker container, they can only read the yml configuration present inside the file. but they cannot change it. That is what we are trying to achieve with this 'ro' command.
 *       - /var/run/docker.sock:/var/run/docker.sock              # Post that, there is also one more volume which is internal to the docker containers. What is it?  Find out.
 *     command:  run --server.http.listen-addr=0.0.0.0:12345 --storage.path=/var/lib/alloy/data /etc/alloy/config.alloy   # After that, you can see that, there is a command which they are trying to execute. And they are trying to use the same config.alloy that was copied to the container from the local file system i.e., ./alloy-local-config.yaml. Inside the local system the file name is alloy-local-config.yml but when they are trying to refer to it inside the container using the volumes it is going to be renamed to config.alloy. We are trying to refer to the same inside the command as well. Try finding out what this command is doing.
 *     ports:
 *       - 12345:12345
 *     depends_on:  # We also have the depends_on configuration here.
 *       - gateway
 *     networks: #And networks is Loki.
 *       - loki
 *
 *   minio:  # Here we also have minio related service definition.
 *     image: minio/minio  # With the image name minio/minio
 *     entrypoint:  # And entrypoint command. During the entrypoint command, we are going to execute some shell commands where we are going to create some directories inside the localhost where we have this docker-compose.yml file. It is trying to create 2 directories, one is '/data/loki-data' and another is '/data/loki-ruler'. With this, Loki is going to store all the logs inside our local system only with the help of minio.
 *                  # If needed, inside production we can try to configure other storage systems like AWS S3, Azure Blob Storage, Google Cloud Storage, etc. Or any other cloud storage.
 *       - sh
 *       - -euc
 *       - |
 *         mkdir -p /data/loki-data && \
 *         mkdir -p /data/loki-ruler && \
 *         minio server /data
 *     environment:   # And some environment variables
 *       - MINIO_ROOT_USER=loki
 *       - MINIO_ROOT_PASSWORD=supersecret
 *       - MINIO_PROMETHEUS_AUTH_TYPE=public
 *       - MINIO_UPDATE=off
 *     ports:  # Port mapping
 *       - 9000
 *     volumes: # And volumes
 *       - ./.data/minio:/data   # Under these volumes you can see they are trying to copy everything present under the .data/minio folder inside the local system, that was created when we tried to create this minio service. And copy to the docker container under the location like /data.
 *                               # And btw inside this minio folder only all the logs will get stored. So, with this volume, we are trying to mount this folder ./.data/minio from our local system to the docker container under the location like /data.
 *     healthcheck: # And there is a healthcheck definition
 *       test: [ "CMD", "curl", "-f", "http://localhost:9000/minio/health/live" ]
 *       interval: 15s
 *       timeout: 20s
 *       retries: 5
 *     networks: # And networks
 *       - loki
 *
 *   grafana:  # Grafana service definition
 *     image: grafana/grafana:latest  # Image details
 *     environment:  # Environment variables details
 *       - GF_PATHS_PROVISIONING=/etc/grafana/provisioning
 *       - GF_AUTH_ANONYMOUS_ENABLED=true
 *       - GF_AUTH_ANONYMOUS_ORG_ROLE=Admin
 *     depends_on:  # Depends on gateway
 *       - gateway
 *     entrypoint: # And there is an entrypoint command where they are trying to execute some shell commands.
 *       - sh
 *       - -euc
 *       - |
 *         mkdir -p /etc/grafana/provisioning/datasources      # Creating a folder with the name 'datasources' under the location '/etc/grafana/provisioning'
 *         cat <<EOF > /etc/grafana/provisioning/datasources/ds.yaml  # And under this 'datasources' folder, they are trying to create a file called 'ds.yaml'
 *         apiVersion: 1
 *         datasources:  # Here you can also see they are trying to define a datasource of type 'loki'. Previously we learnt that from grafana we can try to search the logs present inside the 'loki'. To establish the link between grafana and loki, we need to provide the below defined datasource details to the 'grafana' like: Where: Loki is deployed, What is the url where my grafana can try to reach out to the Loki
 *           - name: Loki
 *             type: loki
 *             access: proxy
 *             url: http://gateway:3100
 *             jsonData:
 *               httpHeaderName1: "X-Scope-OrgID"   # And there are some header related information.
 *             secureJsonData:
 *               httpHeaderValue1: "tenant1"
 *         EOF
 *         /run.sh
 *     ports:    # Post that there is some port mapping where grafana is going to start.
 *       - "3000:3000"
 *     healthcheck:  # And there is some healthcheck definition
 *       test: [ "CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:3000/api/health || exit 1" ]
 *       interval: 10s
 *       timeout: 5s
 *       retries: 5
 *     networks: # And networks
 *       - loki
 *
 *   backend:
 *     image: grafana/loki:latest
 *     volumes:
 *       - ./loki-config.yaml:/etc/loki/config.yaml
 *     ports:
 *       - "3100"
 *       - "7946"
 *     command: "-config.file=/etc/loki/config.yaml -target=backend -legacy-read-mode=false"
 *     depends_on:
 *       - gateway
 *     networks:
 *       - loki
 *
 *
 *   gateway:   # Gateway service definition
 *     image: nginx:latest   # It is going to use the image of nginx server.
 *     depends_on:  # And it is going to depend on two services like read and write. These are the Loki read and Loki write services.
 *       - read
 *       - write
 *     entrypoint:  # And there is some entrypoint command where they are trying to execute some shell commands + define some nginx configurations
 *       - sh
 *       - -euc
 *       - |
 *         cat <<EOF > /etc/nginx/nginx.conf   # And they are trying to create a file called 'nginx.conf' under the '/etc/nginx' folder
 *         user  nginx;
 *         worker_processes  5;  ## Default: 1
 *
 *         events {
 *           worker_connections   1000;
 *         }
 *         # You can see below they are trying to define some routing related configurations.
 *
 *         http {
 *           resolver 127.0.0.11;
 *
 *           server {
 *             listen             3100;
 *
 *             location = / {
 *               return 200 'OK';
 *               auth_basic off;
 *             }
 *
 *             location = /api/prom/push {    # Like whenever someone is trying to invoke this path then it is going to pass the request to the Loki write service/component at the port 3100.
 *               proxy_pass       http://write:3100\$$request_uri;
 *             }
 *
 *             location = /api/prom/tail {    # Very similarly for the path '/api/prom/tail', they are going to re-direct the request to the Loki read service/component at the port 3100. So, all such routing configurations are defined inside these startup scripts.
 *               proxy_pass       http://read:3100\$$request_uri;
 *               proxy_set_header Upgrade \$$http_upgrade;
 *               proxy_set_header Connection "upgrade";
 *             }
 *
 *             location ~ /api/prom/.* {
 *               proxy_pass       http://read:3100\$$request_uri;
 *             }
 *
 *             location = /loki/api/v1/push {
 *               proxy_pass       http://write:3100\$$request_uri;
 *             }
 *
 *             location = /loki/api/v1/tail {
 *               proxy_pass       http://read:3100\$$request_uri;
 *               proxy_set_header Upgrade \$$http_upgrade;
 *               proxy_set_header Connection "upgrade";
 *             }
 *
 *             location ~ /loki/api/.* {
 *               proxy_pass       http://read:3100\$$request_uri;
 *             }
 *           }
 *         }
 *         EOF
 *         /docker-entrypoint.sh nginx -g "daemon off;"
 *     ports:    # At last you can see we have some port mapping related configurations.
 *       - "3100:3100"
 *     healthcheck:  # And health check information
 *       test: ["CMD", "service", "nginx", "status"]
 *       interval: 10s
 *       timeout: 5s
 *       retries: 5
 *     networks:  # And networks definition information
 *       - loki
 *
 * # After the gateway, we have an app with the name 'flog' just like we saw previously inside our architecture image proposed by grafana discussions. This app is going to be continuously generating/emitting some random logs.
 * # We don't need this 'flog' service because inside our scenario we are going to use our own ms's to generate these logs. That's why whenever we try to use this docker-compose file we don't need to copy this 'flog' service.  Because we don't need that sample application inside our scenario.
 *
 *   flog:
 *     image: mingrammer/flog
 *     command: -f json -d 200ms -l
 *     networks:
 *       - loki
 *
 *alloy-local-config.yaml - yml file related to alloy
 * -------------------------
 * Here they have defined the configurations on how alloy should work.
 *
 * discovery.docker "flog_scrape" {
 * 	host             = "unix:///var/run/docker.sock"
 * 	refresh_interval = "5s"
 * }
 *
 * discovery.relabel "flog_scrape" {
 * 	targets = []
 *
 * 	rule {
 * 		source_labels = ["__meta_docker_container_name"]
 * 		regex         = "/(.*)"
 * 		target_label  = "container"
 *        }
 * }
 *
 * loki.source.docker "flog_scrape" {
 * 	host             = "unix:///var/run/docker.sock"
 * 	targets          = discovery.docker.flog_scrape.targets
 * 	forward_to       = [loki.write.default.receiver]
 * 	relabel_rules    = discovery.relabel.flog_scrape.rules
 * 	refresh_interval = "5s"
 * }
 *
 * loki.write "default" {
 * 	endpoint {
 * 		url       = "http://gateway:3100/loki/api/v1/push"
 * 		tenant_id = "tenant1"
 *    }
 * 	external_labels = {}
 * }
 *
 * My instructor was explaining the below promtail-local-config.yaml configurations.
 *
 * promtail-local-config.yaml - yml file related to promtail - They have defined configurations on how promtail should work.
 * ---------------------------------------------------------
 * ---
 * server:   # If Http protocol, it is going to listen at the port 9080 and if grpc protocol, it is going to listen at the port 0.
 *   http_listen_port: 9080
 *   grpc_listen_port: 0
 *
 * positions:
 *   filename: /tmp/positions.yaml
 *
 * clients:   # If you see under the clients, it is going to connect with the gateway at the port 3100. And using the Url http://gateway:3100/loki/api/v1/push, it is going to push  all the logs that it is going to read from the other distributed applications / ms's.
 *   - url: http://gateway:3100/loki/api/v1/push
 *     tenant_id: tenant1
 *
 * scrape_configs:  # Here we have some scrape related configurations
 *   - job_name: flog_scrape   # They have given a job name which is flog_scrape. Since they are using an App name with the name 'flog' thay have defined the job name with the prefix 'flog'. We don't need to change any of this, the job name could be anything - any random value.
 *     docker_sd_configs:   # Under these scrape_configs, they have defined some configurations where promtail is going to read the log contents from my docker containers with the help of the socket path '/var/run/docker.sock', present inside all my containers. And the refresh interval is going to be 5 seconds.
 *       - host: unix:///var/run/docker.sock
 *         refresh_interval: 5s
 *     relabel_configs:  # And whatever data my promtail is going to read, I have defined with the help of regex - which means I want my promtail to read all kind of logs data.
 *       - source_labels: ['__meta_docker_container_name']
 *         regex: '/(.*)'
 *         target_label: 'container'  # And the target label is going to be container. Meaning all these logs will be assigned to a label with the target label as 'container'.
 *
 *You can try to connect the dots of what is present inside alloy-local-config.yaml(Latest) and promtail-local-config.yaml(Older). As my instructor was only explaining promtail-local-config.yaml file.
 * He said, you don't need to understand everything about this because most of this work is related to the DevOps/platforms team. But it is a good thing for you to know all these basics, so that you should be able to implement all of them on your own or you can try to help your DevOPs or platforms team in setting up all these inside your project.
 *
 * loki-config.yaml
 * -----------------
 * ---
 * server:
 *   http_listen_address: 0.0.0.0
 *   http_listen_port: 3100   # Here it is going to listen at the port 3100.
 *
 * memberlist:  # Here we have some configurations with are respective to Loki.
 *   join_members: ["read", "write", "backend"]
 *   dead_node_reclaim_time: 30s
 *   gossip_to_dead_nodes_time: 15s
 *   left_ingesters_timeout: 30s
 *   bind_addr: ['0.0.0.0']
 *   bind_port: 7946
 *   gossip_interval: 2s
 *
 * schema_config:   # The very important thing that I want you to understand here is, they are trying to store all the logs using the service minio which is exposed at the port 9000. And they have created some bucket names which is 'loki-data'
 *   configs:
 *     - from: 2023-01-01
 *       store: tsdb
 *       object_store: s3
 *       schema: v13
 *       index:
 *         prefix: index_
 *         period: 24h
 * common:
 *   path_prefix: /loki
 *   replication_factor: 1
 *   compactor_address: http://backend:3100
 *   storage:   # In a nutshell, they have defined some storage related configurations. So, inside our local system, it is going to leverage minio and our local folder system to store all the logs. But in your real projects, If you want to use some real cloud storage, you can come here inside loki-config.yaml and change the details. There will be some good amount of information inside the official documentation to help you around that.
 *     s3:
 *       endpoint: minio:9000
 *       insecure: true
 *       bucketnames: loki-data
 *       access_key_id: loki   # With some random key id
 *       secret_access_key: supersecret  # With some random secret key
 *       s3forcepathstyle: true
 *   ring:
 *     kvstore:
 *       store: memberlist
 * ruler:
 *   storage:
 *     s3:
 *       bucketnames: loki-ruler
 *
 * compactor:
 *   working_directory: /tmp/compactor
 *
 * Now, we understand all these 3 yml files and as a next step we need to set them up inside our docker-compose file. which we already have inside our ms's project.
 *
 *  * */
@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	/* This method is going to create a bean of type RouteLocator and return it.
	* Inside this RouteLocator only, we are going to send all our custom routing related configurations based on our requirements.
	**/
	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p.path("/eazybank/accounts/**")
						.filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/contactSupport")))
						.uri("lb://ACCOUNTS"))

				.route(p -> p.path("/eazybank/loans/**")
						.filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true))
						)
						.uri("lb://LOANS"))
				.route(p -> p.path("/eazybank/cards/**")
						.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
										.setKeyResolver(userKeyResolver())))
						.uri("lb://CARDS")).build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(1, 1, 1);
	}

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}


}
