package CS509.client.driver;

import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import CS509.client.flight.Flights;
/**
 * Trips is a child class of ArrayList of the type Trip. 
 * Trips contains the functionality necessary to build a Trips object, and print the data held within it. 		
 * @author Team C
 */

public class Trips extends ArrayList<Trip>{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Adds a list of Flights to the current collection of Trips, creates a new Trip object for each valid Flights object.
	 * @param LoF a List of Flights to add to the current collection
	 * @return true if the collection is updated with at least one valid Trip object, false otherwise
	 */
	public boolean addAll(List<Flights> LoF) {
		
		boolean collectionUpdated = false;
		
		for (int i = 0; i < LoF.size(); i++) {
			Trip trip = null;
			
			try {
				trip = new Trip(LoF.get(i));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if (trip.isValid()) {
				this.add(trip);
				collectionUpdated = true;
			}
		}
		
		return collectionUpdated;
	}
	
	/**
	*A Comparator used to sort Trips by price in ascending order. Compares Trips based on their coach class price.
	*/
	Comparator<Trip> compareByPrice = new Comparator<Trip>(){
		@Override
		public int compare(Trip t1, Trip t2) {
			return t1.getPrice(Trip.seatType.COACH).compareTo(t2.getPrice(Trip.seatType.COACH));
		}
	};
	
	/**
	 * displayAll is the method used to display the information held within a Trips object.
	 * @param seatType is the enum seat type stored in Trip.
	 * @return True to confirm that the method has been executed.
	 */
	public boolean displayAll(Trip.seatType seatType) {
		int tripListSize = this.size();
		
		if(tripListSize > 0) {
			for (int i = 0; i < tripListSize; i++) {
			    System.out.println("["+i+"]"+" *****************************************************************************************");
			    this.get(i).displayTrip(seatType);
			}
			System.out.println("********************************************************************************************");
		}
		else {
			System.out.println("No Flights in this List");
		}
		return true;
	}

}
