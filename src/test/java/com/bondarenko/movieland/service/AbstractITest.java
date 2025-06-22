package com.bondarenko.movieland.service;

import com.bondarenko.movieland.configuration.DataSourceProxyConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@ActiveProfiles("test")
@SpringBootTest(classes = {DataSourceProxyConfiguration.class})
public abstract class AbstractITest {
    private static final String POSTGRES_VERSION = "postgres:17.5";

    @Container
    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>(POSTGRES_VERSION)
                    .withReuse(true)
                    .withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test");

    static {
        container.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
