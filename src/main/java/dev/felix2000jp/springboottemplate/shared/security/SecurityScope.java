package dev.felix2000jp.springboottemplate.shared.security;

import org.springframework.modulith.NamedInterface;
import org.springframework.security.core.GrantedAuthority;

@NamedInterface
public class SecurityScope implements GrantedAuthority {

    private final SecurityRole scope;

    public SecurityScope(SecurityRole scope) {
        this.scope = scope;
    }

    @Override
    public String getAuthority() {
        return "SCOPE_" + scope.name();
    }

}
