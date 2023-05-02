package CS509.client.driver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import CS509.client.airport.TimeConversion;
import CS509.client.flight.Flight;

class driver_test {
	public Flight f1 = new Flight("777",
            "606",
            "20836",
            "HNL",
            "2023 May 26 01:25 GMT",
            "SAT",
            "2023 May 26 11:31 GMT",
            "$580.61",
            39,
            "$114.67",
            197);
	ArrayList<Flight> res = new ArrayList<Flight>(Arrays.asList(f1));
	
	/**
	 * JUnit test method for testing the convertToTimeZone() method of the TimeConversion class.
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	@Test
	void Localtimetest() throws IOException {
		TimeConversion converter = new TimeConversion("timezone.csv");
		String localtime = converter.convertToTimeZone(f1.getmTimeDepart(),f1.getmCodeDepart());
		String localtime2 = converter.convertToTimeZone(f1.getmTimeArrival(),f1.getmCodeArrival());
		String actuallocaltime = "2023 May 25 15:25 HST";
		String actuallocaltime2 = "2023 May 26 06:31 CDT";
		assertEquals(actuallocaltime,localtime);
		assertEquals(actuallocaltime2,localtime2);
	}
	
	/**
	 * JUnit test method for testing the reserveFlight() method of the reservationBuilder class.
	 */
	@Test
	void reservationtest() {
		// Create a reservationBuilder object
		reservationBuilder rb = new reservationBuilder();
		// Test whether a flight reservation is created successfully
		assertEquals(true,rb.reserveFlight(res, "Coach"));
		
	}

}
