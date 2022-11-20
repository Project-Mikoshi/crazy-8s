package server;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerCommandLineRunner implements CommandLineRunner {
  // == Props ===============================
  private SocketIOServer server;

  // == Constructor ==========================
  @Autowired
  public ServerCommandLineRunner(SocketIOServer server) {
    this.server = server;
  }

  // == Public Method ========================
  @Override
  public void run(String... args) throws Exception {
    server.start();
  }
}
