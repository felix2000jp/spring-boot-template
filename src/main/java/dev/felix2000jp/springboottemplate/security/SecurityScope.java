package dev.felix2000jp.springboottemplate.security;

import org.springframework.security.core.GrantedAuthority;

public enum SecurityScope implements GrantedAuthority {

    ADMIN,
    APPLICATION;

    @Override
    public String getAuthority() {
        return "SCOPE_" + name();
    }

}
