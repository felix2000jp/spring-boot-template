package dev.felix2000jp.springboottemplate.security.infrastructure.security;

import dev.felix2000jp.springboottemplate.security.SecurityScope;
import dev.felix2000jp.springboottemplate.security.SecurityService;
import dev.felix2000jp.springboottemplate.security.SecurityUser;
import dev.felix2000jp.springboottemplate.security.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.security.domain.exceptions.AppuserNotFoundException;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Scope;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Username;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
class AppuserSecurityService implements SecurityService {

    private static final String ID_CLAIM_NAME = "id";
    private static final String SCOPE_CLAIM_NAME = "scope";

    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;
    private final AppuserRepository appuserService;

    AppuserSecurityService(JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder, AppuserRepository appuserService) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.appuserService = appuserService;
    }

    @Override
    public String generateToken(UUID id, String username, Collection<SecurityScope> securityScopes) {
        var now = Instant.now();
        var expiration = now.plus(4, ChronoUnit.HOURS);

        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .subject(username)
                .claim(ID_CLAIM_NAME, id.toString())
                .claim(SCOPE_CLAIM_NAME, String.join(" ", securityScopes.stream().map(Enum::name).toList()))
                .issuedAt(now)
                .expiresAt(expiration)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public String generateEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public SecurityUser loadUserFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = authentication.getPrincipal();

        if (principal instanceof SecurityUser securityUser) {
            return securityUser;
        }

        if (principal instanceof Jwt jwt) {
            var idClaim = jwt.getClaimAsString(ID_CLAIM_NAME);
            var id = UUID.fromString(idClaim);

            var scopeClaims = jwt.getClaimAsString(SCOPE_CLAIM_NAME).split(" ");
            var scopes = Arrays
                    .stream(scopeClaims)
                    .map(SecurityScope::valueOf)
                    .collect(Collectors.toSet());

            return new SecurityUser(id, jwt.getSubject(), "not available", scopes);
        }

        throw new AccessDeniedException("Invalid authentication method");
    }

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        var usernameValueObject = new Username(username);

        var appuser = appuserService
                .findByUsername(usernameValueObject)
                .orElseThrow(AppuserNotFoundException::new);

        return new SecurityUser(
                appuser.getId().value(),
                appuser.getUsername().value(),
                appuser.getPassword().value(),
                appuser.getScopes().stream().map(Scope::value).collect(Collectors.toSet())
        );
    }

}
