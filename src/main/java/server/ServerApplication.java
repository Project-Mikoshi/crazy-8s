package server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;

@SpringBootApplication
public class ServerApplication {
  // == Main =================================
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

  // == WebSocket Server =====================
  @Value("${server.port}")
  private Integer port;

  @Value("${server.address}")
  private String host;

  @Bean
  public SocketIOServer socketIOServer () {    
    SocketConfig socketConfig = new SocketConfig();
    socketConfig.setReuseAddress(true);
    
    Configuration config = new Configuration();
    config.setHostname(host);
    config.setPort(8081);
    config.setSocketConfig(socketConfig);
    config.setRandomSession(true);

    return new SocketIOServer(config);
  }
}