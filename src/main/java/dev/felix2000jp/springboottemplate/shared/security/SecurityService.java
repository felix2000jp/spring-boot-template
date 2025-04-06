package dev.felix2000jp.springboottemplate.shared.security;

import org.springframework.modulith.NamedInterface;
import org.springframework.security.core.userdetails.UserDetailsService;

@NamedInterface
public interface SecurityService extends UserDetailsService {

    String generateToken(String subject, String idClaimValue, String scopeClaimValue);

    String generateEncodedPassword(String password);

    SecurityUser loadUserFromSecurityContext();

}
