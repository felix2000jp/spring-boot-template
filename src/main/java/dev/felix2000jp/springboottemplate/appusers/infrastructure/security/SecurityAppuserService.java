package dev.felix2000jp.springboottemplate.appusers.infrastructure.security;

import dev.felix2000jp.springboottemplate.appusers.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.appusers.domain.exceptions.AppuserNotFoundException;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Username;
import dev.felix2000jp.springboottemplate.shared.security.SecurityRole;
import dev.felix2000jp.springboottemplate.shared.security.SecurityScope;
import dev.felix2000jp.springboottemplate.shared.security.SecurityService;
import dev.felix2000jp.springboottemplate.shared.security.SecurityUser;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
class SecurityAppuserService implements SecurityService {

    private static final String ID_CLAIM_NAME = "id";
    private static final String SCOPE_CLAIM_NAME = "scope";

    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;
    private final AppuserRepository appuserService;

    SecurityAppuserService(JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder, AppuserRepository appuserService) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.appuserService = appuserService;
    }

    @Override
    public String generateToken(String subject, String idClaimValue, String scopeClaimValue) {
        var now = Instant.now();
        var expiration = now.plus(12, ChronoUnit.HOURS);

        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .subject(subject)
                .claim(ID_CLAIM_NAME, idClaimValue)
                .claim(SCOPE_CLAIM_NAME, scopeClaimValue)
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
    public SecurityUser loadUserFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            var idClaim = jwt.getClaimAsString(ID_CLAIM_NAME);
            var id = UUID.fromString(idClaim);

            var scopeClaims = jwt.getClaimAsString(SCOPE_CLAIM_NAME).split(" ");
            var scopes = Arrays
                    .stream(scopeClaims)
                    .map(SecurityRole::valueOf)
                    .map(SecurityScope::new)
                    .collect(Collectors.toSet());

            return new SecurityUser(id, jwt.getSubject(), "not available", scopes);
        }

        throw new AccessDeniedException("Only JWT authenticated users can access this resource");
    }

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        var usernameObject = new Username(username);
        var appuser = appuserService
                .findByUsername(usernameObject)
                .orElseThrow(AppuserNotFoundException::new);

        return new SecurityUser(
                appuser.getId().value(),
                appuser.getUsername().value(),
                appuser.getPassword().value(),
                appuser.getScopes().value().stream().map(SecurityScope::new).collect(Collectors.toSet()));
    }

}
