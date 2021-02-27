@txn # Runs each Scenario or Example in its own @Transactional
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
    And That product has category "<productCategory>"
    And That product has supplier "<productSupplier>"
    When The search criteria name is "<searchName>"
    And The search criteria supplier is "<searchSupplier>"
    And The search criteria category is "<searchCategory>"
    Then That product is returned by search: "<returned>"

    Examples:
      | productName | productCategory | productSupplier | searchName | searchCategory | searchSupplier | returned |
      | aX          |                 |                 | a          |                |                | true     |
      | aX          |                 |                 | aY         |                |                | false    |
#  TODO search by category
#    TODO search by supplier
