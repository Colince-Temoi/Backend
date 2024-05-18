package tech.csm.beanconfigurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfigurations {

	@Bean
	public RestTemplate	 getRestTemplate() {
		return new RestTemplate();
	}
}
