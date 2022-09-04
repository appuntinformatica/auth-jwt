package demo.spring.springJwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import demo.spring.springJwt.config.WebConfigProperties;
import demo.spring.springJwt.model.Roles;
import demo.spring.springJwt.security.filter.AuthTokenFilter;
import demo.spring.springJwt.util.AuthEntryPointJwt;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(WebConfigProperties.class)
public class SecurityConfig {
	
	 private final WebConfigProperties webConfigProperties;

    public SecurityConfig(final WebConfigProperties webConfigProperties) {
        this.webConfigProperties = webConfigProperties;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    
    @Bean
    public WebMvcConfigurer corsMappingConfigurer() {
       return new WebMvcConfigurer() {
           @Override
           public void addCorsMappings(CorsRegistry registry) {
               WebConfigProperties.Cors cors = webConfigProperties.getCors();
               
               registry.addMapping("/**")
                 .allowedOrigins(cors.getAllowedOrigins())
                 .allowedMethods(cors.getAllowedMethods())
                 .maxAge(cors.getMaxAge())
                 .allowedHeaders(cors.getAllowedHeaders())
                 .exposedHeaders(cors.getExposedHeaders());
           }
       };
    }
    
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    	http.cors()
    	.and()
    		.csrf().disable()
    			.exceptionHandling().
    				authenticationEntryPoint(unauthorizedHandler)
		.and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeHttpRequests((authz) -> authz
                    .antMatchers(HttpMethod.POST, "/login/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/users/**").hasAuthority(Roles.ROLE_ADMIN.name())
                    .anyRequest().authenticated()
            )
            
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .headers().cacheControl();

        return http.build();
    }

}
