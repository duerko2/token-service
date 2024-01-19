Feature: Token assignment to account when created

  Scenario: Token Assignment
    When a "InitialTokensRequested" event for an account is received that asks for 6 tokens
    Then the "InitialTokensAssigned" event is sent
    And the account has 6 tokens

  Scenario: Token removal
   Given a valid payment with a valid token that exist
    When a "PaymentRequestSent" for a payment
    Then the "PaymentRequestValidated" event is sent with a payment
    And the token is deleted

  Scenario:
    When a "InitialTokensRequested" event for an account is received that asks for 1 tokens
    Then the "InitialTokensAssigned" event is sent
    And the account has 1 tokens
    When a "InitialTokensRequested" event for an account is received again that asks for 5 tokens
    Then the "NewTokenRequestedAssigned" event is again
    And the account has 6 tokens
    #Then the "NewTokenRequestedAssigned" event is sent and the account has 4 tokens

  Scenario:
    When a "InitialTokensRequested" event for an account is received that asks for 6 tokens
    Then the "InitialTokensAssigned" event is sent
    And the account has 6 tokens
    When a "InitialTokensRequested" event for an account is received that asks for 6 tokens
    And the account has 6 tokens