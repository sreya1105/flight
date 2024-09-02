Feature: Flight Booking

  Scenario Outline: Find the cheapest and longest duration flights
    Given I am on the Booking.com homepage
    When I enter the flight details from "<fromLocation>" to "<toLocation>"
    Then I should see the cheapest flight and longest flight duration
    

    Examples:
      | fromLocation | toLocation  |
      |detroit			 |chicago		|
      
