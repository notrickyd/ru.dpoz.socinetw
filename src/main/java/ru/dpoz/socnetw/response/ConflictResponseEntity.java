package ru.dpoz.socnetw.response;

import org.springframework.http.HttpStatus;

public class ConflictResponseEntity extends BasicResponseEntity
{
    public ConflictResponseEntity(String msg, Object data)
    {
        super(new BasicResponse(msg,-1, data), HttpStatus.CONFLICT);
    }
}
