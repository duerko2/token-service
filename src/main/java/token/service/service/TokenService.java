package token.service.service;

import messaging.Message;
import messaging.MessageQueue;
import token.service.aggregate.Payment;
import token.service.aggregate.Token;
import token.service.events.BankAccountRequest;
import token.service.events.PaymentCreated;
import token.service.events.TokensAssigned;
import token.service.events.TokensRequested;
import token.service.repositories.TokenRepo;

import java.util.*;

public class TokenService {

	TokenRepo tokenRepo = new TokenRepo();
	MessageQueue queue;
	public TokenService(MessageQueue q) {
		this.queue = q;
		this.queue.addHandler(PaymentCreated.class, e-> handlePaymentRequestSent((PaymentCreated) e));
		this.queue.addHandler(TokensRequested.class, e-> handleTokensRequested((TokensRequested) e));
	}

	public void handleTokensRequested(TokensRequested m) {
		System.out.println("Tokens requested");
		var accountId = m.getAccountId();
		var numberOfTokens = m.getAmount();
		Set<Token> tokens = new HashSet<>();
		if(numberOfTokens <= 6 && numberOfTokens >= 1 && tokenRepo.getTokenList(accountId.toString()).size()<=1 ){
			tokens = generateTokens(numberOfTokens);
			tokenRepo.addTokens(tokens.stream().toList(), accountId);
		}
		Message message = new TokensAssigned(accountId, tokens);
		queue.publish(message);
	}

	private Set<Token> generateTokens(int numberOfTokens) {
		var tokens = new HashSet<Token>();
		for (int i = 0; i < numberOfTokens; i++) {
			tokens.add(generateRandomToken());
		}
		return tokens;
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
		/*
		//Rasmus generated this code
		String ran = UUID.randomUUID().toString();
		//Andreas generated this code
		String dinMor = "ooga booga";
		 */

		return new Token(randomString);
	}

	public void handlePaymentRequestSent(PaymentCreated m) {
		Payment payment = new Payment();
		payment.setToken(m.getToken());
		payment.setPaymentId(m.getPaymentId());
		payment.setAmount(m.getAmount());
		payment.setMerchantId(m.getMerchantId());

		new Thread(()-> concurrentHandlePaymentRequest(payment)).start();
	}
	public void concurrentHandlePaymentRequest(Payment payment){
		var accountId = tokenRepo.getAccountId(payment.getToken());

		payment.setCustomerId(accountId);
		tokenRepo.deleteToken(payment.getToken());

		Message m = new BankAccountRequest(payment.getPaymentId(),payment.getCustomerId(),payment.getMerchantId(),payment.getAmount());
		queue.publish(m);
	}
	public TokenRepo getTokenRepo(){
		return tokenRepo;
	}

}
