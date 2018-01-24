package ru.hh.networking.io;

import java.io.IOException;

public class SingleServerRunner {
  public static void main(String[] args) {
    SingleThreadServer server = new SingleThreadServer(5656);
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
