package dev.felix2000jp.springboottemplate.system.configurations;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@Import(SecurityConfiguration.class)
@TestConfiguration(proxyBeanMethods = false)
public class TestSecurityConfiguration {
}
