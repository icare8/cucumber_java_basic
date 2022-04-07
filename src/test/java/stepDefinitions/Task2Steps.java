package stepDefinitions;

import com.google.common.collect.Lists;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.lexer.Da;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import stepDefinitions.Hooks;

import java.util.*;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.Assert.*;

public class Task2Steps {
    private String listPageUrl = "https://kristinek.github.io/site/tasks/list_of_people.html";
    private String formPageUrl = "https://kristinek.github.io/site/tasks/enter_a_new_person.html";
    private WebDriver driver;
//    static FormPage formPage;
//    static ListPage ListPage;

    public Task2Steps() {
        this.driver = Hooks.driver;
//        formPage = PageFactory.initElements(Hooks.driver, AgePage.class);
//        listPage = PageFactory.initElements(Hooks.driver, AgeSubmittedPage.class);
    }

    @When("^I press the 'add person' button$")
    public void iPressTheAddPersonButton() {
        String buttonSelector = "#addPersonBtn";
        WebElement button = driver.findElement(By.cssSelector(buttonSelector));
        assertNotNull(button);
        button.click();
    }


    @Given("^I navigate to the 'People list' page$")
    public void iNavigateToThePeopleListPage() {
        driver.get(listPageUrl);
        iAmOnThePeopleListPage();
    }

    @Then("^I am on form page$")
    public void iAmOnFormPage() {
        assertTrue(driver.getCurrentUrl().contains(formPageUrl));
    }

