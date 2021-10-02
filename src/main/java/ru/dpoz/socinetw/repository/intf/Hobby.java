package ru.dpoz.socinetw.repository.intf;

import ru.dpoz.socinetw.model.HobbyEntity;

import java.util.List;

public interface Hobby
{
    int add(HobbyEntity hobby);
    List<HobbyEntity> getAll();
}
