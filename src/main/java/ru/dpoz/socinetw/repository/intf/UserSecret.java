package ru.dpoz.socinetw.repository.intf;

import ru.dpoz.socinetw.model.UserSecretEntity;

import java.util.List;
import java.util.UUID;

public interface UserSecret
{
    void add(String username, String password, UUID userId);
    void add(UserSecretEntity userSecret);
    void addAll(List<UserSecretEntity> userSecrets);
    UserSecretEntity findUsername(String username);
}
