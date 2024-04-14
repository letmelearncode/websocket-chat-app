A Java 9 based chat application which uses socket for bidirectional communication.

It has 2 nmain components:
1. Server : Accepts the websocket connections from the clients, receives the message, broadcasts to the respective clients and closes the same.
2. Client : Initiates the connection with server, send messages and recieves messages.
