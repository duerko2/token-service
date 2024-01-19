package token.service.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import token.service.aggregate.AccountId;
import token.service.aggregate.Token;

import java.util.Set;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TokensAssigned extends Event{
    private static final long serialVersionUID = -654639399553909420L;
    private AccountId accountId;
    private Set<Token> accountTokens;
    public boolean equals(Object obj) {
        if (obj instanceof TokensAssigned) {
            TokensAssigned other = (TokensAssigned) obj;
            return other.getAccountId().equals(this.getAccountId());
        }
        return false;
    }
}