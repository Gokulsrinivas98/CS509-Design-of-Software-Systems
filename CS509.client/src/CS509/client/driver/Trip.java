package CS509.client.driver;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import CS509.client.flight.Flights;
/**
 * Trip is a representation of data pertinent to a flight reservation.
 * The Trip object stores the ticket price, the length, and the arrival and departure dates.
 * The trip is primarily composed though of a List of Flights (LoF) of class Flights.
 * @author Joe
 *
 */
public class Trip {
	public enum seatType{
		COACH,
		FIRST
	}
	
	private Flights LoF;
	private Double coachTicketPrice = 0d;
	private Double firstClassTicketPrice = 0d;
	private Long lengthOfTrip; //stored in ms!!
	private Long[] lengthOfLayovers;
	private Long[] lengthOfFlights;
	private Date departureDate;
	private Date arrivalDate;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm z");	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private static final DecimalFormat tf = new DecimalFormat("0");

	/**
	 * Basic constructor for a Trip. The only necessary parameter is a list of flights, with 1 or more flight objects in it.
	 * @param flights
	 * @throws ParseException
	 */
	public Trip(Flights flights) throws ParseException {
		LoF = flights;
		int numFlights = flights.size();
		
		departureDate = sdf.parse(flights.get(0).getmTimeDepart());
		arrivalDate = sdf.parse(flights.get(numFlights-1).getmTimeArrival());
		lengthOfTrip = arrivalDate.getTime() - departureDate.getTime();
		
		lengthOfFlights = new Long[numFlights];
		
		if(numFlights > 1) {
			lengthOfLayovers = new Long[numFlights-1];
		}
		
		for(int i = 0; i < numFlights; i++) {
			coachTicketPrice += Double.parseDouble(flights.get(i).getmPriceCoach().replace(",", "").replace("$", ""));
			firstClassTicketPrice += Double.parseDouble(flights.get(i).getmPriceFirstclass().replace(",", "").replace("$", ""));
			lengthOfFlights[i] = Long.parseLong(flights.get(i).getmFlightTime());
			if(i < numFlights - 1) {
				lengthOfLayovers[i] = sdf.parse(flights.get(i+1).getmTimeDepart()).getTime() - sdf.parse(flights.get(i).getmTimeArrival()).getTime();
			}		
		}
	}
	
	/**
	 * The method displayTrip with parameter seat (of the enum seatType) displays the data of the trip formatted neatly.
	 * This method is quite valuable and used heavily throughout the program.
	 * @param seat
	 */
	public void displayTrip(seatType seat) {
	    System.out.println(this.LoF.toString());
	    
	    double ticketPrice;
	    if(seat == Trip.seatType.COACH) {
	    	ticketPrice = this.coachTicketPrice;
	    }
	    else {
	    	ticketPrice = this.firstClassTicketPrice;
	    }
	    System.out.println("Total Ticket Price :$"+ df.format(ticketPrice));
	    
	    if(this.LoF.size() > 1) {
	    	long laydurationMinutes = 0;
	    	for(int i = 0; i < this.lengthOfLayovers.length; i++) {
	    		laydurationMinutes += this.lengthOfLayovers[i];
	    	}
	    	
	    	laydurationMinutes /= (1000*60);
	    	
		    long lminutes = laydurationMinutes % 60;
		    long lhours = 0;
		    if(laydurationMinutes > 60) {
			    lhours = laydurationMinutes / 60;
		    }
		    System.out.printf("Total Layover Time:" +tf.format(lhours)+" Hours "+ tf.format(lminutes)+" Minutes\n");
	    }
	    
	    long durationMinutes = this.lengthOfTrip/(1000*60); 
	    long fminutes = durationMinutes % 60;
	    long fhours = 0;
	    if(durationMinutes > 60) {
		    fhours = durationMinutes / 60;
	    }

	    System.out.printf("Total Trip Duration:" +tf.format(fhours)+" Hours "+ tf.format(fminutes)+" Minutes\n");
	}
	
	/**
	 * Oops
	 * @return true currently.
	 */
	public boolean isValid() {
		return true; //let's make this better
	}
	
	/**
	 * The method getPrice returns the price of a Trip, given the type of seating class.
	 * @param seat
	 * @return
	 */
	public Double getPrice(seatType seat) {
		if(seat == seatType.COACH) {
			return this.coachTicketPrice;
		}else {
			return this.firstClassTicketPrice;
		}
	}
	
	/**
	 * Simple getter. Utilized in comparator.
	 * @return arrivalDate
	 */
	public Date getArrivalDate() {
		return this.arrivalDate;
	}
	
	/**
	 * Simple getter. Utilized in comparator.
	 * @return departureDate
	 */
	public Date getDepartureDate() {
		return this.departureDate;
	}
	
	/**
	 * Simple getter. Utilized in comparator.
	 * @return lengthofTrip
	 */
	public Long getTripLength() {
		return this.lengthOfTrip;
	}
	
	/**
	 * Simple getter. Utilized to reserve flights within the trip.
	 * @return LoF (List of Flights) of class Flights
	 */
	public Flights getTripFlights() {
		return this.LoF;
	}

}
