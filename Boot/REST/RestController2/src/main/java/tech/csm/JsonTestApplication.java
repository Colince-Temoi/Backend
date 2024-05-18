package tech.csm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class JsonTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonTestApplication.class, args);
	}

}
