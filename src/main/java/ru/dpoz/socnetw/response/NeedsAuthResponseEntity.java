package ru.dpoz.socnetw.response;

import org.springframework.http.HttpStatus;

public class NeedsAuthResponseEntity extends BasicResponseEntity
{
    public NeedsAuthResponseEntity()
    {
        super(new BasicResponse(
                "Login and/or password doesnt match",
                HttpStatus.UNAUTHORIZED.value(),
                null),
                HttpStatus.UNAUTHORIZED);
    }
}
