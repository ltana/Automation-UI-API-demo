@UAT @Mobile @MobileSmoke
Feature: Login flow

  @TestCaseKey=ID
  Scenario: Login smoke
    Given User allows notifications
    And User selects login as an existing customer
    And User accepts terms and conditions before login
    Given User sets credentials as "test_cred" for userID and "test_pass" for password
    Then The Dashboard page is displayed
