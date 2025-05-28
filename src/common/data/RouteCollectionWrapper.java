package common.data;

import java.util.ArrayDeque;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "routes")
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteCollectionWrapper {
    @XmlElement(name = "route")
    private ArrayDeque<Route> routes;

    public RouteCollectionWrapper() {
    }

    public RouteCollectionWrapper(ArrayDeque<Route> routes) {
        this.routes = routes;
    }

    public ArrayDeque<Route> getRoutes() {
        return routes;
    }
}