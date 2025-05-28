package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import common.data.Coordinates;
import common.data.Location;
import common.data.Route;
import common.data.RouteCollectionWrapper;
import common.exceptions.EmptyCollectionException;
import common.exceptions.FileAccessException;
import common.exceptions.InvalidDataException;
import common.exceptions.NotFoundException;
import common.util.ZonedDateTimeAdapter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class CollectionManager {
    private ArrayDeque<Route> collection = new ArrayDeque<>();
    private ZonedDateTime initDate = ZonedDateTime.now();
    private String dataFile;

    public CollectionManager(String dataFile) {
        this.dataFile = dataFile;
        loadFromFile();
    }

    public synchronized void add(Route route) throws InvalidDataException {
        validateRoute(route);
        collection.add(route);
    }

    public synchronized void update(int id, Route newRoute) throws NotFoundException {
    	Route oldRoute = collection.stream()
    	        .filter(r -> r.getId() == id)
    	        .findFirst()
    	        .orElseThrow(() -> new NotFoundException("Элемент не найден"));

	    newRoute.setId(oldRoute.getId());
	    newRoute.setCreationDate(oldRoute.getCreationDate());

	    collection.remove(oldRoute);
	    collection.add(newRoute);
    }

    public synchronized void removeById(int id) throws NotFoundException {
        Route route = getById(id);
        collection.remove(route);
    }

    public synchronized void clear() {
        collection.clear();
    }

    public synchronized void addIfMax(Route route) throws InvalidDataException {
        validateRoute(route);
        Optional<Route> maxRoute = collection.stream().max(Comparator.naturalOrder());
        if (maxRoute.isEmpty() || route.compareTo(maxRoute.get()) > 0) {
            collection.add(route);
        }
    }

    public synchronized int removeGreater(Route route) throws EmptyCollectionException {
        if (collection.isEmpty()) throw new EmptyCollectionException();
        
        int initialSize = collection.size();
        collection = collection.stream()
                .filter(r -> r.compareTo(route) <= 0)
                .collect(Collectors.toCollection(ArrayDeque::new));
        return initialSize - collection.size();
    }

    public Map<Integer, Long> groupCountingById() {
        return collection.stream()
                .collect(Collectors.groupingBy(
                        Route::getId,
                        Collectors.counting()
                ));
    }

    public List<Long> getDescendingDistances() {
        return collection.stream()
                .map(Route::getDistance)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public String getInfo() {
        return String.format(
            "Тип коллекции: %s\nДата инициализации: %s\nКоличество элементов: %d",
            collection.getClass().getSimpleName(),
            initDate.format(DateTimeFormatter.ISO_DATE_TIME),
            collection.size()
        );
    }

    public ArrayDeque<Route> getSortedCollection() {
        return collection.stream()
            .sorted()
            .collect(Collectors.toCollection(ArrayDeque::new));
    }

    public synchronized Optional<Route> getHead() {
        return Optional.ofNullable(collection.peekFirst());
    }

    public Route getMinByCreationDate() throws EmptyCollectionException {
        return collection.stream()
            .min(Comparator.comparing(Route::getCreationDate))
            .orElseThrow(EmptyCollectionException::new);
    }

    public Route getById(int id) throws NotFoundException {
        return collection.stream()
            .filter(r -> r.getId() == id)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Элемент с ID " + id + " не найден"));
    }
    
    public synchronized String executeCommand(String commandName, String args) {
    	try {
            switch (commandName) {
                default:
                    return "Неизвестная команда: " + commandName;
            }
        } catch (Exception e) {
            return "Ошибка выполнения: " + e.getMessage();
        }
    }

    private void validateRoute(Route route) throws InvalidDataException {
        if (route.getName() == null || route.getName().isEmpty()) {
            throw new InvalidDataException("Название маршрута не может быть пустым");
        }
        if (route.getDistance() <= 1) {
            throw new InvalidDataException("Дистанция должна быть > 1");
        }
        if (route.getCoordinates() == null) {
            throw new InvalidDataException("Координаты обязательны");
        }
        route.getCoordinates().validate();
        if (route.getFrom() != null) {
            route.getFrom().validate();
        }
        if (route.getTo() != null) {
            route.getTo().validate();
        }
    }

    public void saveToFile() throws FileAccessException {
        try (OutputStream os = new FileOutputStream(dataFile)) {
        	JAXBContext context = JAXBContext.newInstance(
        		    RouteCollectionWrapper.class,
        		    Route.class,
        		    Coordinates.class,
        		    Location.class,
        		    ZonedDateTimeAdapter.class
        		);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            RouteCollectionWrapper wrapper = new RouteCollectionWrapper(collection);
            marshaller.marshal(wrapper, os);

        } catch (JAXBException | IOException e) {
            throw new FileAccessException("Невозможно сохранить файл: " + dataFile);
        }
    }

    public void loadFromFile() {
        try (InputStream is = new FileInputStream(dataFile)) {
            JAXBContext context = JAXBContext.newInstance(RouteCollectionWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            unmarshaller.setEventHandler(event -> {
                System.err.println("XML Error: " + event.getMessage());
                return false;
            });

            RouteCollectionWrapper wrapper = (RouteCollectionWrapper) unmarshaller.unmarshal(is);
            
            List<Route> loadedRoutes = new ArrayList<>();
            for (Route xmlRoute : wrapper.getRoutes()) {
                try {
                    Route route = new Route(xmlRoute.getId());
                    
                    if (xmlRoute.getName() == null || xmlRoute.getName().isEmpty()) {
                        throw new IllegalArgumentException("Обнаружен объект с пустым именем");
                    }
                    route.setName(xmlRoute.getName());
                    
                    route.setCoordinates(xmlRoute.getCoordinates());
                    route.setCreationDate(
                    	    xmlRoute.getCreationDate() != null 
                    	        ? xmlRoute.getCreationDate() 
                    	        : ZonedDateTime.now()
                    	);
                    route.setFrom(xmlRoute.getFrom());
                    route.setTo(xmlRoute.getTo());
                    route.setDistance(xmlRoute.getDistance());
                    
                    loadedRoutes.add(route);
                } catch (IllegalArgumentException e) {
                    System.err.println("Пропущен некорректный объект: " + e.getMessage());
                }
            }

            collection.clear();
            collection.addAll(loadedRoutes);

            int maxId = loadedRoutes.stream()
                .mapToInt(Route::getId)
                .max()
                .orElse(0);

            Route.updateIdCounter(maxId);

        } catch (JAXBException | IOException e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
        }
    }
}