package ru.dpoz.socinetw.repository.intf;

import java.util.UUID;

public interface UserTokens
{
    UUID add(UUID userId);
    boolean checkExpired(UUID token);
}
