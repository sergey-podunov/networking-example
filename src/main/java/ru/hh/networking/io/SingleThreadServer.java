package ru.hh.networking.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadServer {
  private final int port;

  public SingleThreadServer(int port) {
    this.port = port;
  }

  public void start() throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("Server started on port " + serverSocket.getLocalPort());
    while (true) {
      try (Socket socket = serverSocket.accept()) {
        System.out.println("Accept connection");
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        while (!socket.isInputShutdown()) {
          String data = inputStream.readUTF();
          System.out.println(data);
          outputStream.writeUTF("echo: " + data);
          outputStream.flush();
        }
      } catch (IOException e) {
        System.out.println("exception:" + e.getMessage());
      }
    }
  }
}
