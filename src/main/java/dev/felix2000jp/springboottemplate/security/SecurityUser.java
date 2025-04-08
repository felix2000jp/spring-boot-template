package dev.felix2000jp.springboottemplate.security;

import org.springframework.modulith.NamedInterface;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;

@NamedInterface
public class SecurityUser implements UserDetails {

    private final UUID id;
    private final String username;
    private final String password;
    private final Set<SecurityScope> scopes;

    public SecurityUser(UUID id, String username, String password, SecurityScope scope) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.scopes = Set.of(scope);
    }

    public SecurityUser(UUID id, String username, String password, Set<SecurityScope> scopes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.scopes = scopes;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Set<SecurityScope> getAuthorities() {
        return scopes;
    }

}
