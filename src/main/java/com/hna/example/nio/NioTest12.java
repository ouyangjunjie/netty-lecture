package com.hna.example.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioTest12 {

    public static void main(String[] args) throws IOException {
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        Selector selector = Selector.open();
        for (int port : ports) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSocket.bind(address);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.err.println("监听端口:" + port);
        }

        while (true) {
            int numbers = selector.select();
            System.err.println("numbers : " + numbers);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.err.println("selectionKeys : " + selectionKeys);

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    iterator.remove();
                    System.err.println("获取客户端连接: " + socketChannel);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int byteRead = 0;

                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        byteBuffer.clear();
                        int count = socketChannel.read(byteBuffer);
                        if (count <= 0) {
                            break;
                        }
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);

                        byteRead += count;
                    }

                    System.err.println("读取：" + byteRead + ", 来自于:" + socketChannel);

                }
            }
        }
    }
}
