package client;

import common.data.Coordinates;
import common.data.Location;
import common.data.Route;
import common.exceptions.InvalidDataException;
import java.time.ZonedDateTime;
import java.util.Scanner;


public class InputReader {
    private final Scanner scanner = new Scanner(System.in);

    public Route readRoute() {
        Route route = new Route();
        
        route.setCreationDate(ZonedDateTime.now());

        route.setName(readString("Введите название маршрута: ", false));
        route.setCoordinates(readCoordinates());
        route.setDistance(readLong("Введите дистанцию (целое число > 1): ", 2, Long.MAX_VALUE));

        route.setFrom(readLocation("Введите данные локации отправления (Enter чтобы пропустить): "));
        route.setTo(readLocation("Введите данные локации назначения (Enter чтобы пропустить): "));

        return route;
    }

    private Coordinates readCoordinates() {
        System.out.println("-- Ввод координат --");
        Integer x = readInteger("Введите координату X (целое число): ", false);
        Float y = readFloat("Введите координату Y (число > -290): ", -289.99999f, Float.MAX_VALUE);
        return new Coordinates(x, y);
    }

    private Location readLocation(String prompt) {
        System.out.print(prompt);
        if (scanner.nextLine().trim().isEmpty()) return null;

        System.out.println("-- Ввод локации --");
        Double x = readDouble("Введите координату X: ", true);
        Double y = readDouble("Введите координату Y: ", true);
        Integer z = readInteger("Введите координату Z (целое число): ", false);
        String name = readString("Введите название локации (до 757 символов): ", true);
        
        return new Location(x, y, z, name);
    }

    private String readString(String prompt, boolean nullable) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (!nullable && input.isEmpty()) {
                System.out.println("Значение не может быть пустым!");
            } else {
                return input.isEmpty() ? null : input;
            }
        }
    }

    private Integer readInteger(String prompt, boolean nullable) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (nullable && input.isEmpty()) return null;
            
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: требуется целое число!");
            }
        }
    }

    private Float readFloat(String prompt, float min, float max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            try {
                float value = Float.parseFloat(input);
                if (value <= min) {
                    System.out.printf("Значение должно быть > %.2f!%n", min);
                } else if (value > max) {
                    System.out.printf("Значение должно быть <= %.2f!%n", max);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: требуется число!");
            }
        }
    }

    private Long readLong(String prompt, long min, long max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            try {
                long value = Long.parseLong(input);
                if (value < min) {
                    System.out.printf("Значение должно быть >= %d!%n", min);
                } else if (value > max) {
                    System.out.printf("Значение должно быть <= %d!%n", max);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: требуется целое число!");
            }
        }
    }

    private Double readDouble(String prompt, boolean nullable) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (nullable && input.isEmpty()) return null;
            
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: требуется число!");
            }
        }
    }
}