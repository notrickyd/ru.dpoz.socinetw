package ru.dpoz.socinetw.repository.intf;

import ru.dpoz.socinetw.model.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserFriends
{
    boolean add(UUID userId, UUID friendId);
    void remove(UUID userId, UUID friendId);
    List<UUID> get(UUID userId);

    List<UserEntity> getUsers(UUID userId);
}
