package dev.felix2000jp.springboottemplate.shared.security;

import org.springframework.modulith.NamedInterface;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.UUID;

@NamedInterface
public interface SecurityService extends UserDetailsService {

    String generateToken(UUID id, String username, Collection<SecurityScope> securityScopes);

    String generateEncodedPassword(String password);

    SecurityUser loadUserFromSecurityContext();

}
