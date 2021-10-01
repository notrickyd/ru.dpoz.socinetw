package ru.dpoz.socnetw.repository.intf;

import ru.dpoz.socnetw.model.HobbyEntity;

import java.util.List;

public interface Hobby
{
    int add(HobbyEntity hobby);
    List<HobbyEntity> getAll();
}
