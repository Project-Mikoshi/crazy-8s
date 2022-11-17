package server;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import constant.SocketEvent;
import game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameSocketServer {
  // == Constants ============================
  private static final Logger log = LoggerFactory.getLogger(GameSocketServer.class);
  private final SocketIOServer server;
  private final String ROOM = "game";

  // == Props ================================
  private Game game;

  // == Constructor ==========================
  @Autowired
  public GameSocketServer (SocketIOServer server) {
    this.server = server;
    this.server.addConnectListener(onConnected());
    this.server.addDisconnectListener(onDisconnected());
    this.server.addEventListener(SocketEvent.JOIN_GAME, String.class, onJoiningGameRoom());
    log.info("socket io event handler registered");
  }

  // == Event Handler ========================
  private DataListener<String> onJoiningGameRoom () {
    return (client, playerName, ackSender) -> {
      log.info("Client[{}] - Received client message '{}'", client.getSessionId().toString(), playerName);
      client.joinRoom(ROOM);
      client.sendEvent(SocketEvent.JOIN_GAME);
      server.getRoomOperations(ROOM).sendEvent(SocketEvent.MESSAGE, "Player: %s has joined in the room".formatted(playerName));

      game.addPlayer(playerName, playerName);
    };
  }

  private ConnectListener onConnected () {
    return client -> {
      log.info("Client[{}] - Connected to socket", client.getSessionId().toString());
      client.sendEvent(SocketEvent.CONNECTION);
    };
  }

  private DisconnectListener onDisconnected () {
    return client -> {
      log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
      client.sendEvent(SocketEvent.DISCONNECT);
    };
  }
}
