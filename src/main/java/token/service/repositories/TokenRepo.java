package token.service.repositories;

import token.service.aggregate.AccountId;
import token.service.aggregate.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenRepo {


    Map<String, String> tokenIdToAccountIdMap = new ConcurrentHashMap<>();

    public synchronized void addToken(Token token, AccountId accountId) {
        tokenIdToAccountIdMap.put(token.getRfid(), accountId.getUuid().toString());
    }

    public AccountId getAccountId(Token token) {
        try {
            return new AccountId(UUID.fromString(tokenIdToAccountIdMap.get(token.getRfid())));
        } catch(NullPointerException e){
            return null;
        }
    }

    public void deleteToken(Token token) {
        tokenIdToAccountIdMap.remove(token.getRfid());
    }


    public List<Token> getTokenList(String accountId) {
        List<Token> tokens = new ArrayList<>();

        // ChatGPT generated this
        for (Map.Entry<String, String> entry : tokenIdToAccountIdMap.entrySet()) {
            if (entry.getValue().equals(accountId)) {
                tokens.add(new Token(entry.getKey()));
            }
        }

        return tokens;

    }

    public void addTokens(List<Token> tokens, AccountId accountId) {
        for (Token token : tokens) {
            addToken(token, accountId);
        }
    }
}
