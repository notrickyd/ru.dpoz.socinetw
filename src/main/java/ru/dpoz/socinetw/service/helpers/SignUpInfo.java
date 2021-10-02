package ru.dpoz.socinetw.service.helpers;

import lombok.Getter;
import lombok.Setter;
import ru.dpoz.socinetw.model.UserEntity;
import ru.dpoz.socinetw.model.UserSecretEntity;

import java.util.List;

@Getter
@Setter
public class SignUpInfo
{
    UserEntity user;
    List<Integer> hobbies;
    UserSecretEntity secret;
}
