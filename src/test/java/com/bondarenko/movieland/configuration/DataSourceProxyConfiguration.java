package com.bondarenko.movieland.configuration;

import com.bondarenko.proxydatasource.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;
import org.springframework.jdbc.datasource.*;

import javax.sql.*;

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