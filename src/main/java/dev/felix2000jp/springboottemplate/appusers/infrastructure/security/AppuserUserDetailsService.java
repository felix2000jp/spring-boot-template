package dev.felix2000jp.springboottemplate.appusers.infrastructure.security;

import dev.felix2000jp.springboottemplate.appusers.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.appusers.domain.exceptions.AppuserNotFoundException;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Scope;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Username;
import dev.felix2000jp.springboottemplate.system.security.SecurityUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
class AppuserUserDetailsService implements UserDetailsService {

    private final AppuserRepository appuserService;

    AppuserUserDetailsService(AppuserRepository appuserService) {
        this.appuserService = appuserService;
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
