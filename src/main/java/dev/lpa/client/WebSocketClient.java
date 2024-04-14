package dev.lpa.client;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;
import java.net.http.WebSocket.Listener;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class WebSocketClient {

  public static void main(String[] args)
      throws URISyntaxException {

    Scanner sc = new Scanner(System.in);
    System.out.println("Please enter your name to join chat : ");
    String name = sc.nextLine();
    HttpClient client =  HttpClient.newHttpClient();
    WebSocket webSocket = client.newWebSocketBuilder().buildAsync(new URI(
            String.format("ws://localhost:8080?name=%s", name)),
        new Listener() {
          @Override
          public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            System.out.println(data);
            return Listener.super.onText(webSocket, data, last);
          }
        }).join();

    while(true){
      String input = sc.nextLine();
      if("bye".equalsIgnoreCase(input)){
        try {
          webSocket.sendClose(WebSocket.NORMAL_CLOSURE,"User left normally").get();
        } catch (InterruptedException | ExecutionException e) {
          throw new RuntimeException(e);
        }
      }
      else{
        webSocket.sendText(input,true);
      }
    }


  }

}
