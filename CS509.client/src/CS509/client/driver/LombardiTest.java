package CS509.client.driver;

import CS509.client.dao.ServerInterface;
import CS509.client.flight.*;
import CS509.client.airport.*;

/**
 * 
 * @author lombardi
 *
 */
public class LombardiTest {
	
	/** Let's see what can be tested/implemented.
	 * @param args
	 */
	public static void main(String[] args) {
		
		ServerInterface resSys = new ServerInterface();
		
		//Do the admin stuff
		String xmlAirport = resSys.getAirports("WorldPlaneInc");
		Airports airports = new Airports();
		airports.addAll(xmlAirport);
		String xmlFlights = resSys.getFlights("WorldPlaneInc", "BOS", "2023_05_10");
		Flights flights = new Flights();
		flights.addAll(xmlFlights);
		
		//Demonstrate some functionality
		System.out.println(airports.get(0).toString()); //we can print out a list of airports formatted nicely
		System.out.println(flights.get(0).toString()); //we can print out a list of flights formatted nicely
		System.out.println(airports.containsCode("bos")); //we can find whether the list has a code (is valid)
		System.out.println(flights.toString()); //we can print out the airports from a server list of flights
	}
}
