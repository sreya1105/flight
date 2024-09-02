package pageObjectModel;



import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Logic {

    private WebDriver driver;
    private WebDriverWait wait;

    public Logic(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void performFlightSearch(String startLocation, String destinationLocation) {
        driver.findElement(AllXpath.FLIGHTS_TAB).click();
        WebElement from = driver.findElement(AllXpath.FROM_BUTTON);
        from.click();
        driver.findElement(AllXpath.LOCATION_ICON).click();
        WebElement fromLocationElement = wait.until(ExpectedConditions.visibilityOfElementLocated(AllXpath.FROM_LOCATION_INPUT));
        fromLocationElement.sendKeys(startLocation);
        WebElement fromSelectorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(AllXpath.LOCATION_SUGGESTION));
        fromSelectorElement.click();


        WebElement to = driver.findElement(AllXpath.TO_BUTTON);
        to.click();
        WebElement toLocation = driver.findElement(AllXpath.TO_LOCATION_INPUT);
        toLocation.click();
        toLocation.sendKeys(destinationLocation);
        WebElement toSelectorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(AllXpath.LOCATION_SUGGESTION));
        toSelectorElement.click();

        WebElement dateSelector = driver.findElement(AllXpath.DATE_SELECTOR);
        dateSelector.click();
        WebElement fromDate = driver.findElement(AllXpath.FROM_DATE);
        fromDate.click();
        WebElement toDate = driver.findElement(AllXpath.TO_DATE);
        toDate.click();
        WebElement dateSelectionDoneElement = wait.until(ExpectedConditions.visibilityOfElementLocated(AllXpath.DATE_SELECTION_DONE));
        dateSelectionDoneElement.click();
        WebElement searchElement = wait.until(ExpectedConditions.visibilityOfElementLocated(AllXpath.SEARCH_BUTTON));
        searchElement.click();
    }

    public List<Double> getPrices() {
        List<WebElement> priceElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(AllXpath.PRICE_ELEMENTS));
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
        return prices;
    }

    public FlightDuration getLongestFlightDuration() {
        List<WebElement> flightElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(AllXpath.FLIGHT_ELEMENTS));
        List<FlightDuration> flightDurations = new ArrayList<>();
        for (WebElement flight : flightElements) {
            try {
                String outboundDurationText = flight.findElement(AllXpath.OUTBOUND_DURATION).getText();
                String returnDurationText = flight.findElement(AllXpath.RETURN_DURATION).getText();
                int outboundDurationMinutes = convertDurationToMinutes(outboundDurationText);
                int returnDurationMinutes = convertDurationToMinutes(returnDurationText);
                int totalDurationMinutes = outboundDurationMinutes + returnDurationMinutes;
                FlightDuration details = new FlightDuration(totalDurationMinutes, outboundDurationMinutes, returnDurationMinutes);
                flightDurations.add(details);
            } catch (Exception e) {
                System.out.println("Error extracting or parsing duration: " + e.getMessage());
            }
        }
        return flightDurations.isEmpty() ? null : Collections.max(flightDurations);
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

    public static String formatDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return hours + "h " + (minutes > 0 ? minutes + "m" : "");
    }

    public static class FlightDuration implements Comparable<FlightDuration> {
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