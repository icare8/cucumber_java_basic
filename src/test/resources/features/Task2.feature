@PracticalTask2
Feature: Introduction to cucumber practical task 2
  As a bootcamp participant
  I want to complete Task 2 subtasks

  Background:
    Given I navigate to the 'People list' page

  @AddPerson
  Scenario Outline: Add a person
    When I press the 'add person' button
    Then I am on form page
    And Person's data is entered
      | name   | surname   | job   | dob   | language   | gender   | employee   |
      | <name> | <surname> | <job> | <dob> | <language> | <gender> | <employee> |
    And The 'Add' button on the form page is pressed
    Then I am on the 'People list' page
    And I can see the new person added to the list
      | <name> | <surname> | <job> | <dob> | <language> | <gender> | <employee> |
    Examples:
      | name   | surname | job           | dob        | language        | gender | employee   |
      | Robin  | Cohen   | Web Developer | 1970/01/01 | English,French  | Male   | Intern     |
      | Ashley | Isadora | QA Tester     | 12/12/2025 | English,Spanish | Female | Contractor |
      |        |         |               |            |                 |        |            |

#  @EditPerson
#  Scenario:
#    When I press the 'edit' icon for any entry
#    Then I am on form page
#    And The form is filled with person's data
#    Then I change a person's property
#      | property  | new value     |
#      | name      | Robin Cohen   |
#      | job       | Web Developer |
#      | dob       | 09/09/1999    |
#      | languages | English       |
#      | gender    | new Male      |
#      | employee  | Intern        |
#    And The 'Edit' button is pressed
#    Then I am on the "People list" page
#    And The person's property has been changed

  @EditPerson
  Scenario Outline:
    When I press the 'edit' icon for an entry
    Then I am on form page
    And The form is filled with person's data
#    Then I change the person's "<property>" to "<new value>"
    Then I change a person's property
      | <property> | <value> |
    And The 'Edit' button is pressed
    Then I am on the 'People list' page
    And The person's property has been changed

    Examples:
      | property | value |
      | name     | Bill  |
#      | surname   | Spooner           |
#      | job       | Automation Tester |
#      | dob       | 09/09/1999        |
#      | languages | English           |
#      | gender    | Female            |
#      | gender    | Male              |
#      | employee  | Employee          |

  @RemovePerson
  Scenario:
    When I press the 'remove' icon for any entry
    Then I am on the 'People list' page
    And Entry has been removed

  @ResetAfterAddingPerson
  @current
  Scenario:
    Given A person has been added
    When I press the 'reset' button
    Then I am on the 'People list' page
    And I see the default list

  @ResetAfterEditingPerson
  Scenario:
    Given A person has been edited
    When I press the 'reset' button
    Then I am on the 'People list' page
    And I see the default list

  @ResetAfterRemovingPerson
  Scenario:
    Given A person has been removed
    When I press the 'reset' button
    Then I am on the 'People list' page
    And I see the default list

  @ClearForm
  Scenario:
    Given I press the 'add person' button
    And Person's data is entered
      | name   | surname   | job   | dob          | language | gender | employee   |
      | "name" | "surname" | "job" | "01/01/1970" | English  | Female | Contractor |
    Then 'Clear all fields' button is pressed
    Then I am on form page
    And There is no data in the form-page form
