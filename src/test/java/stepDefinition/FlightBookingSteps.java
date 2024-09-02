package stepDefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import flight.FlightSearchPage;
import flight.FlightSearchPage.FlightDuration;

import static org.testng.Assert.assertTrue;

import java.util.List;
//import static org.testng.Assert.assertNotNull;

public class FlightBookingSteps {
	private String startLocation;
	private String destinationLocation;
    private WebDriver driver;
    private FlightSearchPage flightSearchPage;
    private double cheapestPrice;
    private FlightSearchPage.FlightDuration longestFlight;
    

    @Given("I am on the Booking.com homepage")
    public void i_am_on_the_homepage() {
        driver = new ChromeDriver();
        driver.manage().window().maximize(); 
        driver.get("https://www.booking.com");
        flightSearchPage = new FlightSearchPage(driver);
        
        
    }
    
    @When("I enter the flight details from {string} to {string}")
    public void i_enter_the_flight_details(String fromLocation, String toLocation) {
        // Assign the locations
        startLocation = fromLocation;
        destinationLocation = toLocation;

        flightSearchPage.enterFromLocation(fromLocation);
        flightSearchPage.enterToLocation(toLocation);
        flightSearchPage.selectDates();
        flightSearchPage.searchFlights();
        
        // Retrieve cheapest price and longest flight details
        cheapestPrice = flightSearchPage.getCheapestFlightPrice();
        longestFlight = flightSearchPage.getLongestFlightDuration();
    }

//    @When("I enter the flight details from {string} to {string}")
//    public void i_enter_the_flight_details(String fromLocation, String toLocation) {
//        flightSearchPage.enterFromLocation(fromLocation);
//        flightSearchPage.enterToLocation(toLocation);
//        flightSearchPage.selectDates();
//        flightSearchPage.searchFlights();
//        cheapestPrice = flightSearchPage.getCheapestFlightPrice();
//        longestFlight = flightSearchPage.getLongestFlightDuration();
////        List<FlightDuration> topTwoFlights = flightSearchPage.getTopTwoLongestFlights();
////        if (!topTwoFlights.isEmpty()) {
////            longestFlight = topTwoFlights.get(0); // The longest flight
////            secondLongestFlight = topTwoFlights.size() > 1 ? topTwoFlights.get(1) : null; // The second longest flight, if it exists
////        }
//    }
//
//    @Then("I should see the cheapest flight and longest flight duration")
//    public void i_should_see_the_cheapest_flight_and_longest_flight_duration() {
//        System.out.println("Cheapest Flight Price: $" + cheapestPrice);
//        System.out.println("Longest Flight Duration: " + (longestFlight != null ? longestFlight.totalDurationMinutes + " minutes" : "Not found"));
//        System.out.println("time for flight1: "+flightSearchPage.getLongestFlightDuration());
//        System.out.println( formatDuration(longestFlight.outboundDurationMinutes));
//        System.out.println(  formatDuration(longestFlight.returnDurationMinutes));
//
//        // Example assertions (customize based on actual test criteria)
//        assertTrue(cheapestPrice > 0, "Cheapest flight price should be greater than 0");
////        assertNotNull(longestFlight, "Longest flight duration should be found");
//
//        driver.quit();
//    }
    
    @Then("I should see the cheapest flight and longest flight duration")
    public void i_should_see_the_cheapest_flight_and_longest_flight_duration() {
        // Print cheapest flight details
        System.out.println("**********************************************************************************");
        System.out.println("Cheapest Flight details:");
        System.out.println("\tFrom: " + startLocation);
        System.out.println("\tTo: " + destinationLocation);
        System.out.println("\tThe cheapest price is: $" + cheapestPrice);
        System.out.println("**********************************************************************************");

        // Get the longest flight duration
        FlightDuration longestFlight = flightSearchPage.getLongestFlightDuration();
        
        // Print longest flight duration details
        if (longestFlight != null) {
            System.out.println("The longest flight duration is: " + formatDuration(longestFlight.totalDurationMinutes));
            System.out.println("\t" + startLocation + " to " + destinationLocation + ": " + formatDuration(longestFlight.outboundDurationMinutes));
            System.out.println("\t" + destinationLocation + " to " + startLocation + ": " + formatDuration(longestFlight.returnDurationMinutes));
        } else {
            System.out.println("Longest flight duration not found.");
        }

        // Example assertions (customize based on actual test criteria)
        assertTrue(cheapestPrice > 0, "Cheapest flight price should be greater than 0");
        // assertNotNull(longestFlight, "Longest flight duration should be found");

        driver.quit();
    }


    private String formatDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format("%dh %dm", hours, minutes);
    }
    
}
    
/*  @Then("I should see the cheapest flight and longest flight duration")
    public void i_should_see_the_cheapest_flight_and_longest_flight_duration() {
        // Print cheapest flight details
        System.out.println("**********************************************************************************");
        System.out.println("Cheapest Flight details:");
        System.out.println("\tFrom: " + startLocation);
        System.out.println("\tTo: " + destinationLocation);
        System.out.println("\tThe cheapest price is: $" + cheapestPrice);
        System.out.println("**********************************************************************************");
        
        // Get top two longest flights
        List<FlightDuration> topTwoLongestFlights = flightSearchPage.getTopTwoLongestFlights();

        if (topTwoLongestFlights != null && !topTwoLongestFlights.isEmpty()) {
            System.out.println("The longest flight duration is: " + formatDuration(topTwoLongestFlights.get(0).totalDurationMinutes));
            System.out.println("\t" + startLocation + " to " + destinationLocation + ": " + formatDuration(topTwoLongestFlights.get(0).outboundDurationMinutes));
            System.out.println("\t" + destinationLocation + " to " + startLocation + ": " + formatDuration(topTwoLongestFlights.get(0).returnDurationMinutes));
            
            if (topTwoLongestFlights.size() > 1) {
                System.out.println("The second longest flight duration is: " + formatDuration(topTwoLongestFlights.get(1).totalDurationMinutes));
                System.out.println("\t" + startLocation + " to " + destinationLocation + ": " + formatDuration(topTwoLongestFlights.get(1).outboundDurationMinutes));
                System.out.println("\t" + destinationLocation + " to " + startLocation + ": " + formatDuration(topTwoLongestFlights.get(1).returnDurationMinutes));
            }
        } else {
            System.out.println("No flight durations found.");
        }

        driver.quit();
    }
    private String formatDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format("%dh %dm", hours, minutes);
    }}*/


