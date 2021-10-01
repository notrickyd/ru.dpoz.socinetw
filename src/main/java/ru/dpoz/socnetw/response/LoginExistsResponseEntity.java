package ru.dpoz.socnetw.response;

import org.springframework.http.HttpStatus;

public class LoginExistsResponseEntity extends BasicResponseEntity
{
    public LoginExistsResponseEntity(String login)
    {
        super(new BasicResponse(String.format("Логин '%s' уже используется", login),-1, null), HttpStatus.CONFLICT);
    }
}
