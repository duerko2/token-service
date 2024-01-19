package token.service.events;

import lombok.*;
import token.service.aggregate.AccountId;
import token.service.aggregate.PaymentId;
import token.service.aggregate.Token;

@Getter
@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaymentCreated extends Event{

    private static final long serialVersionUID = -1;
    private PaymentId paymentId;

    private int amount;

    private Token token;

    private AccountId merchantId;

    @Override
    public AccountId getAccountId() {
        return null;
    }
}