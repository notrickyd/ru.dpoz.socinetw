package ru.dpoz.socnetw.response;

import org.springframework.http.HttpStatus;

public class OkResponseEntity extends BasicResponseEntity
{
    public OkResponseEntity(String msg, Object data)
    {
        super(new BasicResponse(msg, HttpStatus.OK.value(), data), HttpStatus.OK);
    }
}
