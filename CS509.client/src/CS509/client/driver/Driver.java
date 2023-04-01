package CS509.client.driver;

import CS509.client.dao.ServerInterface;
import CS509.client.flight.*;
import CS509.client.airport.*;

/**
 * @author blake
 *
 */
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerInterface resSys = new ServerInterface();
		
		
		
		// Try to get a list of airports
		String xmlAirport = resSys.getAirports("WorldPlaneInc");
		// Create the aggregate airports object.
		Airports airports = new Airports();
		airports.addAll(xmlAirport);
		
		System.out.println(airports.get(0).toString());

		// Get a sample list of flights from server
		String xmlFlights = resSys.getFlights("WorldPlaneInc", "BOS", "2023_05_10");
		
		// Create the aggregate flights object.
		Flights flights = new Flights();
		flights.addAll(xmlFlights);
		
		System.out.println(flights.get(0).toString());
		System.out.println(flights.size());
		
		//try to reserve a coach seat on one of the flights
//		Flight flight = flights.get(0);
//		String flightNumber = flight.getmNumber();
//		int seatsReservedStart = flight.getmSeatsCoach();
		
//		String xmlReservation = "<Flights>"
//				+ "<Flight number=\"" + flightNumber + " seating=\"Coach\" />"
//				+ "</Flights>";
		
		
		// Try to lock the database, purchase ticket and unlock database
//		resSys.lock("WorldPlaneInc");
//		resSys.buyTickets("WorldPlaneInc", xmlReservation);
//		resSys.unlock("WorldPlaneInc");
		
		// Verify the operation worked
		xmlFlights = resSys.getFlights("WorldPlaneInc", "BOS", "2023_05_10");
		//System.out.println(xmlFlights);
		flights.clear();
		flights.addAll(xmlFlights);
		
		// Find the flight number just updated
//		int seatsReservedEnd = seatsReservedStart;
//		for (Flight f : flights) {
//			String tmpFlightNumber = f.getmNumber();
//			if (tmpFlightNumber.equals(flightNumber)) {
//				seatsReservedEnd = f.getmSeatsCoach();
//				break;
//			}
//		}
//		if (seatsReservedEnd == (seatsReservedStart + 1)) {
//			System.out.println("Seat Reserved Successfully");
//		} else {
//			System.out.println("Reservation Failed");
//		}
		
	}

}
