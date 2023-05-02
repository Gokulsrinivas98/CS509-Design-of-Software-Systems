package CS509.client.driver;
import CS509.client.flight.*;
import CS509.client.util.Sp;
import CS509.client.airport.TimeConversion;
import CS509.client.dao.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
/**
 * This class provides an interface for reservation building.
 * @author Joe
 * @author Gokul
 *
 */
public class reservationBuilder {
	
	public enum flDirection{
		INBOUND,
		OUTBOUND;
	}
	
	String team = Sp.TICKET_AGENCY;//OOF check this constant.
	
	List<Flights> onewayFlights = new ArrayList<Flights>();
	List<Flights> DepartureFlights = new ArrayList<Flights>();
	List<Flights> ArrivalFlights = new ArrayList<Flights>();
	
	Trips outboundTrips = new Trips();
	Trips inboundTrips = new Trips();
	
	Trip outboundTrip;
	Trip inboundTrip;
	
	ArrayList<Date> outboundDates = new ArrayList<Date>();
	ArrayList<Date> inboundDates = new ArrayList<Date>();
	
	//We should have proper data types here...
	String departureCode = new String();
	String arrivalCode = new String();
	String departureDate = new String();
	String arrivalDate = new String();
	String tripType = new String();
	String seatClass = new String();
	Boolean coach = false;
	Boolean firstClass = false;
	Boolean roundTrip = false;
	Boolean oneWay = false;
	int seats = 1; //does the professor ask for multiple seat reservation ability?
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	/**
	 * Searches flights 
	 * @throws ParseException
	 */
	public void searchFlights() throws ParseException {
		//Have to add handling of MULTIPLE departure dates (windows)

//		
				DepartureFlights.addAll(FlightSearch.searchFlightsWithNoStop(team,departureCode,arrivalCode,departureDate,seats, coach));
				DepartureFlights.addAll(FlightSearch.searchFlightsWithOneStop(team,departureCode,arrivalCode,departureDate,seats, coach));
				DepartureFlights.addAll(FlightSearch.searchFlightsWithTwoStop(team,departureCode,arrivalCode,departureDate,seats, coach));
				
				if(roundTrip) {
					ArrivalFlights.addAll(FlightSearch.searchFlightsWithNoStop(team,arrivalCode,departureCode,arrivalDate,seats, coach));
					ArrivalFlights.addAll(FlightSearch.searchFlightsWithOneStop(team,arrivalCode,departureCode,arrivalDate,seats, coach));
					ArrivalFlights.addAll(FlightSearch.searchFlightsWithTwoStop(team,arrivalCode,departureCode,arrivalDate,seats, coach));
//					System.out.println(ArrivalFlights.size());
				}
			}
	
	/**
	 * Generates trips for the reservation based on the selected flights.
	 * The method adds all the departure flights to the outboundTrips list
	 * and all the arrival flights to the inboundTrips list if roundTrip is true.
	 */
	public void makeTrips() {
		try {
			this.outboundTrips.addAll(this.DepartureFlights);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(this.roundTrip) {
			this.inboundTrips.addAll(this.ArrivalFlights);
		}
	}
	
	/**
	 * Calls displayAll method to display the Departure Flights information held within a Trips object.
	 */
	public void showDepartureFlights() {
		Trip.seatType seatType;
		
		if(this.coach) {
			seatType = Trip.seatType.COACH;
		}
		else {
			seatType = Trip.seatType.FIRST;
		}
		outboundTrips.displayAll(seatType);
	}
	
	/**
	 * Calls displayAll method to display the Arrival Flights information held within a Trips object.
	 */
	public void showArrivalFlights() {
		Trip.seatType seatType;
		
		if(this.coach) {
			seatType = Trip.seatType.COACH;
		}
		else {
			seatType = Trip.seatType.FIRST;
		}
		inboundTrips.displayAll(seatType);
	}
	
	/**
	 * Sets the selected outbound trip based on the given index.
	 * @param index the index of the outbound trip to be selected
	 */
	public void selectDeparture(Integer index) {
		this.outboundTrip = outboundTrips.get(index);
	}
	
	/**
	 * Sets the selected inbound trip based on the given index.
	 * @param index the index of the inbound trip to be selected
	 */
	public void selectArrival(Integer index) {
		this.inboundTrip = inboundTrips.get(index);
	}
	
	/**
	 * Sets the departure code of the flight.
	 * @param departureCode the departure code of the flight.
	 */
	public void setDepartureCode(String departureCode) {
		this.departureCode = departureCode;
	}
	
	/**
	 * Gets the departure code of the flight.
	 */
	public String getDepartureCode() {
		return (this.departureCode);
	}
	
	/**
	 * Sets the arrival code of the flight.
	 * @param arrivalCode the arrival code of the flight.
	 */
	public void setArrivalCode(String arrivalCode) {
		this.arrivalCode = arrivalCode;
	}
	
	/**
	 * Sets the departure date of the flight.
	 * @param departureDate the departure date of the flight in the format "yyyy MMM dd HH:mm z".
	 */
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	
	/**
	 * Sets the arrival date of the flight.
	 * @param arrivalDate the arrival date of the flight in the format "yyyy MMM dd HH:mm z".
	 */
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	
	/**
	 * Sets the trip type of the flight.
	 * @param tripType the trip type of the flight
	 */
	public void setTripType(String tripType) {
		this.tripType = tripType;
		if (tripType.equalsIgnoreCase("oneway")) {
			this.roundTrip = false;
			this.oneWay = true;
		} else if(tripType.equalsIgnoreCase("round")) {
			this.oneWay = false;
			this.roundTrip = true;
		}
		else {
			System.out.println("Caught Error in tripType.");
			this.oneWay = false;
			this.roundTrip = false;
		}
	}
	
	/**
	 * Sets the seat class of the flight.
	 * @param seatClass
	 */
	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
		if (seatClass.equalsIgnoreCase("firstclass")) {
			this.coach = false;
			this.firstClass = true;
		} else if(seatClass.equalsIgnoreCase("coach")) {
			this.firstClass = false;
			this.coach = true;
		}
		else {
			System.out.println("Caught Error in Seat Class.");
			this.firstClass = false;
			this.coach = false;
		}
	}
	
