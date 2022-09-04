package demo.spring.springJwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "web")
public class WebConfigProperties {

    private Cors cors;

    @Data
    public static class Cors {

        private String[] allowedOrigins = { };

        private String[] allowedMethods = { };
        
        private String[] allowedHeaders = { };
        
        private String[] exposedHeaders = { };

        private long maxAge = 3600;

    }
}