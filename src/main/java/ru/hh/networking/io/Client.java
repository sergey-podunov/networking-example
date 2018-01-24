package ru.hh.networking.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
  public static void main(String[] args) {
    try (Socket socket = new Socket("localhost", 5656);
        BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
    ) {
      DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
      DataInputStream inputStream = new DataInputStream(socket.getInputStream());
      while(!socket.isOutputShutdown()) {
        if (br.ready()) {

          String clientCommand = br.readLine();

          outputStream.writeUTF(clientCommand);
          outputStream.flush();

          if (clientCommand.equalsIgnoreCase("quit")) {
            System.out.println("quit....");
            socket.close();
            break;
          }

          String serverAnswer = inputStream.readUTF();
          System.out.println(serverAnswer);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
