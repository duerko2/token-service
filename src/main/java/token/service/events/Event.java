package token.service.events;

import lombok.Getter;
import messaging.Message;
import token.service.aggregate.AccountId;

import java.io.Serializable;

public abstract class Event implements Message, Serializable {

    private static final long serialVersionUID = -8571080289905090781L;

    private static long versionCount = 1;

    @Getter
    private final long version = versionCount++;

    public abstract AccountId getAccountId();
}