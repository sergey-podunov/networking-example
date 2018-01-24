package ru.hh.networking.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {
  private final int port;

  public MultiThreadServer(int port) {
    this.port = port;
  }

  public void start() throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("Server started on port " + serverSocket.getLocalPort());
    while (true) {
      try {
        Socket socket = serverSocket.accept();
        System.out.println("Accept connection");
        new Thread(new RequestHandler(socket)).start();
      } catch (IOException e) {
        System.out.println("exception:" + e.getMessage());
      }
    }
  }

  private class RequestHandler implements Runnable {
    private final Socket socket;

    public RequestHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        String threadName = Thread.currentThread().getName();
        System.out.println("Start handling connection in thread " + threadName);

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        while (!socket.isInputShutdown()) {
          String data = inputStream.readUTF();
          System.out.println("data in thread " + threadName + ": " + data);
          outputStream.writeUTF("echo: " + data);
          outputStream.flush();
        }
      } catch (IOException e) {
        System.out.println("exception:" + e.getMessage());
      } finally {
        try {
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}

