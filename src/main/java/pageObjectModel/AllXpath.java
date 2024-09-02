package pageObjectModel;

import org.openqa.selenium.By;

public class AllXpath {
	public static final By FLIGHTS_TAB = By.xpath("//a[@id='flights']");
    public static final By FROM_BUTTON = By.xpath("(//button[@class='ShellButton-module__btn___tCJzz'])[1]");
    public static final By LOCATION_ICON = By.xpath("//span[@class='Icon-module__root___lkZX8 Tags-module__icon___XQsOM Icon-module__root--size-small___8pl9w']");
    public static final By FROM_LOCATION_INPUT = By.xpath("//input[@class='AutoComplete-module__textInput___tZuOx ']");
    public static final By TO_BUTTON = By.xpath("//button[@class='ShellButton-module__btn___tCJzz' and @data-ui-name='input_location_to_segment_0']");
    public static final By TO_LOCATION_INPUT = By.xpath("//input[@class='AutoComplete-module__textInput___tZuOx ']");
    public static final By DATE_SELECTOR = By.xpath("//button[@data-ui-name='button_date_segment_0']");
    public static final By FROM_DATE = By.xpath("//span[contains(@aria-label, '19 October 2024')]");
    public static final By TO_DATE = By.xpath("//span[contains(@aria-label, '27 October 2024')]");
    public static final By DATE_SELECTION_DONE = By.xpath("//span[contains(text(),'Done')]");
    public static final By SEARCH_BUTTON = By.xpath("//span[contains(text(),'Search')]");
    public static final By LOCATION_SUGGESTION = By.xpath("(//span[@class='InputCheckbox-module__field___9MB75'])[2]");

    // Locators for prices and flight durations
    public static final By PRICE_ELEMENTS = By.xpath("//div[@data-testid='flight_card_price_main_price']");
    public static final By FLIGHT_ELEMENTS = By.xpath("//li[@class='List-module__item___VKfBl List-module__item--spacing-medium___j8uHx']");
    public static final By OUTBOUND_DURATION = By.xpath(".//div[@data-testid='flight_card_segment_duration_0']");
    public static final By RETURN_DURATION = By.xpath(".//div[@data-testid='flight_card_segment_duration_1']");
}