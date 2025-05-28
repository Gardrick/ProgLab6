package common.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import server.CollectionManager;

public class ExecuteScriptCommand extends Command {
	private static final long serialVersionUID = 1L;
    private String filename;
    private static final int MAX_SCRIPT_DEPTH = 50;
    private static final Deque<String> scriptStack = new ArrayDeque<>();

    public ExecuteScriptCommand() {
        super("execute_script");
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String execute(CollectionManager collection) {
        try {
            if (scriptStack.contains(filename)) {
                return "Обнаружена рекурсия! Файл: " + filename;
            }
            
            if (scriptStack.size() >= MAX_SCRIPT_DEPTH) {
                return "Достигнута максимальная глубина выполнения скриптов";
            }
            
            scriptStack.push(filename);
            
            List<String> lines = Files.readAllLines(Paths.get(filename));
            StringBuilder output = new StringBuilder();
            
            output.append("добавить с 5 лабы");
            
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty() || line.startsWith("//")) continue;
                
                String[] parts = line.split("\\s+", 2);
                String cmdName = parts[0];
                String args = parts.length > 1 ? parts[1] : "";
                
                if (cmdName.equals("execute_script")) {
                    ExecuteScriptCommand nestedCmd = new ExecuteScriptCommand();
                    nestedCmd.setFilename(args);
                    output.append("> ").append(line).append("\n");
                    output.append(nestedCmd.execute(collection)).append("\n");
                } else {
                    output.append("> ").append(line).append("\n");
                    try {
                        output.append(collection.executeCommand(cmdName, args)).append("\n");
                    } catch (Exception e) {
                        output.append("Ошибка: ").append(e.getMessage()).append("\n");
                    }
                }
            }
            
            return output.toString();
        } catch (IOException e) {
            return "Ошибка чтения файла: " + e.getMessage();
        } finally {
            scriptStack.pop();
        }
    }
}