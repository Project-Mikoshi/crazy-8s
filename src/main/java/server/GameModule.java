package server;

import java.util.HashMap;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.corundumstudio.socketio.SocketIOServer;
import config.GameConfig;
import constant.SocketEvent;
import lombok.Getter;
import lombok.Setter;
import model.*;

@Getter
@Setter
@Component
public class GameModule {
  private static final Logger log = LoggerFactory.getLogger(GameModule.class);

  // == Props ================================
  SocketIOServer server;
  HashMap<UUID, Player> players;

  // == Constructor ==========================
  @Autowired
  public GameModule (SocketIOServer server) {
    this.server = server;
  }

  // == Public Method ========================
  public void addPlayer (UUID id, String name) {
    players.putIfAbsent(id, new Player(id, name));
  }

  public void removePlayer (UUID id) {
    players.remove(id);
  }

  public boolean isReadyToStart () {
    return players.size() == GameConfig.NUM_OF_PLAYERS;
  }

  public void start () {
    log.info("Game has started");
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.MESSAGE, "Game started");
    server.getRoomOperations(GameConfig.GAME_ROOM).sendEvent(SocketEvent.GAME_START);
  }
}
