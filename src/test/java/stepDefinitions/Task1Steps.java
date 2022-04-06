package stepDefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;

import java.util.Random;

import static java.lang.String.format;
import static org.junit.Assert.*;

public class Task1Steps {
    private final WebDriver driver;
    private String numberArgument;
    public Task1Steps() {
        this.driver = Hooks.driver;
    }

    @Given("^I am on the tested page$")
    public void iAmOnTheTestedPage() {
        driver.get(
                "https://kristinek.github.io/site/tasks/enter_a_number"
        );
    }

    @When("^I enter a number \"(.*)\"$")
    public void iEnterANumber(String numberArg) throws Throwable {
        String fieldID = "numb";
        numberArgument = numberArg;
        driver.findElement(By.id(fieldID)).clear();
        driver.findElement(By.id(fieldID)).sendKeys(numberArg);
    }

    @Then("^I see an error message: \"([^\"]*)\"$")
    public void iSeeAnErrorMessage(String errorMessageArg) throws Throwable {
        String errorID = "ch1_error";
        try {
            assertTrue(driver.findElement(By.id(errorID)).isDisplayed() );
            assertEquals(errorMessageArg, driver.findElement(By.id(errorID)).getText() );
        } catch (UnhandledAlertException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Then("^a pop-up appears with a square-root of that number rounded to two decimal places$")
    public void aPopUpAppearsWithASquareRootOfTheNumberRoundedToTwoDecimalPlaces() throws InterruptedException {
        Alert popup = driver.switchTo().alert();
        String popupText = popup.getText();
        popup.accept();
        try {
            String expected = format("Square root of %s is %.2f", numberArgument, Math.sqrt(Double.parseDouble(numberArgument)) );
            assertEquals(expected, popupText);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            fail();
        }
    }

    @And("^press the submit button$")
    public void pressTheSubmitButton() {
        String buttonID = "w3-orange";
        try {
            driver.findElement(By.className(buttonID)).click();
            // TODO: remove sleep
            Thread.sleep(250);
        } catch (UnhandledAlertException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("^I enter a number in the range (\\d+)-(\\d+)$")
    public void iEnterANumberInTheRange(int min, int max) throws Throwable {
        int random = new Random().nextInt(50 + max) - min;
        iEnterANumber(String.valueOf(min));
    }

    @Given("^every number in the range (\\d+)-(\\d+) works$")
    public void everyNumberInTheRangeWorks(int min, int max) throws Throwable {
        for (; min <= max; min++) {
            iEnterANumber(String.valueOf(min));
            pressTheSubmitButton();
            aPopUpAppearsWithASquareRootOfTheNumberRoundedToTwoDecimalPlaces();
        }
    }
}
