package ru.dpoz.socinetw.repository.intf;

import ru.dpoz.socinetw.model.HobbyEntity;
import ru.dpoz.socinetw.model.UserHobbiesEntity;

import java.util.List;
import java.util.UUID;

public interface UserHobbies
{
    void add(UUID userId, int hobbyId);
    void addAll(List<UserHobbiesEntity> usersHobbies);
    List<HobbyEntity> get(UUID userId);
}
