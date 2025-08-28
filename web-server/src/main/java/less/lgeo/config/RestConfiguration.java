package less.lgeo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Defines CORS mapping that allows frontend NextJS server to GET/POST/DELETE to backend web-server
 */
@EnableWebMvc
@Configuration
@ComponentScan( basePackageClasses = RestConfiguration.class )
public class RestConfiguration implements WebMvcConfigurer {

  @Override
  public void addCorsMappings( CorsRegistry registry ) {
    registry.addMapping( "/v1/**" )
        .allowedOrigins( "http://localhost:3000" );
  }

}