	/**
	 * Clears all the reservations made by the user by removing all the departure flights,
	 * arrival flights, outbound trips, and inbound trips from the corresponding lists.
	 */
	public void flushReservation() {
		this.DepartureFlights.removeAll(DepartureFlights);
		this.ArrivalFlights.removeAll(ArrivalFlights);
		this.outboundTrips.removeAll(outboundTrips);
		this.inboundTrips.removeAll(inboundTrips);
	}
	
	/**
	 * Displays the pending reservation details along with the total cost of the reservation.
	 * This method first determines the seat type based on whether the reservation is for coach or first class.
	 * It then displays the outbound flight(s) followed by the inbound flight(s) if the reservation is round trip.
	 * The total cost of the reservation is calculated and displayed at the end.
	 */
	public void displayReservation() {
		Trip.seatType seatType;
		double reservationCost = 0d;
		
		if(this.coach) {
			seatType = Trip.seatType.COACH;
		}
		else {
			seatType = Trip.seatType.FIRST;
		}
		
		System.out.println("********************************** PENDING RESERVATION *************************************");
		System.out.println("********************************************************************************************");
		if(this.outboundTrips.size() > 1) {
		System.out.println("**********************************   OUTBOUND FLIGHTS  *************************************");
		}else {
		System.out.println("**********************************   OUTBOUND FLIGHT   *************************************");	
		
		}
		
		this.outboundTrip.displayTrip(seatType);
		reservationCost += this.outboundTrip.getPrice(seatType);
		
		if(this.roundTrip) {
			if(this.inboundTrips.size() > 1) {
				System.out.println("**********************************   INBOUND FLIGHTS   *************************************");
				}else {
				System.out.println("**********************************   INBOUND FLIGHT    *************************************");	
				}
			this.inboundTrip.displayTrip(seatType);
			reservationCost += this.inboundTrip.getPrice(seatType);
		}
		System.out.println("******************************* PENDING RESERVATION ABOVE ***********************************");
		System.out.println("******************************* TOTAL COST: $" + df.format(reservationCost)+" ***************************************");
		System.out.println("*********************************************************************************************");
	}

	/**
	 * Reserves the flights in the outbound trip and if round trip is true, reserves flights in inbound trip
	 * @return true if reservation successful for both trips, false otherwise
	 */
	public boolean reserve() {
		boolean complete = false;
		boolean completereturn=true;
		Flights flights1 = this.outboundTrip.getTripFlights();
		if(reserveFlight(flights1, this.seatClass)) {
			complete = true;
		}
		
		if(this.roundTrip) {
			//book flights in inbound
			completereturn = false;
			Flights flights2 = this.inboundTrip.getTripFlights();
			if(reserveFlight(flights2, this.seatClass)) {
				completereturn = true;
			}
		
		}
		return (complete && completereturn);
	}
	
	/**
	 *Sorts the list of inbound or outbound trips based on price.
	 * @param fl Direction of flight (INBOUND/OUTBOUND)
	 */
	public void sortByPrice(flDirection fl) {
		Trip.seatType seatType;
		if(this.coach) {
			seatType = Trip.seatType.COACH;
		}
		else {
			seatType = Trip.seatType.FIRST;
		}
		
		Comparator<Trip> compareByPrice = new Comparator<Trip>(){
			@Override
			public int compare(Trip t1, Trip t2) {
				return t1.getPrice(seatType).compareTo(t2.getPrice(seatType));
			}
		};
		
		if(fl == flDirection.INBOUND) {
			inboundTrips.sort(compareByPrice);
		}else if (fl == flDirection.OUTBOUND) {
			outboundTrips.sort(compareByPrice);
		}
		else {
			System.out.println("Not sorted oof");
		}

	}

