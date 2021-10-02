package ru.dpoz.socinetw.repository.intf;

import ru.dpoz.socinetw.model.UserEntity;

import java.util.List;
import java.util.UUID;

public interface User
{
    UUID add(UserEntity user);
    List<UUID> addAll(List<UserEntity> users);
    UserEntity get(UUID userId);
    List<UserEntity> getAll(Integer page);
    int count();
    List<UserEntity> findByName(String first, String last);
}
