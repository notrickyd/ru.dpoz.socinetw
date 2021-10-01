package ru.dpoz.socnetw.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.dpoz.socnetw.model.UserSecretEntity;

import java.util.Collection;

@Getter
public class UserSecretDetails implements UserDetails
{
    private UserSecretEntity userSecretEntity;

    public UserSecretDetails(UserSecretEntity userData)
    {
        this.userSecretEntity = userData;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }

    @Override
    public String getPassword()
    {
        return userSecretEntity.getPassword();
    }

    @Override
    public String getUsername()
    {
        return userSecretEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
