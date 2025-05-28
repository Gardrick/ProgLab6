package common.data;

import java.io.Serializable;

import common.exceptions.InvalidDataException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement
    private double x;
	@XmlElement
    private double y;
    @XmlElement(required = true)
    private Integer z;
    @XmlElement
    private String name;

    public Location() {
    }
    
    public Location(Double x, Double y, Integer z, String name) {
        setX(x);
        setY(y);
        setZ(z);
        setName(name);
    }

    public void setX(double x) {
    	this.x = x;
    }

    public double getX() {
    	return this.x;
    }

    public void setY(double y) {
    	this.y = y;
    }

    public double getY() {
    	return this.y;
    }

    public void setZ(Integer z) {
    	if (z == null) {
    		throw new IllegalArgumentException("Z cannot be empty");
    	}
    	this.z = z;
    }

    public Integer getZ() {
    	return this.z;
    }

    public void setName(String name) {
    	if (name.length() > 757) {
    		throw new IllegalArgumentException("Leng of name must be less than 758");
    	}
    	this.name = name;
    }

    public String getName() {
    	return this.name;
    }
    
    public void validate() throws InvalidDataException {
        if (z == null) {
            throw new InvalidDataException("Координата Z обязательна");
        }
        if (name != null && name.length() > 757) {
            throw new InvalidDataException("Название локации не может быть длиннее 757 символов");
        }
    }

    @Override
    public String toString() {
        return String.format(
            "[X: %.2f, Y: %.2f, Z: %d, Name: %s]",
            x, y, z, name
        );
    }
}