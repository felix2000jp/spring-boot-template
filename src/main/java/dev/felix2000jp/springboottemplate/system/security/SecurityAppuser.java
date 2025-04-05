package dev.felix2000jp.springboottemplate.system.security;

import org.springframework.modulith.NamedInterface;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;

@NamedInterface
public class SecurityAppuser implements UserDetails {

    private UUID id;

    private String username;

    private String password;

    private Set<SecurityScope> scopes;

    public SecurityAppuser(UUID id, String username, String password, Set<SecurityScope> scopes) {
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
