package ru.dpoz.socnetw.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class BasicResponseEntity extends ResponseEntity<BasicResponse>
{
    public BasicResponseEntity(BasicResponse entity, HttpStatus status)
    {
        super(entity, status);
    }

}
