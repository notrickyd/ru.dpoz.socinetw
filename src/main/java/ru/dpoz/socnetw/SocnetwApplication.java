package ru.dpoz.socnetw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class SocnetwApplication
{

    public static final Logger logger = LoggerFactory.getLogger(SocnetwApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(SocnetwApplication.class, args);
    }

}
