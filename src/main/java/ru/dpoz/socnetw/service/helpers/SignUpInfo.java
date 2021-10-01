package ru.dpoz.socnetw.service.helpers;

import lombok.Getter;
import lombok.Setter;
import ru.dpoz.socnetw.model.UserEntity;
import ru.dpoz.socnetw.model.UserSecretEntity;

import java.util.List;

@Getter
@Setter
public class SignUpInfo
{
    UserEntity user;
    List<Integer> hobbies;
    UserSecretEntity secret;
}
