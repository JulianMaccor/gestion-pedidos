package com.ejemplo.pedidos.support;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.MockMvcBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine");
    }

    // Required so @WithMockUser's SecurityContext survives Spring Security's
    // SecurityContextHolderFilter, which otherwise reloads an empty context
    // from the request under a stateless session policy.
    @Bean
    public MockMvcBuilderCustomizer securityMockMvcBuilderCustomizer() {
        return new MockMvcBuilderCustomizer() {
            @Override
            public void customize(ConfigurableMockMvcBuilder<?> builder) {
                builder.apply(SecurityMockMvcConfigurers.springSecurity());
            }
        };
    }
}
