package ru.dpoz.socnetw.repository.intf;

import ru.dpoz.socnetw.model.HobbyEntity;
import ru.dpoz.socnetw.model.UserHobbiesEntity;

import java.util.List;
import java.util.UUID;

public interface UserHobbies
{
    void add(UUID userId, int hobbyId);
    void addAll(List<UserHobbiesEntity> usersHobbies);
    List<HobbyEntity> get(UUID userId);
}
