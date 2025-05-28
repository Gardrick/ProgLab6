package common.commands;

import server.CollectionManager;

public class HelpCommand extends Command {
	private static final long serialVersionUID = 1L;
    public HelpCommand() {
        super("help");
    }

    @Override
    public String execute(CollectionManager collection) {
        return """
            Список доступных команд:
            - help : Вывести справку
            - info : Информация о коллекции
            - show : Показать все элементы
            - add {element} : Добавить элемент
            - update id {element} : Обновить элемент
            - remove_by_id id : Удалить по ID
            - clear : Очистить коллекцию
            - head : Показать первый элемент
            - add_if_max {element} : Добавить, если максимальный
            - remove_greater {element} : Удалить большие элементы
            - min_by_creation_date : Минимальная дата создания
            - group_counting_by_id : Группировка по ID
            - print_field_descending_distance : Дистанции по убыванию
            - execute_script file_name : Исполнить скрипт
            - exit : Выйти
            """;
    }
}