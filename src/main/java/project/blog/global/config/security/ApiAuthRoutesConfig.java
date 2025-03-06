package project.blog.global.config.security;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import project.blog.global.config.common.YamlPropertySourceFactory;

@Component
@ConfigurationProperties
@PropertySource(value = "classpath:config/api-auth-routes.yml", factory = YamlPropertySourceFactory.class)
@Getter
@Setter
public class ApiAuthRoutesConfig {

  private List<Route> routes;

  @Getter
  @Setter
  public static class Route {
    private String path;
    private String method;
  }

}
