package client;

public class ClientApp {
    public static void main(String[] args) {
        try (Client client = new Client("helios.cs.ifmo.ru", 39276);) {
            InputReader inputReader = new InputReader();
            ClientCommandHandler handler = new ClientCommandHandler(client, inputReader);
            
            System.out.println("Клиент запущен. Введите команду (help для справки):");
            handler.start();
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}