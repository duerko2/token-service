package token.service.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import token.service.aggregate.AccountId;
import token.service.aggregate.PaymentId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BankAccountRequest extends Event{
    PaymentId paymentId;
    private AccountId customerId;
    private AccountId merchantId;
    private int amount;

    @Override
    public AccountId getAccountId() {
        return null;
    }
}
