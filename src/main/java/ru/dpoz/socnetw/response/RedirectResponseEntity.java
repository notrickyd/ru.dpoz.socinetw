package ru.dpoz.socnetw.response;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class RedirectResponseEntity extends BasicResponseEntity
{
    public static HashMap<String, String> getData(String url)
    {
        HashMap<String, String> data = new LinkedHashMap<>();
        data.put("url", url);
        return data;
    }

    public RedirectResponseEntity(String url)
    {
        super(new BasicResponse("Перенаправление", HttpStatus.FOUND.value(), getData(url)),
                HttpStatus.FOUND
        );
    }

}
