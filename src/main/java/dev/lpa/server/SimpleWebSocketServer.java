package dev.lpa.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

@Slf4j
public class SimpleWebSocketServer extends WebSocketServer {

  public static final int SERVER_PORT = 8080;
  private  Map<String, String> map = new HashMap<>();

  public SimpleWebSocketServer() {
    super(new InetSocketAddress(SERVER_PORT));
  }

  public static void main(String[] args) {
    SimpleWebSocketServer server = new SimpleWebSocketServer();
    server.start();
  }

  public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    String name = webSocket.getResourceDescriptor().split("=")[1];
    map.put(webSocket.getRemoteSocketAddress().toString(), name);
    System.out.println(map.values());
    System.out.println("Connection opened : "+ webSocket.getRemoteSocketAddress());
    broadcastAllButSender(webSocket, String.format("%s joined.", name));

  }

  public void onClose(WebSocket webSocket, int i, String s, boolean b) {
    String name = webSocket.getResourceDescriptor().split("=")[1];
    map.remove(webSocket.getRemoteSocketAddress().toString());
    System.out.println("Connection closed : "+ webSocket.getRemoteSocketAddress());
    broadcastAllButSender(webSocket, String.format("%s left.", name));
  }

  public void onMessage(WebSocket webSocket, String s) {
    System.out.println("Message from  : "+ webSocket.getRemoteSocketAddress());
    String chatName = map.get(webSocket.getRemoteSocketAddress().toString());
    broadcastAllButSender(webSocket, String.format("%s:%s", chatName, s));

  }

  private void broadcastAllButSender(WebSocket webSocket, String message){
    List<WebSocket> connections = new ArrayList<>(getConnections());
    connections.remove(webSocket);
    broadcast(message, connections);

  }

  public void onError(WebSocket webSocket, Exception e) {
    System.out.println("Error for : "+ webSocket.getRemoteSocketAddress());

  }

  public void onStart() {
    System.out.println("Server listening on port :" + SERVER_PORT);
  }
}
