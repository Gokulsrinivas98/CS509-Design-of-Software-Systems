package CS509.client.driver;

import CS509.client.dao.ServerInterface;
import CS509.client.flight.*;
import CS509.client.airport.*;
import java.util.Scanner;

/**
 * @author TEAM C
 *
 */

public class PrototypeDriver {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerInterface resSys = new ServerInterface();
		boolean airportOK = false;
		String departure = new String();
		
		Scanner s = new Scanner(System.in);
		String team = "TeamC";
		System.out.println("Welcome to THE PROTOTYPE!\n");
		
		String xmlAirport = resSys.getAirports(team);
		Airports airports = new Airports();
		airports.addAll(xmlAirport);
		
		while(!airportOK) {
		System.out.println("Please enter the departure airport 3-character code:");	
	    departure = s.nextLine().toUpperCase();
			if(!airports.containsCode(departure)) {
				System.out.println("Invalid departure code!\nHere are the acceptable codes and their airports:\n");
				System.out.println(airports.toString());
			}
			else {
				airportOK = true;
				System.out.println("Acceptable Airport Code!\n");
			}
		}
		
		System.out.println("Choose a departure date:\n" +
		"Please format as (YYYY_MM_DD) between 2023_05_06 and 2023_05_31:");
		String time = s.nextLine();
		s.close();
		

		String xmlFlights = resSys.getFlights(team, departure, time);
		
		Flights flights = new Flights();
		flights.addAll(xmlFlights);
		
		System.out.println("Found " + flights.size() + " flights departing " + departure + " on " + time);
		System.out.println(flights.toString());
	}
}
	