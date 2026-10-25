package dev.felix2000jp.springboottemplate.system.security;

import org.springframework.modulith.NamedInterface;
import org.springframework.security.core.GrantedAuthority;

@NamedInterface
public enum SecurityScope implements GrantedAuthority {

    ADMIN,
    MEMBER;

    public static final String ADMIN_AUTHORITY = "SCOPE_ADMIN";
    public static final String MEMBER_AUTHORITY = "SCOPE_MEMBER";

    @Override
    public String getAuthority() {
        return "SCOPE_" + name();
    }

}
