package dev.felix2000jp.springboottemplate.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.UUID;

public interface SecurityService extends UserDetailsService {

    String generateToken(UUID id, String username, Collection<SecurityScope> securityScopes);

    String generateEncodedPassword(String password);

    SecurityUser loadUserFromSecurityContext();

}
