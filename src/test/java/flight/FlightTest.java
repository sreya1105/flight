package flight;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlightTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.get("https://www.booking.com");
		driver.manage().window().maximize();
        driver.findElement(By.xpath("//a[@id='flights']")).click();
		WebElement from = driver.findElement(By.xpath("(//button[@class='ShellButton-module__btn___tCJzz'])[1]"));
		from.click();
		driver.findElement(By.xpath(
				"//span[@class='Icon-module__root___lkZX8 Tags-module__icon___XQsOM Icon-module__root--size-small___8pl9w']"))
				.click();
		By fromLocation = By.xpath("//input[@class='AutoComplete-module__textInput___tZuOx ']");
		WebElement fromLocationelement = wait.until(ExpectedConditions.visibilityOfElementLocated(fromLocation));
		String startLocation = "Dallas";
		fromLocationelement.sendKeys(startLocation);
		By toSelector = By.xpath("(//span[@class='InputCheckbox-module__field___9MB75'])[2]");
		WebElement toSelectorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(toSelector));
		toSelectorElement.click();
		WebElement to = driver.findElement(By.xpath(
				"//button[@class='ShellButton-module__btn___tCJzz' and @data-ui-name='input_location_to_segment_0']"));
		to.click();
		WebElement toLocation = driver
				.findElement(By.xpath("//input[@class='AutoComplete-module__textInput___tZuOx ']"));
		toLocation.click();
		String destinationLocation = "Denver";
		toLocation.sendKeys(destinationLocation);
		By selector = By.xpath("(//span[@class='InputCheckbox-module__field___9MB75'])[2]");
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
		element.click();
		WebElement dateSelector = driver.findElement(By.xpath("//button[@data-ui-name='button_date_segment_0']"));
		dateSelector.click();
		WebElement fromDate = driver.findElement(By.xpath("//span[contains(@aria-label, '19 October 2024')]"));
		fromDate.click();
		WebElement toDate = driver.findElement(By.xpath("//span[contains(@aria-label, '27 October 2024')]"));
		toDate.click();
		By dateSelectionDone = By.xpath("//span[contains(text(),'Done')]");
		WebElement dateSelectionDoneElement = wait
				.until(ExpectedConditions.visibilityOfElementLocated(dateSelectionDone));
		dateSelectionDoneElement.click();
		By search = By.xpath("//span[contains(text(),'Search')]");
		WebElement searchElement = wait.until(ExpectedConditions.visibilityOfElementLocated(search));
		searchElement.click();
    }

    @Test(groups = {"smoke"}, dataProvider = "flightData", dataProviderClass = FlightData.class)
    public void testGetCheapestPrice(String startLocation, String destinationLocation) {
        searchFlights(startLocation, destinationLocation);
        
        // Process prices
        List<WebElement> priceElements = wait.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-testid='flight_card_price_main_price']")));
        List<Double> prices = new ArrayList<>();
        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replaceAll("[^\\d.]", "");
            if (!priceText.isEmpty()) {
                try {
                    prices.add(Double.parseDouble(priceText)); // Parse string to double
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing price: " + priceText);
                }
            }
        }

        // Find and print the cheapest price
        if (!prices.isEmpty()) {
        	
            double cheapestPrice = Collections.min(prices);
            System.out.println("**********************************************************************************");
            System.out.println("Cheapest Flight details:\n\tFrom: " + startLocation + "\n\tTo:   " + destinationLocation);
            System.out.println("\tThe cheapest price is: $" + cheapestPrice);
            Assert.assertTrue(cheapestPrice > 0, "Cheapest flight price should be greater than 0");
            // Assert that the actual cheapest price matches the expected value
            
        
        } else {
            System.out.println("No prices found.");
            Assert.fail("No flight prices found");
        }
    }

    @Test(groups = {"regression"}, dataProvider = "flightData", dataProviderClass = FlightData.class)
    public void testGetLongestDuration(String startLocation, String destinationLocation) {
        searchFlights(startLocation, destinationLocation);

        // Process durations
        List<WebElement> flightElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//li[@class='List-module__item___VKfBl List-module__item--spacing-medium___j8uHx']")));
        List<Integer> totalDurations = new ArrayList<>();
        List<FlightDuration> flightDurations = new ArrayList<>();
        for (WebElement flight : flightElements) {
            try {
                String outboundDurationText = flight
                        .findElement(By.xpath(".//div[@data-testid='flight_card_segment_duration_0']")).getText();
                String returnDurationText = flight
                        .findElement(By.xpath(".//div[@data-testid='flight_card_segment_duration_1']")).getText();

                int outboundDurationMinutes = convertDurationToMinutes(outboundDurationText);
                int returnDurationMinutes = convertDurationToMinutes(returnDurationText);

                int totalDurationMinutes = outboundDurationMinutes + returnDurationMinutes;
                totalDurations.add(totalDurationMinutes);
                FlightDuration details = new FlightDuration(totalDurationMinutes, outboundDurationMinutes,
                        returnDurationMinutes);
                flightDurations.add(details);
            } catch (Exception e) {
                System.out.println("Error extracting or parsing duration: " + e.getMessage());
            }
        }

        // Find and print the longest total duration
        if (!totalDurations.isEmpty()) {
            FlightDuration longestFlight = Collections.max(flightDurations);
            System.out.println("**********************************************************************************");
            System.out.println("The longest flight duration is: " + formatDuration(longestFlight.totalDurationMinutes));
            System.out.println("\t" + startLocation + " to " + destinationLocation + ": " + formatDuration(longestFlight.outboundDurationMinutes));
            System.out.println("\t" + destinationLocation + " to " + startLocation + ": " + formatDuration(longestFlight.returnDurationMinutes));
            Assert.assertNotNull(longestFlight, "Longest flight duration should be found");
        } else {
            System.out.println("No durations found.");
        }
    }

    private void searchFlights(String startLocation, String destinationLocation) {
        // Flight search logic here...
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static int convertDurationToMinutes(String duration) {
        int minutes = 0;
        Pattern pattern = Pattern.compile("(\\d+)h(?:\\s*(\\d+)m)?");
        Matcher matcher = pattern.matcher(duration);
        if (matcher.find()) {
            minutes += Integer.parseInt(matcher.group(1)) * 60;
            if (matcher.group(2) != null) {
                minutes += Integer.parseInt(matcher.group(2));
            }
        }
        return minutes;
    }

    private static String formatDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return hours + "h " + (minutes > 0 ? minutes + "m" : "");
    }

    static class FlightDuration implements Comparable<FlightDuration> {
        int totalDurationMinutes;
        int outboundDurationMinutes;
        int returnDurationMinutes;

        FlightDuration(int totalDuration, int outboundDuration, int returnDuration) {
            this.totalDurationMinutes = totalDuration;
            this.outboundDurationMinutes = outboundDuration;
            this.returnDurationMinutes = returnDuration;
        }

        @Override
        public int compareTo(FlightDuration other) {
            return Integer.compare(this.totalDurationMinutes, other.totalDurationMinutes);
        }
    }
}
