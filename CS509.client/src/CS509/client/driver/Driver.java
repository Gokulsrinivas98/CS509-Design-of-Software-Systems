/**
 * 
 */

package CS509.client.driver;
//------------
import java.util.Scanner;
//------------
import CS509.client.dao.ServerInterface;
import CS509.client.flight.Flight;
import CS509.client.flight.Flights;
import CS509.client.airport.Airports;

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
		//------------
		Scanner s = new Scanner(System.in);
		String team = "TeamC";
		System.out.println("departAirportCode:");	
	    String departure = s.nextLine().toUpperCase();
		System.out.println("departTime (YYYY_MM_DD) B/W. 2023_05_06 and 2023_05_31:");
		String time = s.nextLine();
//		System.out.println("arrivalAiportCode:");
//		String arrival = s.nextLine();
		//------------
		// Try to get a list of airports
		String xmlAirport = resSys.getAirports(team);
		Airports airports = new Airports();
		airports.addAll(xmlAirport);

		//-----------
		String xmlFlights = resSys.getFlights(team, departure, time,true);
//		System.out.println(xmlFlights);
		//-----------
		// Create the aggregate flights
		Flights flights = new Flights();
		flights.addAll(xmlFlights);
		
		System.out.println(flights.toString());
		
	// 	//try to reserve a coach seat on one of the flights
	// 	Flight flight = flights.get(0);
	// 	String flightNumber = flight.getmNumber();
	// 	int seatsReservedStart = flight.getmSeatsCoach();
		
	// 	String xmlReservation = "<Flights>"
	// 			+ "<Flight number=\"" + flightNumber + " seating=\"Coach\" />"
	// 			+ "</Flights>";
		
		
	// 	// Try to lock the database, purchase ticket and unlock database
	// 	resSys.lock("WorldPlaneInc");
	// 	resSys.buyTickets("WorldPlaneInc", xmlReservation);
	// 	resSys.unlock("WorldPlaneInc");
		
	// 	// Verify the operation worked
	// 	xmlFlights = resSys.getFlights("WorldPlaneInc", "BOS", "2016_05_10");
	// 	// System.out.println(xmlFlights);
	// 	flights.clear();
	// 	flights.addAll(xmlFlights);
		
	// 	// Find the flight number just updated
	// 	int seatsReservedEnd = seatsReservedStart;
	// 	for (Flight f : flights) {
	// 		String tmpFlightNumber = f.getmNumber();
	// 		if (tmpFlightNumber.equals(flightNumber)) {
	// 			seatsReservedEnd = f.getmSeatsCoach();
	// 			break;
	// 		}
	// 	}
	// 	if (seatsReservedEnd == (seatsReservedStart + 1)) {
	// 		System.out.println("Seat Reserved Successfully");
	// 	} else {
	// 		System.out.println("Reservation Failed");
	// 	}
		
	}

}
