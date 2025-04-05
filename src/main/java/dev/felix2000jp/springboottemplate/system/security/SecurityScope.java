package dev.felix2000jp.springboottemplate.system.security;

import org.springframework.modulith.NamedInterface;

@NamedInterface
public enum SecurityScope {

    ADMIN,
    APPLICATION;

    public String toAuthority() {
        return "SCOPE_" + name();
    }

}
