package CS509.client.flight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Sort extends ArrayList<Flight> {
	private static final long serialVersionUID = 1L;
	
//	
//	public void sortbycoachprice(Flights flight){
//		 Comparator<Flight> comparator = new Comparator<Flight>(){
//			     public int compare(Flight o1, Flight o2) {
//		 String str1 = o1.getSeating().getCoachPrice().substring(1, 6);
//		 String str2 = o1.getSeating().getCoachPrice().substring(1, 6);
//		 float flo1 = Float.parseFloat(str1);
//		 float flo2 = Float.parseFloat(str2);
//		 if(flo1 == flo2)
//		 {
//		 return 0;
//		 } 
//		 else if(flo1 < flo2)
//		 {
//		 return -1;
//		 }
//		 else
//		 {
//		 return 1;
//		 }
//		 }
//		
//		 }; 
//	}
//
//	public void sortbyfirstclassprice(Flights flight){
//		 Comparator<Flight> comparator = new Comparator<Flight>(){
//			     public int compare(Flight o1, Flight o2) {
//		 String str1 = o1.getSeating().getFirstclassPrce().substring(1, 6);
//		 String str2 = o1.getSeating().getFirstclassPrce().substring(1, 6);
//		 float flo1 = Float.parseFloat(str1);
//		 float flo2 = Float.parseFloat(str2);
//		 if(flo1 == flo2)
//		 {
//		 return 0;
//		 } 
//		 else if(flo1 < flo2)
//		 {
//		 return -1;
//		 }
//		 else
//		 {
//		 return 1;
//		 }
//		 }
//		
//		 }; 
//	}
		 


	
	static public Flights filterByArrival(String arr, Flights flights){
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
//	@Override
//	public String toString() {
//		String s=null;
//		for(Flight f : this){
//			s+=f.toString()+" ; ";
//		}
//		return "Flights:"+s;
//	}
//	
	
	
	}
	

