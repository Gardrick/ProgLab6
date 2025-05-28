package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import common.commands.ExitCommand;
import common.exceptions.FileAccessException;
import common.network.Request;
import common.network.Response;

public class Server {
    private static final int PORT = 39276;
    private static final int BUFFER_SIZE = 65536;
    private static CollectionManager collectionManager;
    private static Selector selector;

    public static void main(String[] args) throws Exception {
    	if (args.length < 1) {
            System.err.println("Usage: java -jar Server.jar <datafile.xml>");
            System.exit(1);
        }
    	collectionManager = new CollectionManager(args[0]);
    	
    	DatagramChannel channel = DatagramChannel.open();
    	try {
    		channel.bind(new InetSocketAddress("0.0.0.0", PORT));
    		//channel.bind(new InetSocketAddress("helios.cs.ifmo.ru", PORT));
    	} catch (Exception e) {
    	    System.err.println("Ошибка биндинга порта: " + e.getMessage());
    	    System.exit(1);
    	}
    	channel.configureBlocking(false);
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
        
        System.out.println("Сервер привязан к: " + channel.getLocalAddress());
        System.out.println("Сервер запущен. Ожидание запросов...");
        System.out.println("save: сохранить коллекцию");
        System.out.println("exit: сохранить коллекцию и остановить сервер");
        
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String command = scanner.nextLine().trim();
                if (command.equalsIgnoreCase("save")) {
                    try {
						collectionManager.saveToFile();
					} catch (FileAccessException e) {
						System.out.println("Не удалось сохранить коллекцию: " + e);
					}
                    System.out.println("Коллекция сохранена");
                } else if (command.equalsIgnoreCase("exit")) {
                    try {
						collectionManager.saveToFile();
					} catch (FileAccessException e) {
						System.out.println("Не удалось сохранить коллекцию: " + e);
					}
                    System.out.println("Сохранение и завершение работы");
                    System.exit(0);
                }
            }
        }).start();
        
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }
    
    private static void handleRead(SelectionKey key) {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        
        try {
            InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
            
            if (clientAddress != null) {
                buffer.flip();
                
                System.out.println("Получен запрос от: " + clientAddress.getAddress() + ":" + clientAddress.getPort());
                
                Request request = deserialize(buffer);
                Response response = processRequest(request);
                
                ByteBuffer responseBuffer = serialize(response);
                channel.send(responseBuffer, clientAddress);
            }
        } catch (IOException e) {
            System.err.println("Ошибка обработки запроса: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Response processRequest(Request request) {
    	if (request.getCommand() instanceof ExitCommand) {
            System.out.println("Клиент инициировал отключение");
            return new Response("exit_acknowledged");
        }
    	CommandProcessor processor = new CommandProcessor(collectionManager);
        return processor.process(request);
    }
    
    private static void sendResponse(DatagramChannel channel, SocketAddress clientAddress, Response response) {
        try {
            ByteBuffer buffer = serialize(response);
            channel.send(buffer, clientAddress);
        } catch (IOException e) {
            System.err.println("Ошибка отправки ответа: " + e.getMessage());
        }
    }
    
    private static Request deserialize(ByteBuffer buffer) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Request) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка десериализации", e);
        }
    }

    private static ByteBuffer serialize(Response response) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(response);
            return ByteBuffer.wrap(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сериализации", e);
        }
    }
}