package token.service;

import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;

public class StartUp {
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		System.out.println("startup");
		MessageQueue mq = new RabbitMqQueue("rabbitMq");
		new TokenService(mq);
	}
}
