package tech.csm.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {

	@Bean
	public DataSource getDataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
		dataSourceBuilder.url("jdbc:mysql://localhost:3306/collage_admn");
		dataSourceBuilder.username("root");
		dataSourceBuilder.password("Tmi@2022");
		return dataSourceBuilder.build();
	}
}
