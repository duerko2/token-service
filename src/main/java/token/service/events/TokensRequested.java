package token.service.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import token.service.aggregate.AccountId;


@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokensRequested extends Event{
    private static final long serialVersionUID = 8022595332951627022L;
    private AccountId accountId;
    private int amount;
}

