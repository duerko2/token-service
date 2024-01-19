package behaviourtests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import messaging.MessageQueue;
import token.service.aggregate.*;
import token.service.events.BankAccountRequest;
import token.service.events.PaymentCreated;
import token.service.events.TokensAssigned;
import token.service.events.TokensRequested;
import token.service.service.TokenService;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TokenServiceSteps {
	MessageQueue queue = mock(MessageQueue.class);
	TokenService s = new TokenService(queue);
	Account account;
	Payment payment;
	Token token;
	private AccountId customerID;


	@When("a {string} event for an account is received that asks for {int} tokens")
	public void aEventForAnAccountIsReceivedThatAsksForTokens(String eventName, Integer tokensRequested) {
		account = new Account();
		// Bond..
		account.setName("James");
		account.setLastname("Bond");
		account.setCpr("007");
		account.setAccountId(new AccountId(UUID.randomUUID()));

		//System.out.println(account);

		s.handleTokensRequested(new TokensRequested(account.getAccountId(), tokensRequested));
	}

	@When("a {string} event for an account is received again that asks for {int} tokens")
	public void aEventForAnAccountIsReceivedAgainThatAsksForTokens(String eventName, Integer tokensRequested) {
		s.handleTokensRequested(new TokensRequested(account.getAccountId(), tokensRequested));
	}

	@Then("the {string} event is sent")
	public void theEventIsSent(String eventName) {


		var expectedTokens = new HashSet<Token>();
		expectedTokens.add(new Token("xxx69xxx"));

		var message = new TokensAssigned(account.getAccountId(), expectedTokens);
		verify(queue).publish(message);
	}

	@Then("the account has {int} tokens")
	public void theAccountGetsTokens(Integer int1) {
		assertEquals(int1, s.getTokenRepo().getTokenList(account.getAccountId().getUuid().toString()).size());
	}

	@Then("the {string}")
	public void the(String string) {
		// Write code here that turns the phrase above into concrete actions

	}

	@Given("a valid payment with a valid token that exist")
	public void aValidPaymentWithAValidTokenThatExist() {
		token = new Token("xxx69xxx");
		customerID = new AccountId(UUID.randomUUID());
		s.getTokenRepo().addToken( token, customerID);

		payment = new Payment();
		payment.setAmount(100);
		payment.setMerchantId(new AccountId(UUID.randomUUID()));
		payment.setToken(token);
		payment.setPaymentId(new PaymentId(UUID.randomUUID()));

	}
	@When("a {string} for a payment")
	public void aForAPayment(String eventName) throws InterruptedException {

		PaymentCreated m = new PaymentCreated(payment.getPaymentId(),payment.getAmount(),payment.getToken(),payment.getMerchantId());


		s.handlePaymentRequestSent(m);

		// simulate downstream service
		Thread.sleep(500);
	}

	@Then("the token is deleted")
	public void the_token_is_deleted() {
		// Write code here that turns the phrase above into concrete actions'
		assertNull(s.getTokenRepo().getAccountId(token));
	}


	@Then("the {string} event is sent with a payment")
	public void theEventIsSentWithAPayment(String eventName) {


		var event = new BankAccountRequest(payment.getPaymentId(),customerID, payment.getMerchantId(),payment.getAmount());
		verify(queue).publish(event);

	}


	@Then("the {string} event is again")
	public void theEventIsAgain(String arg0) {
		var expectedTokens = new HashSet<Token>();
		expectedTokens.add(new Token("xxx69xxx"));

		var message = new TokensAssigned(account.getAccountId(), expectedTokens);
		verify(queue,times(2)).publish(message);

	}
}


