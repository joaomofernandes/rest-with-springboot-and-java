package com.example.demo.integrationtests.testcontainers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTests.Initializer.class)
public class AbstractIntegrationTests {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.31");

        private static void startContainers(){
            Startables.deepStart(Stream.of(mySQLContainer)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
                return Map.of(
                        "spring.datasource.url", mySQLContainer.getJdbcUrl(),
                        "spring.datasource.username", mySQLContainer.getUsername(),
                        "spring.datasource.password", mySQLContainer.getPassword()
                );
        }
        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers = new MapPropertySource("testContainers", (Map)createConnectionConfiguration());
            environment.getPropertySources().addFirst(testContainers);
        }


    }
}
