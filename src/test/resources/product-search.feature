@txn
Feature: Search for product

  Background:
    Given Supplier "X" exists
    And Supplier "Y" exists

  Scenario: Empty search criteria
    Given One product exists
    Then That product is returned by search

  Scenario: No match by name
    Given One product exists
    And That product has name "name"
    When The search criteria name is "namex"
    Then No products are returned by search

  Scenario Outline: Full Match
    Given One product exists
    And That product has name "<productName>"
    And That product has supplier "<productSupplier>"
    When The search criteria name is "<searchName>"
    And The search criteria supplier is "<searchSupplier>"
    Then That product is returned by search: "<returned>"

    Examples:
      | productName | productSupplier | searchName | searchSupplier | returned |
      | a           |                 | a          |                | true     |