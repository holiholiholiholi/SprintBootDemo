package com.example.demo.data.configuration;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration	
@EnableJpaRepositories(basePackages = { DatabaseConfig.BASE_PACKAGE }, entityManagerFactoryRef = "demoEntityManagerFactory")
public class DatabaseConfig {
	
	protected static final String BASE_PACKAGE = "com.example.demo.data";
	protected static final String PREFIX = "demo.datasource";
	@Configuration
	public class PersistenceConfig {

		@Bean
		@Primary
		public LocalContainerEntityManagerFactoryBean demoEntityManagerFactory(final EntityManagerFactoryBuilder builder) {
			return builder.dataSource(demoDbDataSource()).packages(DatabaseConfig.BASE_PACKAGE).persistenceUnit("demoDBFactoryBean").build();
		}

		@Bean
		@Primary
		@ConfigurationProperties(prefix = DatabaseConfig.PREFIX)
		public DataSource demoDbDataSource() {
			return DataSourceBuilder.create().build();
		}
	}
}
