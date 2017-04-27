package orders.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("BackendConfig")
@ConfigurationProperties(prefix = "backend")
public class BackendConfig {
  private String host;
  private String port;
  public String getHost() { return host; }
  public void setHost(String value) { host = value; }
  public String getPort() { return port; }
  public void setPort(String value) { port = value; }
}


