package ru.dpoz.socinetw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocinetwApplication
{

    public static final Logger logger = LoggerFactory.getLogger(SocinetwApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(SocinetwApplication.class, args);
    }

}
