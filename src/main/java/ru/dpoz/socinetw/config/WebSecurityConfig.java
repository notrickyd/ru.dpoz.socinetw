package ru.dpoz.socinetw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                    .antMatchers("/", "/home", "/signup", "/signupbulk", "/js/**", "/css/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/signin")
                    .loginProcessingUrl("/dosignin")
                    .defaultSuccessUrl("/user")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/signout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/")
                    .permitAll()
                    .and()
                .httpBasic()
                    .and()
                .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());/*.disable();*/
    }

    @Bean
    public static PasswordEncoder passwordEncoder()
    {
            return new BCryptPasswordEncoder();
    }
}
