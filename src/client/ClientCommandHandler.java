package client;

import common.commands.*;
import common.data.Route;
import common.exceptions.InvalidArgumentException;
import common.network.*;

import java.io.IOException;
import java.util.Scanner;

public class ClientCommandHandler {
    private final Client client;
    private final InputReader inputReader;
    private final Scanner scanner = new Scanner(System.in);

    public ClientCommandHandler(Client client, InputReader inputReader) {
        this.client = client;
        this.inputReader = inputReader;
    }

    public void start() throws InvalidArgumentException {
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            processCommand(input);
        }
    }

    private void processCommand(String input) throws InvalidArgumentException {
        String[] parts = input.split(" ", 2);
        String commandName = parts[0];
        String args = parts.length > 1 ? parts[1] : "";
        
        if (input.trim().equalsIgnoreCase("exit")) {
            System.out.println("Завершение работы клиента...");
            try {
                client.close();
            } catch (Exception e) {
            	System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
			}
            System.exit(0);
        }
        
        try {
        	Command command = createCommand(commandName, args);
        	sendCommand(command);
        } catch (InvalidArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Фатальная ошибка: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private void sendCommand(Command command) {
        try {
            Request request = new Request(command);
            Response response = client.sendRequest(request);
            System.out.println(response.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
    
    Command createCommand(String name, String args) throws InvalidArgumentException {
	    return switch (name.toLowerCase()) {
	        //no args
	        case "help" -> new HelpCommand();
	        case "info" -> new InfoCommand();
	        case "show" -> new ShowCommand();
	        case "clear" -> new ClearCommand();
	        case "exit" -> new ExitCommand();
	        case "head" -> new HeadCommand();
	        case "min_by_creation_date" -> new MinByCreationDateCommand();
	        case "group_counting_by_id" -> new GroupCountingByIdCommand();
	        case "print_field_descending_distance" -> new PrintFieldDescendingDistanceCommand();
	
	        //simple args
	        case "remove_by_id" -> createRemoveByIdCommand(args);
	        case "execute_script" -> createExecuteScriptCommand(args);
	
	        //comp args
	        case "add" -> createAddCommand();
	        case "update" -> createUpdateCommand(args);
	        case "add_if_max" -> createAddIfMaxCommand();
	        case "remove_greater" -> createRemoveGreaterCommand();
	
	        default -> throw new InvalidArgumentException("Неизвестная команда: " + name);
    	};
    }

    /**
     * Создает команду remove_by_id.
     * Формат: remove_by_id <id>
     */
    private Command createRemoveByIdCommand(String args) throws InvalidArgumentException {
        if (args.isEmpty()) throw new InvalidArgumentException("Требуется аргумент: id");
        
        try {
            int id = Integer.parseInt(args);
            RemoveByIdCommand cmd = new RemoveByIdCommand();
            cmd.setId(id);
            return cmd;
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("ID должен быть целым числом");
        }
    }

    /**
     * Создает команду execute_script.
     * Формат: execute_script <file_name>
     */
    private Command createExecuteScriptCommand(String args) throws InvalidArgumentException {
        if (args.isEmpty()) throw new InvalidArgumentException("Требуется аргумент: имя файла");
        
        ExecuteScriptCommand cmd = new ExecuteScriptCommand();
        cmd.setFilename(args);
        return cmd;
    }

    /**
     * Создает команду add.
     * Запрашивает у пользователя ввод полей Route.
     */
    private Command createAddCommand() {
        Route route = inputReader.readRoute();
        AddCommand cmd = new AddCommand();
        cmd.setRoute(route);
        return cmd;
    }

    /**
     * Создает команду update.
     * Формат: update <id>
     * Затем запрашивает ввод полей Route.
     */
    private Command createUpdateCommand(String args) throws InvalidArgumentException {
        if (args.isEmpty()) throw new InvalidArgumentException("Требуется аргумент: id");
        
        try {
            int id = Integer.parseInt(args);
            Route route = inputReader.readRoute();
            
            UpdateCommand cmd = new UpdateCommand();
            cmd.setId(id);
            cmd.setUpdatedRoute(route);
            return cmd;
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("ID должен быть целым числом");
        }
    }

    /**
     * Создает команду add_if_max.
     * Запрашивает у пользователя ввод полей Route.
     */
    private Command createAddIfMaxCommand() {
        Route route = inputReader.readRoute();
        AddIfMaxCommand cmd = new AddIfMaxCommand();
        cmd.setRoute(route);
        return cmd;
    }

    /**
     * Создает команду remove_greater.
     * Запрашивает у пользователя ввод полей Route.
     */
    private Command createRemoveGreaterCommand() {
        Route route = inputReader.readRoute();
        RemoveGreaterCommand cmd = new RemoveGreaterCommand();
        cmd.setRoute(route);
        return cmd;
    }
}