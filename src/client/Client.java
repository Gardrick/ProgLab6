package client;

import common.network.Request;
import common.network.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class Client implements AutoCloseable {
    private static final int BUFFER_SIZE = 65536;
    private final DatagramChannel channel;
    private final InetSocketAddress serverAddress;

    public Client(String host, int port) throws Exception {
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        //channel.connect(new InetSocketAddress("192.168.86.44", port));
        channel.connect(new InetSocketAddress(host, port));
        //this.serverAddress = new InetSocketAddress(host, port);
        this.serverAddress = new InetSocketAddress("192.168.86.44", port);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
        
        System.out.println("Подключено к серверу: " + host + ":" + port);
    }

    public Response sendRequest(Request request) throws Exception {
        int attempts = 3;
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
        
        ByteBuffer buffer = serialize(request);
        
        while (attempts-- > 0) {
            channel.write(buffer);
            buffer.rewind();
            
            selector.select(2000);
            if (selector.selectedKeys().isEmpty()) {
                System.out.println("Таймаут, попыток осталось: " + attempts);
                continue;
            }
            
            ByteBuffer responseBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            channel.receive(responseBuffer);
            responseBuffer.flip();
            return deserialize(responseBuffer);
        }
        throw new SocketTimeoutException("Сервер не ответил после 3 попыток");
    }

    private static Response deserialize(ByteBuffer buffer) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Response) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка десериализации", e);
        }
    }

    private static ByteBuffer serialize(Request request) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(request);
            return ByteBuffer.wrap(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации", e);
        }
    }

    @Override
    public void close() throws Exception {
        channel.close();
    }
}