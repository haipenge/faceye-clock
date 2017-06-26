package com.faceye.component;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = com.faceye.feature.repository.jpa.impl.BaseRepositoryImpl.class, basePackages = {
		"com.faceye" }, enableDefaultTransactions = true, repositoryImplementationPostfix = "Impl")
public class JpaConfiguration {
	// @Autowired
	// private DataSource dataSource;

	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource() {
		DataSource dataSource = null;
		dataSource = DataSourceBuilder.create().build();
		return dataSource;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setGenerateDdl(true);
		jpaVendorAdapter.setShowSql(true);
		jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
		return jpaVendorAdapter;
	}

	//
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setPackagesToScan("com.faceye");
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		Properties properties = new Properties();
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.jdbc.fetch_size", "100");
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		entityManagerFactoryBean.setJpaProperties(properties);
		entityManagerFactoryBean.setPersistenceUnitName("feature");
		return entityManagerFactoryBean;
	}
}
