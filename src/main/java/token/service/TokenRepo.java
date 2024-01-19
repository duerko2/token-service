package token.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @Author: Nikolaj Beier
 * Mob programming, all members
 */
public class TokenRepo {


    Map<String, String> tokenIdToAccountIdMap = new ConcurrentHashMap<>();

    public synchronized void addToken(Token token, String accountId) {
        tokenIdToAccountIdMap.put(token.getRfid(), accountId);
    }

    public String getAccountId(Token token) {
        return tokenIdToAccountIdMap.get(token.getRfid());
    }

    public void deleteToken(Token token) {
        tokenIdToAccountIdMap.remove(token.getRfid());

    }


    public List<Token> getTokenList(String accountId) {
        List<Token> tokens = new ArrayList<>();
        for (Map.Entry<String, String> entry : tokenIdToAccountIdMap.entrySet()) {
            if (entry.getValue().equals(accountId)) {
                tokens.add(new Token(entry.getKey()));
            }
        }

        return tokens;

    }
}
