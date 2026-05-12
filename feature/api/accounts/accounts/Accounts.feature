@UAT
@US-ID @API
Feature: /accounts - Account details

  Background: Set request body from json
    Given User set request body to 'accounts.json' from 'accounts' folder

  @TestCaseKey=ID
  Scenario Outline: /accounts - Accounts schema validation test with different <language>
    When User sets request with parameters:
      | requestParameter | requestValue |
      | header.language  | <language>   |
    When User makes a POST request to "/accounts" resource from "baseUrlAccounts" endpoint
    Then Status code equals 200
    And Response json schema equals to 'accounts.json' from 'accounts' folder
    Examples:
      | language |
      | EN       |

  @TestCaseKey=ID @APIRegression
  Scenario Outline: /accounts - Request without required fields returns 400 using "<parameter>"
    Given User set request body without parameters:
      | requestParameter |
      | <parameter>      |
    When User makes a POST request to "/accounts" resource from "baseUrlAccounts" endpoint
    Then Status code equals 400
    And Response has next returned values:
      | responseParameter | responseValue                |
      | errors.message | <parameter>: <valueResponse> |
      | errors.code    | 0-400                        |
    Examples:
      | parameter     | valueResponse |
      | header.userId | error message |
