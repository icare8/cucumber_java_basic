@PracticalTask1
Feature: Introduction to cucumber practical task 1
  As a bootcamp participant
  I want to complete the task 1

  Background:
    Given I am on the tested page

  @InvalidInput
  Scenario Outline: Error cases
    When I enter a number "<number>"
    And press the submit button
    Then I see an error message: "<errorMessage>"

    Examples:
      | number           | errorMessage                 |
      | 24               | Number is too small          |
      | 0                | Number is too small          |
      | 101              | Number is too big            |
      | 1001             | Number is too big            |
      |                  | You haven't entered anything |
      | This is a string | Please enter a number        |
      | "55"             | Please enter a number        |

    #Values that break the expected flow of the app
    @UndefinedBehaviour
    Scenarios: Bug
      | number | errorMessage        |
      | 42     | Number is too small |
      | 49     | Number is too small |
      | 666    | Number is too big   |
      | -1     | Number is too small |

  @ValidInput
  Scenario: A random valid number
    When I enter a number in the range 50-100
    And press the submit button
    Then a pop-up appears with a square-root of that number rounded to two decimal places

  @ValidInput
  Scenario: Valid lower and upper  boundary
    When I enter the smallest valid number
    And press the submit button
    Then a pop-up appears with a square-root of that number rounded to two decimal places
    Then I enter the largest valid number
    And press the submit button
    Then a pop-up appears with a square-root of that number rounded to two decimal places

  @Loop
  Scenario: Every valid number
    And every number in the range 50-100 works
