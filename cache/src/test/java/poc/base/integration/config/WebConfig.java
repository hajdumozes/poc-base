package poc.base.integration.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAutoConfiguration
@EnableWebMvc
@Configuration
@EntityScan(basePackages = "poc.base.entity")
@EnableJpaRepositories(basePackages = "poc.base.repository")
@ComponentScan(basePackages = {"poc.base"})
public class WebConfig implements WebMvcConfigurer {
}