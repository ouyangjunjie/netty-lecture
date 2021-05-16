package com.hna.example.nio;

import io.netty.channel.ServerChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class NioTest13 {

    public static final Map<String, SocketChannel> clientMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.stream().forEach(selectionKey -> {
                    final SocketChannel client;
                    try {
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                            client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);

                            String key = "[" + UUID.randomUUID().toString() + "]";
                            clientMap.put(key, client);
//                            selectionKey.cancel();
                        } else if (selectionKey.isReadable()) {
                            client = (SocketChannel) selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                            int count = client.read(readBuffer);
                            if (count > 0) {
                                readBuffer.flip();
                                Charset charset = Charset.forName("utf-8");
                                String receiveMessage = String.valueOf(charset.decode(readBuffer).array());
                                System.err.println(client + ": " + receiveMessage);

                                String sendKey = clientMap.entrySet().stream().filter(entry -> client == entry.getValue()).findFirst().get().getKey();
                                final String returnMessage = sendKey + ":" + receiveMessage;
                                clientMap.entrySet().stream().filter(entry -> client != entry.getValue()).forEach(
                                        entry -> {
                                            SocketChannel channel = entry.getValue();
                                            ByteBuffer writeBuffer = ByteBuffer.allocate(returnMessage.length() + 1);
                                            writeBuffer.put(returnMessage.getBytes());
                                            writeBuffer.flip();
                                            try {
                                                channel.write(writeBuffer);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                );
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                selectionKeys.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
