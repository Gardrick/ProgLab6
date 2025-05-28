package common.data;

import java.io.Serializable;

import common.exceptions.InvalidDataException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(required = true)
    private Integer x;
	@XmlElement(required = true)
    private float y;

    public Coordinates() {
    }
    
    public Coordinates(Integer x, Float y) {
        setX(x);
        setY(y);
    }

    public void setX(Integer x) {
    	if (x == null) {
    		throw new IllegalArgumentException("X cannot be empty");
    	}
    	this.x = x;
    }

    public Integer getX() {
    	return this.x;
    }

    public void setY(float y) {
    	if (y <= -290) {
    		throw new IllegalArgumentException("Y must be more than -290");
    	}
    	this.y = y;
    }

    public float getY() {
    	return this.y;
    }
    
    public void validate() throws InvalidDataException {
        if (x == null) {
            throw new InvalidDataException("Координата X обязательна");
        }
        if (y <= -290) {
            throw new InvalidDataException("Координата Y должна быть числом > -290");
        }
    }

    @Override
    public String toString() {
        return String.format("(X: %d, Y: %.2f)", x, y);
    }
}