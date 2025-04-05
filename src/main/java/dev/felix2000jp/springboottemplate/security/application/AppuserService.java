package dev.felix2000jp.springboottemplate.security.application;

import dev.felix2000jp.springboottemplate.security.application.dtos.AppuserDto;
import dev.felix2000jp.springboottemplate.security.application.dtos.CreateAppuserDto;
import dev.felix2000jp.springboottemplate.security.domain.Appuser;
import dev.felix2000jp.springboottemplate.security.domain.AppuserPublisher;
import dev.felix2000jp.springboottemplate.security.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.security.domain.exceptions.AppuserAlreadyExistsException;
import dev.felix2000jp.springboottemplate.security.domain.exceptions.AppuserNotFoundException;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Identity;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Password;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Username;
import dev.felix2000jp.springboottemplate.system.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@NamedInterface
public class AppuserService {

    private static final Logger log = LoggerFactory.getLogger(AppuserService.class);

    private final AppuserRepository appuserRepository;
    private final AppuserPublisher appuserPublisher;
    private final AppuserMapper appuserMapper;
    private final SecurityService securityService;

    AppuserService(
            AppuserRepository appuserRepository,
            AppuserPublisher appuserPublisher,
            AppuserMapper appuserMapper, SecurityService securityService
    ) {
        this.appuserRepository = appuserRepository;
        this.appuserPublisher = appuserPublisher;
        this.appuserMapper = appuserMapper;
        this.securityService = securityService;
    }

    public AppuserDto getAppuserFromUsername(String username) {
        var usernameObject = new Username(username);

        var appuser = appuserRepository
                .findByUsername(usernameObject)
                .orElseThrow(AppuserNotFoundException::new);

        return appuserMapper.toDto(appuser);
    }

    @Transactional
    public void create(CreateAppuserDto createAppuserDto) {
        var idObject = new Identity(UUID.randomUUID());
        var usernameObject = new Username(createAppuserDto.username());
        var passwordObject = new Password(createAppuserDto.password());

        if (appuserRepository.findByUsername(usernameObject).isPresent()) {
            throw new AppuserAlreadyExistsException();
        }

        // TODO: encode password
        var appuser = new Appuser(idObject, usernameObject, passwordObject);

        // TODO: add scope methods
        //appuser.addScopeApplication();

        appuserRepository.save(appuser);
        log.info("Appuser with id {} created", appuser.getId());

        appuserPublisher.publishAppuserCreatedEvent(appuser);
        log.info("Published AppuserCreatedEvent with appuserId {}", appuser.getId());
    }

    @Transactional
    public void delete() {
        var user = securityService.getAuthenticatedUser();
        var idObject = new Identity(user.getId());

        var appuser = appuserRepository
                .findById(idObject)
                .orElseThrow(AppuserNotFoundException::new);

        appuserRepository.delete(appuser);
        log.info("Appuser with id {} deleted", appuser.getId());

        appuserPublisher.publishAppuserDeletedEvent(appuser);
        log.info("Published AppuserDeletedEvent with appuserId {}", appuser.getId());
    }

}
