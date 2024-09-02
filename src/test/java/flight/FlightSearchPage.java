package flight;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlightSearchPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public FlightSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.get("https://www.booking.com");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//a[@id='flights']")).click();
		
    }

    public void enterFromLocation(String location) {
    	WebElement from = driver.findElement(By.xpath("(//button[@class='ShellButton-module__btn___tCJzz'])[1]"));
		from.click();
		driver.findElement(By.xpath(
				"//span[@class='Icon-module__root___lkZX8 Tags-module__icon___XQsOM Icon-module__root--size-small___8pl9w']"))
				.click();
        WebElement fromLocation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='AutoComplete-module__textInput___tZuOx ']")));
        fromLocation.sendKeys(location);
        By toSelector = By.xpath("(//span[@class='InputCheckbox-module__field___9MB75'])[2]");
        WebElement toSelectorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(toSelector));
		toSelectorElement.click();
    }

    public void enterToLocation(String location) {
    	
		WebElement to = driver.findElement(By.xpath(
				"//button[@class='ShellButton-module__btn___tCJzz' and @data-ui-name='input_location_to_segment_0']"));
		to.click();
        WebElement toLocation = driver.findElement(By.xpath("//input[@class='AutoComplete-module__textInput___tZuOx ']"));
        toLocation.sendKeys(location);
		By selector = By.xpath("(//span[@class='InputCheckbox-module__field___9MB75'])[2]");
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
		element.click();
        
    }

    public void selectDates() {
        WebElement dateSelector = driver.findElement(By.xpath("//button[@data-ui-name='button_date_segment_0']"));
        dateSelector.click();
        WebElement fromDate = driver.findElement(By.xpath("//span[contains(@aria-label, '19 October 2024')]"));
        fromDate.click();
        WebElement toDate = driver.findElement(By.xpath("//span[contains(@aria-label, '27 October 2024')]"));
        toDate.click();
        WebElement dateSelectionDone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Done')]")));
        dateSelectionDone.click();
    }

    public void searchFlights() {
        WebElement searchElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Search')]")));
        searchElement.click();
    }

    public double getCheapestFlightPrice() {
        List<WebElement> priceElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-testid='flight_card_price_main_price']")));
        List<Double> prices = new ArrayList<>();
        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replaceAll("[^\\d.]", "");
            if (!priceText.isEmpty()) {
                try {
                    prices.add(Double.parseDouble(priceText));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing price: " + priceText);
                }
            }
        }
        return prices.isEmpty() ? -1 : Collections.min(prices);
    }

   public FlightDuration getLongestFlightDuration() {
        List<WebElement> flightElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@class='List-module__item___VKfBl List-module__item--spacing-medium___j8uHx']")));
        List<FlightDuration> flightDurations = new ArrayList<>();
        for (WebElement flight : flightElements) {
            try {
                String outboundDurationText = flight.findElement(By.xpath(".//div[@data-testid='flight_card_segment_duration_0']")).getText();
                String returnDurationText = flight.findElement(By.xpath(".//div[@data-testid='flight_card_segment_duration_1']")).getText();
                int outboundDurationMinutes = convertDurationToMinutes(outboundDurationText);
                int returnDurationMinutes = convertDurationToMinutes(returnDurationText);
                int totalDurationMinutes = outboundDurationMinutes + returnDurationMinutes;
                flightDurations.add(new FlightDuration(totalDurationMinutes, outboundDurationMinutes, returnDurationMinutes));
            } catch (Exception e) {
                System.out.println("Error extracting or parsing duration: " + e.getMessage());
            }
        }
//        return flightDurations.isEmpty() ? null : Collections.max(flightDurations);
        FlightDuration longestFlight;
        if (flightDurations.isEmpty()) {
            longestFlight = null;
        } else {
            longestFlight = Collections.max(flightDurations);
        }
        
        return longestFlight;
    }
   /*  public List<FlightDuration> getTopTwoLongestFlights() {
        List<WebElement> flightElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@class='List-module__item___VKfBl List-module__item--spacing-medium___j8uHx']")));
        List<FlightDuration> flightDurations = new ArrayList<>();
        for (WebElement flight : flightElements) {
            try {
                String outboundDurationText = flight.findElement(By.xpath(".//div[@data-testid='flight_card_segment_duration_0']")).getText();
                String returnDurationText = flight.findElement(By.xpath(".//div[@data-testid='flight_card_segment_duration_1']")).getText();
                int outboundDurationMinutes = convertDurationToMinutes(outboundDurationText);
                int returnDurationMinutes = convertDurationToMinutes(returnDurationText);
                int totalDurationMinutes = outboundDurationMinutes + returnDurationMinutes;
                flightDurations.add(new FlightDuration(totalDurationMinutes, outboundDurationMinutes, returnDurationMinutes));
            } catch (Exception e) {
                System.out.println("Error extracting or parsing duration: " + e.getMessage());
            }
        }
        Collections.sort(flightDurations, Collections.reverseOrder());
        return flightDurations.size() > 1 ? flightDurations.subList(0, 2) : flightDurations;
    }*/


    private int convertDurationToMinutes(String duration) {
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

    public static class FlightDuration implements Comparable<FlightDuration> {
        public int totalDurationMinutes;
        public int outboundDurationMinutes;
        public int returnDurationMinutes;

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