    @And("^Person's data is entered$")
    public void personSDataIsEntered(DataTable data) {
        List<String> keys = data.topCells();
        data.asMaps(String.class, String.class).forEach(
                map -> {
                    keys.forEach(key -> {
//                                out.println(key + ' ' + map.get(key));
                                if (key.equals("language")) {
                                    List<String> languages = Lists.newArrayList(map.get(key).toLowerCase().split("(,)"));
                                    for (String language : languages) {
                                        if (language.isEmpty()) {
                                            continue;
                                        }
                                        if (!driver.findElement(By.cssSelector("input[id='" + language + "']")).isSelected()) {
                                            driver.findElement(By.cssSelector("input[id='" + language + "']")).click();
                                        }
                                    }
                                    return;
                                }
                                if (key.equals("gender")) {
                                    if (map.get(key).isEmpty()) {
                                        return;
                                    }
                                    driver.findElement(By.cssSelector("input[id='" + map.get(key).toLowerCase() + "']")).click();
                                    return;
                                }
                                if (key.equals("employee")) {
                                    Select statusDropdown = new Select(driver.findElement(By.id("status")));
                                    if (map.get(key).isEmpty()) {
                                        return;
                                    }
                                    statusDropdown.selectByValue(map.get(key).toLowerCase());
                                    /*Click outside the menu*/
                                    driver.findElement(By.id("addEditPerson")).click();
                                    return;
                                }
                                driver.findElement(By.id(key)).sendKeys(map.get(key));
                                if (key.equals("dob")) {
                                    /*Click outside the calender*/
                                    driver.findElement(By.id("addEditPerson")).click();
                                }

                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                    );
                }
        );
    }

    @Then("^I am on the 'People list' page$")
    public void iAmOnThePeopleListPage() {
        assertEquals(listPageUrl, driver.getCurrentUrl());
    }

    @And("^I can see the new person added to the list$")
    public void iCanSeeTheNewPersonAddedToTheList(List<String> args) throws InterruptedException {
        Person expected = new Person(args);
        WebElement peopleList = driver.findElement(By.id("listOfPeople"));
        List<WebElement> personsInList = peopleList.findElements(By.tagName("li"));
        List<Person> entryList = new ArrayList<>();
        personsInList.forEach(entry -> {
            entryList.add(new Person(entry));
        } );
        assertTrue(entryList.contains(expected));

        Thread.sleep(500);
    }

    @And("^The 'Add' button on the form page is pressed$")
    public void theAddButtonOnTheFormPageIsPressed() {
        WebElement button = driver.findElement(By.xpath("//div[@class='w3-btn-group']/button[1]"));
        assertNotNull(button);
        button.click();
//        driver.get(listPageUrl);
    }

    int testIndex;

    @When("^I press the 'edit' icon for an entry$")
    public void iPressTheEditIconForAnEntry() {
        /* Save the person for comparison*/
        WebElement peopleList = driver.findElement(By.id("listOfPeople"));
        List<WebElement> personsInList = peopleList.findElements(By.tagName("li"));
        testIndex = 0;
        testPerson = new Person(personsInList.get(testIndex));

        String buttonSelector = "//li[contains(@id, 'person" + testIndex + "')]//span[contains(@class, 'editbtn')]";
        WebElement button = driver.findElement(By.xpath(buttonSelector));
        assertNotNull(button);
        button.click();
    }

    @And("^The form is filled with person's data$")
    public void theFormIsFilledWithPersonSData() {
        assertEquals(testPerson.getName(),
                driver.findElement(By.cssSelector("input#name")).getAttribute("value")
        );
    }

    Map<String, String> changedProperty = new HashMap<String, String>();
    Person testPerson;

    @Then("^I change a person's property$")
    public void iChangeAPersonSProperty(Map<String, String> args) {
        //TODO: implement properties other than name
        for (Map.Entry<String, String> e : args.entrySet()) {
            driver.findElement(By.cssSelector("input#" + e.getKey() + "")).clear();
            driver.findElement(By.cssSelector("input#" + e.getKey() + "")).sendKeys(e.getValue());
        }
    }

    @And("^The 'Edit' button is pressed$")
    public void theEditButtonIsPressed() {
        WebElement button = driver.findElement(By.xpath("//div[@class='w3-btn-group']/button[1]"));
        assertNotNull(button);
        button.click();
    }

    @And("^The person's property has been changed$")
    public void thePersonSPropertyHasBeenChanged() {
        WebElement peopleList = driver.findElement(By.id("listOfPeople"));
        List<WebElement> personsInList = peopleList.findElements(By.tagName("li"));
        Person editedPerson = new Person(personsInList.get(testIndex));
        assertFalse(
                testPerson.equals(editedPerson)
        );
    }

    @When("^I press the 'remove' icon for any entry$")
    public void iPressTheRemoveIconForAnyEntry() {
        /* Save the person for comparison*/
        WebElement peopleList = driver.findElement(By.id("listOfPeople"));
        List<WebElement> personsInList = peopleList.findElements(By.tagName("li"));
        testIndex = 0;
        testPerson = new Person(personsInList.get(testIndex));

        String buttonSelector = "//li[contains(@id, 'person" + testIndex + "')]//span[contains(@class, 'closebtn')]";
        WebElement button = driver.findElement(By.xpath(buttonSelector));
        assertNotNull(button);
        button.click();
    }

    @And("^Entry has been removed$")
    public void entryHasBeenRemoved() {
        WebElement peopleList = driver.findElement(By.id("listOfPeople"));
        List<WebElement> personsInList = peopleList.findElements(By.tagName("li"));
        List<Person> entryList = new ArrayList<>();
        personsInList.forEach(entry -> entryList.add(new Person(entry)));
        assertFalse(
                entryList.contains(testPerson)
        );
    }

    List<Person> testList;

    @Given("^A person has been edited$")
    public void aPersonHasBeenEdited() {
        /*Save the default list for comparison*/
        WebElement peopleList = driver.findElement(By.id("listOfPeople"));
        List<WebElement> personsInList = peopleList.findElements(By.tagName("li"));
        List<Person> p1 = new ArrayList<>();
        personsInList.forEach(entry -> p1.add(new Person(entry)));
        testList = p1;

        iPressTheEditIconForAnEntry();
        iAmOnFormPage();
        theFormIsFilledWithPersonSData();

        Map<String, String> testMap = new HashMap<>();
        testMap.put("name", "Alice");
        iChangeAPersonSProperty(testMap);

        theEditButtonIsPressed();
        iAmOnThePeopleListPage();
        thePersonSPropertyHasBeenChanged();
    }

    @When("^I press the 'reset' button$")
    public void iPressTheResetButton() {
        String buttonSelector = "#addPersonBtn:last-of-type";
        WebElement button = driver.findElement(By.cssSelector(buttonSelector));
        assertNotNull(button);
        button.click();
    }

    @And("^I see the default list$")
    public void iSeeTheDefaultList() {
        WebElement peopleList = driver.findElement(By.id("listOfPeople"));
        List<WebElement> personsInList = peopleList.findElements(By.tagName("li"));
        List<Person> p1 = new ArrayList<>();
        personsInList.forEach(entry -> p1.add(new Person(entry)));
        p1.removeAll(testList);
        assertTrue(p1.isEmpty());
    }

    @Given("^A person has been removed$")
    public void aPersonHasBeenRemoved() {
        /*Save the default list for comparison*/
        WebElement peopleList = driver.findElement(By.id("listOfPeople"));
        List<WebElement> personsInList = peopleList.findElements(By.tagName("li"));
        List<Person> p1 = new ArrayList<>();
        personsInList.forEach(entry -> p1.add(new Person(entry)));
        testList = p1;

        iPressTheRemoveIconForAnyEntry();
        iAmOnThePeopleListPage();
        entryHasBeenRemoved();
    }

    @Then("^'Clear all fields' button is pressed$")
    public void clearAllFieldsButtonIsPressed() {
        String buttonSelector = "div.clear-btn>button";
        WebElement button = driver.findElement(By.cssSelector(buttonSelector));
        assertNotNull(button);
        button.click();
    }

    @And("^There is no data in the form-page form$")
    public void thereIsNoDataInTheFormPageForm() {
        String name = driver.findElement(By.cssSelector("input#name")).getAttribute("value");
        String surname = driver.findElement(By.cssSelector("input#surname")).getAttribute("value");
        String job = driver.findElement(By.cssSelector("input#job")).getAttribute("value");
        String dob = driver.findElement(By.cssSelector("input#dob")).getAttribute("value");
        assertTrue(
                name.isEmpty()
                        && surname.isEmpty()
                        && job.isEmpty()
                        && dob.isEmpty()
        );
        List<WebElement> gender = driver.findElements(By.cssSelector("input[name='gender']"));
        gender.forEach(box -> assertFalse(box.isSelected())
        );
        List<WebElement> languages = driver.findElements(By.cssSelector("input[name='language']"));
        languages.forEach(box -> {
                    if (box.getAttribute("id").equals("english")) {
                        assertTrue(box.isSelected());
                        return;
                    }
                    assertFalse(box.isSelected());
                }
        );
        String employee = driver.findElement(By.cssSelector("#status")).getAttribute("value");
        assertEquals("employee", employee);
    }

    @Given("^A person has been added$")
    public void aPersonHasBeenAdded() throws InterruptedException {
        /*Save the default list for comparison*/
        WebElement peopleList = driver.findElement(By.id("listOfPeople"));
        List<WebElement> personsInList = peopleList.findElements(By.tagName("li"));
        List<Person> p1 = new ArrayList<>();
        personsInList.forEach(entry -> p1.add(new Person(entry)));
        testList = p1;

        List<String> valuesList = Arrays.asList(
                "Alice",
                "Valderrama",
                "Project Manager",
                "07/02/1972",
                "English,French",
                "Female",
                "Employee"
        );
        String[] columnNames = {"name", "surname", "job", "dob", "language", "gender", "employee"};
        List<String> columnNamesList = Arrays.asList(columnNames);
        List<List<String>> raw = Lists.newArrayList(columnNamesList, valuesList);
        DataTable testData = DataTable.create(raw);

        iPressTheAddPersonButton();
        iAmOnFormPage();
        personSDataIsEntered(testData);
        theAddButtonOnTheFormPageIsPressed();
        iAmOnThePeopleListPage();
        iCanSeeTheNewPersonAddedToTheList(valuesList);
        Thread.sleep(1000);
    }
}
