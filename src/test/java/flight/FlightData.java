package flight;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FlightData {

    @DataProvider(name = "flightData")
    public static Object[][] flightData() {
        return new Object[][] {
                {"Dallas", "Denver"},
                {"New York", "Chicago"},
                {"San Francisco", "Los Angeles"}
        };
    }
    @Test(groups = "smoke", dataProvider = "flightData")
    public void testGetCheapestPrice(String startLocation, String destinationLocation) {
        // Your test code here
    }

    @Test(groups = "regression", dataProvider = "flightData")
    public void testGetLongestDuration(String startLocation, String destinationLocation) {
        // Your test code here
    }
}


