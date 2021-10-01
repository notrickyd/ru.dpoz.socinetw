package ru.dpoz.socnetw.response;

import org.springframework.http.HttpStatus;

public class NotFoundResponseEntity extends BasicResponseEntity
{
    public NotFoundResponseEntity(String msg, Object data)
    {
        super(new BasicResponse(msg,-1, data), HttpStatus.NOT_FOUND);
    }
}
