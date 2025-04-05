package dev.felix2000jp.springboottemplate.security.application;

import dev.felix2000jp.springboottemplate.security.application.dtos.AppuserDto;
import dev.felix2000jp.springboottemplate.security.domain.Appuser;
import org.springframework.stereotype.Component;

@Component
class AppuserMapper {

    AppuserDto toDto(Appuser appuser) {
        return new AppuserDto(
                appuser.getId().value(),
                appuser.getUsername().value(),
                appuser.getScopes().stream().map(s -> s.getScope().value()).toList()
        );
    }

}
