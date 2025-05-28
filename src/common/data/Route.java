package common.data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import common.util.ZonedDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "route")
@XmlAccessorType(XmlAccessType.FIELD)
public class Route implements Comparable<Route>, Serializable {
	private static final long serialVersionUID = 1L;
	private static int nextId = 0;

    @XmlAttribute
    private int id;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private Coordinates coordinates;
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime creationDate;
    @XmlElement
    private Location from;
    @XmlElement
    private Location to;
    @XmlElement(required = true)
    private long distance;
    
    public Route() {
        this.id = nextId++;
        this.creationDate = ZonedDateTime.now();
    }

    public Route(int id) {
        this.id = id;
        this.creationDate = ZonedDateTime.now();
    }
    
    public Route(String name, Coordinates coordinates, long distance) {
        this();
        setName(name);
        setCoordinates(coordinates);
        setDistance(distance);
    }
    
    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("ID должен быть >= 0");
        this.id = id;
    }
    
    public int getId() {
    	return this.id;
    }
    
    public void setCreationDate(ZonedDateTime creationDate) {
    	this.creationDate = creationDate;
    }
    
    public ZonedDateTime getCreationDate() {
    	return this.creationDate;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название маршрута не может быть пустым");
        }
        this.name = name;
    }

    public String getName() {
    	return this.name;
    }

    public void setCoordinates(Coordinates coordinates) {
    	if (coordinates == null) {
    		throw new IllegalArgumentException("Координаты не могут быть пустыми");
    	}
    	this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
    	return this.coordinates;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getFrom() {
    	return this.from;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public Location getTo() {
    	return this.to;
    }

    public void setDistance(Long distance) {
        if (distance == null) {
            throw new IllegalArgumentException("Дистанция не может быть пустой");
        }
        else if (distance <= 1 ) {
            throw new IllegalArgumentException("Дистанция должна быть больше 1");
        }
        this.distance = distance;
    }

    public Long getDistance() {
    	return this.distance;
    }
    
    public static void updateIdCounter(int maxId) {
        nextId = maxId + 1;
    }

    @Override
    public int compareTo(Route o) {
        return Long.compare(this.distance, o.distance);
    }

    @Override
    public String toString() {
        return String.format(
            "Route [ID: %d]\n- Name: %s\n- Coordinates: %s\n- Creation Date: %s\n- From: %s\n- To: %s\n- Distance: %d",
            id, 
            name,
            coordinates,
            creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            from,
            to,
            distance
        );
    }
}
