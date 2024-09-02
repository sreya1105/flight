package pageObjectModel;

import java.util.Collections;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Runner {

    WebDriver driver;
    Logic flightActions;
    String startLocation = "Dallas";
    String destinationLocation = "Denver";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        flightActions = new Logic(driver);
        driver.get("https://www.booking.com");
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(groups = {"smoke", "regression"})
    public void testCheapestFlightDetails() {
        flightActions.performFlightSearch(startLocation, destinationLocation);
        List<Double> prices = flightActions.getPrices();
        if (!prices.isEmpty()) {
            double cheapestPrice = Collections.min(prices);
            System.out.println("**********************************************************************************");
            System.out.println("Cheapest Flight details:\n\tFrom: " + startLocation + "\n\tTo:   " + destinationLocation);
            System.out.println("\tThe cheapest price is: $" + cheapestPrice);

            Assert.assertTrue(cheapestPrice > 0, "Cheapest flight price should be greater than 0");
        } else {
            System.out.println("No prices found.");
            Assert.fail("No flight prices found");
        }
    }

    @Test(groups = {"regression"})
    public void testLongestFlightDurationDetails() {
        flightActions.performFlightSearch(startLocation, destinationLocation);
        Logic.FlightDuration longestFlight = flightActions.getLongestFlightDuration();
        if (longestFlight != null) {
            System.out.println("**********************************************************************************");
            System.out.println("The longest flight duration is: " + Logic.formatDuration(longestFlight.totalDurationMinutes));
            System.out.println("\t" + startLocation + " to " + destinationLocation + ": " + Logic.formatDuration(longestFlight.outboundDurationMinutes));
            System.out.println("\t" + destinationLocation + " to " + startLocation + ": " + Logic.formatDuration(longestFlight.returnDurationMinutes));

            Assert.assertTrue(longestFlight.totalDurationMinutes > 0, "Longest flight duration should be greater than 0");
        } else {
            System.out.println("No durations found.");
            Assert.fail("No flight durations found");
        }
    }
}