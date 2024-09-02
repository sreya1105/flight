package RunnerClass;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
		    features = "C:\\Users\\user11\\Documents\\workspace-spring-tool-suite-4-4.21.0.RELEASE\\JavaPractice\\flight\\src\\test\\resources\\cucmber\\flight_search.feature", 
    glue = "stepDefinition", 
    plugin = {"pretty", "html:target/cucumber-reports.html"} 
)
public class TestRunner extends AbstractTestNGCucumberTests {


}