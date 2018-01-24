package ru.hh.networking.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOSingleThreadServer {
  private final int port;
  private ByteBuffer echoBuffer = ByteBuffer.allocate( 1024 );

  public NIOSingleThreadServer(int port) {
    this.port = port;
  }

  public void start() throws IOException {
    Selector selector = Selector.open();

    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.configureBlocking(false);

    ServerSocket serverSocket = ssc.socket();
    InetSocketAddress address = new InetSocketAddress(port);
    serverSocket.bind(address);

    ssc.register(selector, SelectionKey.OP_ACCEPT);

    System.out.println("Server started on port " + serverSocket.getLocalPort());

    while (true) {
      int num = selector.select();

      Set selectedKeys = selector.selectedKeys();
      Iterator it = selectedKeys.iterator();

      while (it.hasNext()) {
        SelectionKey key = (SelectionKey) it.next();

        if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
          ServerSocketChannel sscAccept = (ServerSocketChannel) key.channel();
          SocketChannel scAccept = sscAccept.accept();
          scAccept.configureBlocking(false);

          scAccept.register(selector, SelectionKey.OP_READ);
          it.remove();

          System.out.println("Got connection from " + scAccept);
        } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
          SocketChannel sc = (SocketChannel) key.channel();

          int bytesEchoed = 0;
          while (true) {
            echoBuffer.clear();

            int r = sc.read(echoBuffer);

            if (r <= 0) {
              break;
            }

            echoBuffer.flip();

            sc.write(echoBuffer);
            bytesEchoed += r;
          }

          System.out.println("Echoed " + bytesEchoed + " from " + sc);

          it.remove();
        }
      }
    }
  }
}
