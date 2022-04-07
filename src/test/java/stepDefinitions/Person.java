package stepDefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

public class Person {
    String name;
    String surname;
    String job;
    String dob;
    String languages = "";
    String gender;
    String employee;

    public Person(List<String> args) {
        if (args.size() < 7) {
            throw new IllegalArgumentException("At least 7 arguments are needed to create a Person");
        }
        int index = 0;
        name = args.get(index++);
        surname = args.get(index++);
        job = args.get(index++);
        dob = args.get(index++);
        languages = args.get(index++);
        gender = args.get(index++);
        employee = args.get(index++);

        if (languages.isEmpty()) {
            languages = "English";
        }
        if (gender.isEmpty()) {
            gender = "undefined";
        }
        if (employee.isEmpty()) {
            employee = "employee";
        }
    }

    public Person(Map<String, String> args) {
        try {
            name = args.get("name");
            surname = args.get("surname");
            job = args.get("job");
            dob = args.get("dob");
            languages = args.get("languages");
            gender = args.get("gender");
            employee = args.get("employee");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    public Person(WebElement element) {
        name = element.findElement(By.cssSelector("span.name")).getText();
        surname = element.findElement(By.cssSelector("span.surname")).getText();
        job = element.findElement(By.cssSelector("span.job")).getText();
        dob = element.findElement(By.cssSelector("span.dob")).getText();
//        languages = element.findElement(By.cssSelector("span.language")).getText();
        String[] parts = element.findElement(By.cssSelector("span.language")).getText().split(" ");
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            languages += part;
        }
        languages = languages.substring(0, languages.length() - 1);
        gender = element.findElement(By.cssSelector("span.gender")).getText();
        if (gender.equals("male")) {
            gender = "Male";
        }
        if (gender.equals("female")) {
            gender = "Female";
        }
        employee = element.findElement(By.cssSelector("span.status")).getText();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Person))
            return false;
 /*       if (obj.getClass() != this.getClass())
            return false;*/
        return this.equals((Person) obj);
    }

    public boolean equals(Person other) {
        return (
                this.name.equalsIgnoreCase(other.getName())
                        && this.surname.equalsIgnoreCase(other.getSurname())
                        && this.job.equalsIgnoreCase(other.getJob())
                        && this.dob.equalsIgnoreCase(other.getDob())
                        && getLanguages(this.languages).equals( getLanguages(other.getLanguages()) )
//                        && this.languages.equalsIgnoreCase(other.getLanguages())
                        && this.gender.equalsIgnoreCase(other.getGender())
                        && this.employee.equalsIgnoreCase(other.getEmployee())
        );
    }

    private List<String> getLanguages(String languages) {
        //TODO: verify sorting
        List<String> result = Arrays.asList(languages.toLowerCase().split(", "));
        java.util.Collections.sort(result, Collator.getInstance() );
        out.println(
                result
        );
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", job='" + job + '\'' +
                ", dob='" + dob + '\'' +
                ", languages='" + languages + '\'' +
                ", gender='" + gender + '\'' +
                ", employee='" + employee + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getJob() {
        return job;
    }

    public String getDob() {
        return dob;
    }

    public String getLanguages() {
        return languages;
    }

    public String getGender() {
        return gender;
    }

    public String getEmployee() {
        return employee;
    }

    public void setName(String name) {
        this.name = name;
    }
}
