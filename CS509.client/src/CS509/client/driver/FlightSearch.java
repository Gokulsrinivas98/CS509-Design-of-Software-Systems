package CS509.client.driver;
import java.util.ArrayList;

import java.util.List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import CS509.client.flight.*;
import CS509.client.airplane.*;
import CS509.client.airport.*;
import CS509.client.dao.ServerInterface; 
import CS509.client.util.Sp;
/**
 * @author gokul
 *
 */

/**
 * Searches and return the list of possible flights between the given airports without any layover
 * 
 * Returns the string of the possible flights
 */
public class FlightSearch{

	final static Airplanes a = getAirplanes();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm z");	
	
	/**
	 * Searches and return the list of possible flights between the given airports with No layover
	 *
	 * Returns a List of Flights
	 * @param team The ticket agency
	 * @param departure Departure airport code
	 * @param arrival Arrival airport code
	 * @param date Departure Date
	 * @param seats Number of seats
	 * @param coach True if coach, false if FirstClass
	 * @return List of flights with no stops.
	 */
	public static List<Flights> searchFlightsWithNoStop(String team,String departure,String arrival,String date, int seats,boolean coach){

		List<Flights> res=new ArrayList<Flights>();
		
		Flights xmlflights = getFlights(team,departure,arrival,date,true);
		xmlflights = filterByArrival(arrival,xmlflights);
		for (Flight f1: xmlflights) {
			if(isSeatAvailable(f1,seats,coach)) {
				Flights flight=new Flights();
				if (validDate(f1,date,departure)) {
				flight.add(f1);
				res.add(flight);
				}}
		}
//		System.out.println("Flights with no layover:"+res.size());	
		return res;
		
	}
	
	/**
	 * 
	 * Searches and return the list of possible flights between the given airports with one layover
	 * The layover threshold is set between 30 minutes and 2 hours
	 * Returns a List of Flights
	 *
	 * @param team The ticket agency
	 * @param departure Departure airport code
	 * @param arrival Arrival airport code
	 * @param date Departure Date
	 * @param seats Number of seats
	 * @param coach True if coach, false if FirstClass
	 * @return List of flights with one stops.
	 * @throws ParseException
	 */
	public static List<Flights> searchFlightsWithOneStop(String team,String departure,String arrival,String date,int seats,boolean coach) throws ParseException{
	
		List<Flights> res=new ArrayList<Flights>();
		
		Flights flights1 = getFlights(team,departure,arrival,date,true);
		Flights flights2 = getFlights(team,departure,arrival,date,false);//false means search by departure
			
		for(Flight f1 : flights1){			
			for(Flight f2 :flights2){
				if(f1.getmCodeArrival().equals(f2.getmCodeDepart()) && validDate(f1,date,departure)){										
						long layover = (sdf.parse(f2.getmTimeDepart()).getTime() - sdf.parse(f1.getmTimeArrival()).getTime())/ (60 * 1000);
//						The layover threshold is set between 30 minutes and 6 hours.
						if (layover >= Sp.MIN_LAYOVER && layover <= Sp.MAX_TOTAL_LAYOVER && isSeatAvailable(f1,seats,coach) && isSeatAvailable(f2,seats,coach)) {
							
							Flights flight=new Flights();
							flight.add(f1);
							flight.add(f2);
							res.add(flight);
							}
				}
			}
		}
//		System.out.println("Flights with one layover:"+res.size());		
		return res;		
	}
	