	/**
	 * Sorts the list of inbound or outbound trips based on departure date.
	 * @param fl Direction of flight (INBOUND/OUTBOUND)
	 */
	public void sortByDepartureDate(flDirection fl) {
		Comparator<Trip> compareByDepartureDate = new Comparator<Trip>(){
			@Override
			public int compare(Trip t1, Trip t2) {
				return t1.getDepartureDate().compareTo(t2.getDepartureDate());
			}
		};
		
		if(fl == flDirection.INBOUND) {
			inboundTrips.sort(compareByDepartureDate);
		}else if (fl == flDirection.OUTBOUND) {
			outboundTrips.sort(compareByDepartureDate);
		}
		else {
			System.out.println("Not sorted oof");
		}
		
	}
	
	/**
	 * Sorts the list of inbound or outbound trips based on arrival date.
	 * @param fl Direction of flight (INBOUND/OUTBOUND)
	 */
	public void sortByArrivalDate(flDirection fl) {
		Comparator<Trip> compareByArrivalDate = new Comparator<Trip>(){
			@Override
			public int compare(Trip t1, Trip t2) {
				return t1.getArrivalDate().compareTo(t2.getArrivalDate());
			}
		};
		
		if(fl == flDirection.INBOUND) {
			inboundTrips.sort(compareByArrivalDate);
		}else if (fl == flDirection.OUTBOUND) {
			outboundTrips.sort(compareByArrivalDate);
		}
		else {
			System.out.println("Not sorted oof");
		}
	}
	
	/**
	 * Sorts the list of inbound or outbound trips based on trip length.
	 * @param fl Direction of flight (INBOUND/OUTBOUND)
	 */
	public void sortByTripLength(flDirection fl) {
		Comparator<Trip> compareByTripLength = new Comparator<Trip>(){
			@Override
			public int compare(Trip t1, Trip t2) {
				return t1.getTripLength().compareTo(t2.getTripLength());
			}
		};
		
		if(fl == flDirection.INBOUND) {
			inboundTrips.sort(compareByTripLength);
		}else if (fl == flDirection.OUTBOUND) {
			outboundTrips.sort(compareByTripLength);
		}
		else {
			System.out.println("Not sorted oof");
		}
	}
	
	/**
	 * Checks if the the given date is between the Start and End dates of the database
	 * @param userIn User input date.
	 * @return True if the given date is between the database start and end dates; Else false.
	 */
	public boolean validDate(String userIn) {
    	LocalDateTime start = LocalDateTime.parse(Sp.START,DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm"));
    	LocalDateTime end = LocalDateTime.parse(Sp.END,DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm"));
    	LocalDateTime dateC =LocalDateTime.parse(TimeConversion.startofDay(userIn),DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm"));
    	if (dateC.isAfter(start) && dateC.isBefore(end)) {
    		return true;
    	}
    	return false;
	}
	
	/**
	 * Creates the XML to be used to reserve flights
	 * @param flightList
	 * @param seatClass
	 * @return xml in the following format
	 * <pre>{@code
	 * <Flights>
         <Flight number=DDDDD seating=SEAT_TYPE/>
	        <Flight number=DDDDD seating=SEAT_TYPE/>
     </Flights>
	 * }</pre>
	 */
	public String getXML(ArrayList<Flight> flightList, String seatClass) {
		 
     
        String head = "<Flights>";
        String end = "</Flights>";
        String middle = "";
        String SEAT_TYPE = "";

        if(seatClass.equalsIgnoreCase("coach")) {
            SEAT_TYPE = "Coach";
        } else if (seatClass.equalsIgnoreCase("firstClass")) {
            SEAT_TYPE = "FirstClass";
        }

        for(Flight f:flightList){
            String number = f.getmNumber();
            middle += "<Flight number=\"" + number + "\" seating=\"" + SEAT_TYPE + "\"/>";
        }
        String res = head + middle + end;
        return res;
    }
	
	/**
	 * Reserves multiple flights simultaneously
	 * @param flightList
	 * @param seatClass
	 * @return true if reserved successfully
	 */
	@SuppressWarnings("unused")
	public boolean reserveFlight(ArrayList<Flight> flightList, String seatClass) {
        boolean isLocked = false;
        boolean isReserved = false;
        boolean isUnlocked = true;
        String xmlFlights;

        // lock server
        isLocked = ServerInterface.lock(Sp.TICKET_AGENCY);

        // if server locked
        // update server
        if (isLocked) {
            isUnlocked = false;
            xmlFlights = getXML(flightList, seatClass);
            isReserved = ServerInterface.buyTickets(Sp.TICKET_AGENCY, xmlFlights);

            // if reservation is successful
            // unlock server
            if (isReserved) {
                isUnlocked = ServerInterface.unlock(Sp.TICKET_AGENCY);
               
            }
            // if not, also unlock, and ask to try again.
            else {
                isUnlocked = ServerInterface.unlock(Sp.TICKET_AGENCY);
                System.out.println("Please try again.");
                
            }

        }
        return isReserved;
    }
}
