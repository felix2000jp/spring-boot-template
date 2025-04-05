package dev.felix2000jp.springboottemplate.appusers.application;

import dev.felix2000jp.springboottemplate.appusers.application.dtos.AppuserDto;
import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
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
