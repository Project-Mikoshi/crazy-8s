package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class GameModule {
  // == Props ================================
  SocketIOServer server;

  // == Constructor ==========================
  @Autowired
  public GameModule (SocketIOServer server) {
    this.server = server;
  }

}
