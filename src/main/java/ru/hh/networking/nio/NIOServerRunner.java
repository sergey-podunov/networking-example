package ru.hh.networking.nio;

import java.io.IOException;

public class NIOServerRunner {
  public static void main(String[] args) {
    NIOSingleThreadServer server = new NIOSingleThreadServer(5656);
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
