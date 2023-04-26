package CS509.client.driver;
import java.util.ArrayList;

import java.util.List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
	 * @param team
	 * @param departure
	 * @param arrival
	 * @param time
	 * @param seats
	 * @param coach
	 * @return List of flights with no stops.
	 */
	
	public static List<Flights> searchFlightsWithNoStop(String team,String departure,String arrival,String time, int seats,boolean coach){

		List<Flights> res=new ArrayList<Flights>();
		
		Flights xmlflights = getFlights(team,departure,arrival,time,true);
		xmlflights = filterByArrival(arrival,xmlflights);
		for (Flight f1: xmlflights) {
			if(isSeatAvailable(f1,seats,coach)) {
				Flights flight=new Flights();
				if (validDate(f1,time,departure)) {
				flight.add(f1);
				res.add(flight);
				}}
		}
		System.out.println("res size:"+res.size());	
		return res;
		
	}
	
	/**
	 * 
	 * Searches and return the list of possible flights between the given airports with one layover
	 * The layover threshold is set between 30 minutes and 2 hours
	 * Returns a List of Flights
	 *
	 * @param team
	 * @param departure
	 * @param arrival
	 * @param time
	 * @param seats
	 * @param coach
	 * @return List of flights with one stops.
	 * @throws ParseException
	 */
	public static List<Flights> searchFlightsWithOneStop(String team,String departure,String arrival,String time,int seats,boolean coach) throws ParseException{
	
		List<Flights> res=new ArrayList<Flights>();
		
		String nextday = nextDate(time);
		Flights flights1 = getFlights(team,departure,arrival,time,true);
		Flights flights2 = getFlights(team,departure,arrival,time,false);//false means search by departure
			
		for(Flight f1 : flights1){			
			for(Flight f2 :flights2){
				if(f1.getmCodeArrival().equals(f2.getmCodeDepart()) && validDate(f1,time,departure)){										
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
		System.out.println("res size:"+res.size());		
		return res;		
	}
	
	/**
	 * Searches and return the list of possible flights between the given airports with two layovers on the same day
	 * The individual layover threshold is set between 30 minutes and 3 hours
	 * The total layover will be less than 6 hours 
	 * Returns a List of Flights
	 * @param team
	 * @param departure
	 * @param arrival
	 * @param time
	 * @param seats
	 * @param coach
	 * @return List of flights with two stops.
	 * @throws ParseException
	 */

	public static List<Flights> searchFlightsWithTwoStop(String team,String departure,String arrival,String time,int seats,boolean coach) throws ParseException{
		List<Flights> res=new ArrayList<Flights>();
		Flights flights1 = getFlights(team,departure,arrival,time,true);
		Flights flights3 = getFlights(team,departure,arrival,time,false);//false means search by arrival
		for(Flight f1:flights1) {
			if(validDate(f1,time,departure)) {
			//Since it is set to true, arrival doesnt matter thus set to Dummy
			Flights flights2 = getFlights(team, f1.getmCodeArrival(), "Dummy", time, true);
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

		System.out.println("res size:"+res.size());		
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
				System.out.println("We admire your love for your home airport, but unfortunately,\n"
						+ "it can't be both the departure and arrival. Please make a different selection.");
				break;
			}
		}
		return res;
	}
	
/**
 * 
 * @param f flight
 * @param seats
 * @param coach
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
	 * 
	 * @param f
	 * @param date
	 * @param airportCode
	 * @return
	 */
	private static boolean validDate(Flight f, String date, String airportCode) {
		String departTime = f.getmTimeDepart();
		return TimeConversion.dateBetweentwoDates(date,departTime,airportCode);
		
	}
	/**
	 * 
	 * @param date
	 * @return
	 */
	private static String nextDate(String date) {
		return TimeConversion.getNextDate(date);
	}
	/**
	 * 
	 * @param team
	 * @param departure
	 * @param arrival
	 * @param time
	 * @param dep
	 * @return
	 */
	private static Flights getFlights(String team,String departure,String arrival,String time,boolean dep) {
		ServerInterface resSys = new ServerInterface();
		String nextday = nextDate(time);
		String nextnextday = nextDate(nextday);
		Flights flights1 = resSys.getFlights(team,departure,arrival,time,dep); //true means search by departure
		Flights flnextday = resSys.getFlights(team,departure,arrival,nextday,dep);
		flights1.addAll(flnextday);
		Flights flnextnextday = resSys.getFlights(team,departure,arrival,nextnextday,dep);
		flights1.addAll(flnextnextday);
		return flights1;
	}

	/**
	 * 
	 * @return 
	 */
	private static Airplanes getAirplanes() {
		Airplanes airplanes = new Airplanes();
		ServerInterface resSys = new ServerInterface();
		String xmlAirplane = resSys.getAirplanes(Sp.Database);
		airplanes.addAll(xmlAirplane);
		return airplanes;
	}	
}
