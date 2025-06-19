package com.bondarenko.movieland.configuration;

import com.bondarenko.proxydatasource.ProxyDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
@TestConfiguration
public class DataSourceProxyConfiguration {
    @Autowired
    private Environment environment;

    @Bean
    @Primary
    public ProxyDataSource createDataSourceListener() {
        log.info("Proxy datasource start works ");
        return new ProxyDataSource(actualDataSource());
    }


    @Bean
    public DataSource actualDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        return dataSource;
    }
}