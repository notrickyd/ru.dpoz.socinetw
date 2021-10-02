package ru.dpoz.socinetw.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BasicResponse
{
    String msg;
    int code;
    Object data;
}