	/**
	 * Searches and return the list of possible flights between the given airports with two layovers on the same day
	 * The individual layover threshold is set between 30 minutes and 3 hours
	 * The total layover will be less than 6 hours 
	 * Returns a List of Flights
	 * @param team The ticket agency
	 * @param departure Departure airport code
	 * @param arrival Arrival airport code
	 * @param date Departure Date
	 * @param seats Number of seats
	 * @param coach True if coach, false if FirstClass
	 * @return List of flights with two stops.
	 * @throws ParseException
	 */
	public static List<Flights> searchFlightsWithTwoStop(String team,String departure,String arrival,String date,int seats,boolean coach) throws ParseException{
		List<Flights> res=new ArrayList<Flights>();
		Flights flights1 = getFlights(team,departure,arrival,date,true);
		Flights flights3 = getFlights(team,departure,arrival,date,false);//false means search by arrival
		for(Flight f1:flights1) {
			if(validDate(f1,date,departure)) {
			//Since it is set to true, arrival doesnt matter thus set to Dummy
			Flights flights2 = getFlights(team, f1.getmCodeArrival(), "Dummy", date, true);
			for(Flight f2 : flights2) {				
				for(Flight f3 :flights3){
					if(f2.getmCodeArrival().equals(f3.getmCodeDepart()) && !f1.getmCodeArrival().equals(f3.getmCodeArrival())){									
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm z");
							long layover1 = (sdf.parse(f2.getmTimeDepart()).getTime() - sdf.parse(f1.getmTimeArrival()).getTime())/ (60 * 1000); //Layover 1
							long layover2 = (sdf.parse(f3.getmTimeDepart()).getTime() - sdf.parse(f2.getmTimeArrival()).getTime())/ (60 * 1000);// Layover 2
							long total_layover = layover1+layover2;
//							The layover threshold is set between 30 minutes and 6 hours.
							if (layover1 >= Sp.MIN_LAYOVER && layover1 <= Sp.MAX_LAYOVER && 
								layover2 >= Sp.MIN_LAYOVER && layover2 <= Sp.MAX_LAYOVER && 
								total_layover <= Sp.MAX_TOTAL_LAYOVER  && 
								isSeatAvailable(f1,seats,coach) && isSeatAvailable(f2,seats,coach)&& isSeatAvailable(f3,seats,coach))
							{								
								Flights flight=new Flights();
								flight.add(f1);
								flight.add(f2);
								flight.add(f3);
								res.add(flight);
								}
					}
			}
		}
	}
}

//		System.out.println("Flights with two layover:"+res.size());		
		return res;
		
	}
	
	/**
	 * Filters the list of Flights based on the arrival of the flight
	 * @param arr Arrival Code
	 * @param flights List of Flights
	 * @return List of flights with the arrival airports same as the given arrival code
	 */
	private static Flights filterByArrival(String arr, Flights flights){
		Flights res= new Flights();
		for(Flight flight: flights){
			if(flight.getmCodeArrival().equals(arr)){
				res.add(flight);
			}else if (flight.getmCodeDepart().equals(arr)) {
//				System.out.println("We admire your love for your home airport, but unfortunately,\n"+ "it can't be both the departure and arrival. Please make a different selection.");
				break;
			}
		}
		return res;
	}
	
	/**
	 * Checks if the seat is available for the given flight
	 * @param f flight in which seat has to be checked
	 * @param seats number of seats
	 * @param coach true if coach, false if FirstClass
	 * @return
	 */
	private static boolean isSeatAvailable(Flight f,int seats,boolean coach) {
		Airplane a1 = a.stream().filter(ap -> ap.model().equals(f.getmAirplane())).findFirst().orElse(null);
		if (coach){
			int fseat =a1.coachSeats() - f.getmSeatsCoach();
			if (fseat >= seats){
				return true;
			}
		}
		else if (!coach) {
			int fseat = a1.firstClassSeats() - f.getmSeatsFirstclass();
			if (fseat >= seats){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if given date is valid
	 * @param f Flight of the date to be checked
	 * @param date the date
	 * @param airportCode code of airport
	 * @return True if the departTime is in the 24 hours of given date (yyyy_MM_dd). 
	 */
	private static boolean validDate(Flight f, String date, String airportCode) {
		String departTime = f.getmTimeDepart();
		return TimeConversion.dateBetweentwoDates(date,departTime,airportCode);
		
	}
	
	/**
	 * Gets a date and return the date of next day
	 * @param date
	 * @return date of next day
	 */
	private static String nextDate(String date) {
		return TimeConversion.getNextDate(date);
	}
	
	/**
	 * Gets flight
	 * @param team The ticket agency
	 * @param departure Departure airport code
	 * @param arrival Arrival airport code
	 * @param date Departure Date
	 * @param seats Number of seats
	 * @param dep True if search by departure, false if search by arrival
	 * @return List of all flights
	 */
	private static Flights getFlights(String team,String departure,String arrival,String time,boolean dep) {
		ServerInterface resSys = new ServerInterface();
		String nextday = nextDate(time);
		Flights flights1 = resSys.getFlights(team,departure,arrival,time,dep); //true means search by departure
		Flights flnextday = resSys.getFlights(team,departure,arrival,nextday,dep);
		flights1.addAll(flnextday);
		return flights1;
	}

	/**
	 * Finds all the airplanes
	 * @return List of all airplanes
	 */
	private static Airplanes getAirplanes() {
		Airplanes airplanes = new Airplanes();
		ServerInterface resSys = new ServerInterface();
		String xmlAirplane = resSys.getAirplanes(Sp.TICKET_AGENCY);
		airplanes.addAll(xmlAirplane);
		return airplanes;
	}	
}
