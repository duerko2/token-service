package token.service;

import messaging.Event;
import messaging.MessageQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TokenService {

	TokenRepo tokenRepo = new TokenRepo();
	MessageQueue queue;

	public TokenService(MessageQueue q) {
		System.out.println("Handlers added");
		this.queue = q;
		this.queue.addHandler("InitialTokensRequested", this::handleInitialTokenEvent);
		this.queue.addHandler("PaymentRequestSent", this::handlePaymentRequestSent);
		this.queue.addHandler("NewTokenRequestRequested", this::newTokenRequestRequested);
	}

	public String newTokenRequestRequested(Event ev) {
		System.out.printf("New Token event");
		var account = ev.getArgument(0, String.class);
		int tokensRequested = ev.getArgument(1, Integer.class);
		List<Token> tokenList = new ArrayList<>();

		tokenList = tokenRepo.getTokenList(account);
		if (tokenList.size() <= 1 && tokensRequested <= 5 && tokensRequested >= 1) {
			for (int i = 0; i < tokensRequested; i++) {
				tokenList.add(generateRandomToken());
			}
			addTokensToAccount(account, tokenList);
			Event event = new Event("NewTokenRequestedAssigned", new Object[]{account,tokenList});
			queue.publish(event);
			return account;
		}
		return account;
	}




	public String handleInitialTokenEvent(Event ev) {
		var account = ev.getArgument(0, String.class);
		Integer tokensRequested = ev.getArgument(1,Integer.class);
        // 6 tokens
		List<Token> tokenList = new ArrayList<>();
		for (int i = 0; i < tokensRequested; i++) {
			tokenList.add(generateRandomToken());
		}
		addTokensToAccount(account, tokenList);
		tokenList = tokenRepo.getTokenList(account);
		Event event = new Event("InitialTokensAssigned", new Object[]{account,tokenList});
		queue.publish(event);
		return account;
	}

	private void addTokensToAccount(String accountId, List<Token> tokenList) {
		for (Token token : tokenList) {
			tokenRepo.addToken(token, accountId);
		}
	}

	private Token generateRandomToken() {
		String randomString = UUID.randomUUID().toString();
		return new Token(randomString);
	}

	public void handlePaymentRequestSent(Event ev) {
		var payment = ev.getArgument(0, Payment.class);
		System.out.printf("token before async" + payment.getToken());
		new Thread(()-> concurrentHandlePaymentRequest(payment)).start();
	}
	public void concurrentHandlePaymentRequest(Payment payment){
		System.out.println("PaymentRequest" + payment.getToken());
		var accountId = tokenRepo.getAccountId(payment.getToken());

		payment.setCustomerId(accountId);

		tokenRepo.deleteToken(payment.getToken());
		tokenRepo.addToken(generateRandomToken(),accountId);

		Event event = new Event("PaymentRequestValidated", new Object[]{payment});
		queue.publish(event);
	}
	public TokenRepo getTokenRepo(){
		return tokenRepo;
	}

}
