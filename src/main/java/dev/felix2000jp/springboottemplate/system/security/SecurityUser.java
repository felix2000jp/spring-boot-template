package dev.felix2000jp.springboottemplate.system.security;

import org.jspecify.annotations.NonNull;
import org.springframework.modulith.NamedInterface;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@NamedInterface
public record SecurityUser(
        UUID id,
        String username,
        String password,
        Set<SecurityScope> scopes
) implements UserDetails {

    @Override
    public @NonNull String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public @NonNull Collection<SecurityScope> getAuthorities() {
        return scopes;
    }

}
