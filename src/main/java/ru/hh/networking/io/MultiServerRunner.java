package ru.hh.networking.io;

import java.io.IOException;

public class MultiServerRunner {
  public static void main(String[] args) {
    MultiThreadServer server = new MultiThreadServer(5656);
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
