package token.service;

import messaging.Event;
import messaging.MessageQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * @Author: Nikolaj Beier
 */
public class TokenService {

	TokenRepo tokenRepo = new TokenRepo();
	MessageQueue queue;

	public TokenService(MessageQueue q) {
		this.queue = q;
		this.queue.addHandler("InitialTokensRequested", this::handleInitialTokenEvent);
		this.queue.addHandler("PaymentRequestSent", this::handlePaymentRequestSent);
		this.queue.addHandler("NewTokenRequestRequested", this::newTokenRequestRequested);
	}

	public Account newTokenRequestRequested(Event ev) {
		var account = ev.getArgument(0, Account.class);
		int tokensRequested = ev.getArgument(1, Integer.class);
		List<Token> tokenList = new ArrayList<>();

		tokenList = tokenRepo.getTokenList(account.getAccountId());

		if (tokenList.size() <= 1 && tokensRequested <= 5 && tokensRequested >= 1) {


			for (int i = 0; i < tokensRequested; i++) {
				tokenList.add(generateRandomToken());
			}
			addTokensToAccount(account, account.tokens);
			addTokensToAccount(account, tokenList);
			Event event = new Event("NewTokenRequestedAssigned", new Object[]{account});
			queue.publish(event);
			return account;
		}
		return account;
	}




	public Account handleInitialTokenEvent(Event ev) {
		var account = ev.getArgument(0, Account.class);
		Integer tokensRequested = ev.getArgument(1,Integer.class);
        // 6 tokens
		List<Token> tokenList = new ArrayList<>();
		for (int i = 0; i < tokensRequested; i++) {
			tokenList.add(generateRandomToken());
		}
		addTokensToAccount(account, tokenList);

		Event event = new Event("InitialTokensAssigned", new Object[]{account});
		queue.publish(event);
		return account;
	}

	private void addTokensToAccount(Account account, List<Token> tokenList) {
		account.setTokens(tokenList);
		for (Token token : tokenList) {
			tokenRepo.addToken(token, account.getAccountId());
		}
	}

	private Token generateRandomToken() {
		Random r = new Random();

		// Copilot generated this code
		String randomString = r.ints(48, 122)
				.filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
				.mapToObj(i -> (char) i)
				.limit(10)
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();


		return new Token(randomString);
	}

	public void handlePaymentRequestSent(Event ev) {
		var payment = ev.getArgument(0, Payment.class);
		new Thread(()-> concurrentHandlePaymentRequest(payment)).start();
	}
	public void concurrentHandlePaymentRequest(Payment payment){
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
