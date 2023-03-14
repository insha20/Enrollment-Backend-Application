package com.springboot.Backend.AppConfig;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {"com.springboot.Backend.AppRepository"})
public class AppDatabaseConfig {

   @Value("${spring.datasource.url}")
   private String url;

   @Value("${spring.datasource.username}")
   private String username;

   @Value("${spring.datasource.password}")
   private String password;

   @Value("${spring.datasource.driver-class-name}")
   private String driverClassName;

   @Bean
   public DataSource dataSource() {
       DriverManagerDataSource dataSource = new DriverManagerDataSource();
       dataSource.setDriverClassName(driverClassName);
       dataSource.setUrl(url);
       dataSource.setUsername(username);
       dataSource.setPassword(password);
       return dataSource;
   }

   @Bean
   public EntityManagerFactory entityManagerFactory() {
       HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
       vendorAdapter.setGenerateDdl(true);

       LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
       factory.setJpaVendorAdapter(vendorAdapter);
       factory.setPackagesToScan("com.springboot.Backend.AppConfig","com.springboot.Backend.AppController","com.springboot.Backend.AppRepository","com.springboot.Backend.AppService","com.springboot.Backend.AppUser");
       factory.setDataSource(dataSource());
       factory.afterPropertiesSet();

       return factory.getObject();
   }
}
