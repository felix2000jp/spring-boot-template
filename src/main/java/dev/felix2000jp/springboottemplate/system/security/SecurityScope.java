package dev.felix2000jp.springboottemplate.system.security;

import org.springframework.modulith.NamedInterface;
import org.springframework.security.core.GrantedAuthority;

@NamedInterface
public enum SecurityScope implements GrantedAuthority {

    ADMIN(SecurityScope.ADMIN_AUTHORITY),
    MEMBER(SecurityScope.MEMBER_AUTHORITY);

    public static final String ADMIN_AUTHORITY = "SCOPE_ADMIN";
    public static final String MEMBER_AUTHORITY = "SCOPE_MEMBER";

    private final String authority;

    SecurityScope(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

}
