/**
 *
 */
/**
 *
 */
module Lab6NEW {
	exports common.commands;
	exports common.data;
	exports common.exceptions;
	exports common.network;
	exports common.util;
	
    requires jakarta.xml.bind;
    requires org.glassfish.jaxb.runtime;
    requires com.sun.xml.txw2;

    opens common.data to jakarta.xml.bind, org.glassfish.jaxb.runtime;
    opens common.util to jakarta.xml.bind, org.glassfish.jaxb.runtime;
    opens server to jakarta.xml.bind, org.glassfish.jaxb.runtime;
}