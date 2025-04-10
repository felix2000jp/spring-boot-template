package dev.felix2000jp.springboottemplate.security.application;

import dev.felix2000jp.springboottemplate.security.SecurityScope;
import dev.felix2000jp.springboottemplate.security.SecurityService;
import dev.felix2000jp.springboottemplate.security.application.dtos.AppuserDto;
import dev.felix2000jp.springboottemplate.security.application.dtos.CreateAppuserDto;
import dev.felix2000jp.springboottemplate.security.application.dtos.UpdateAppuserDto;
import dev.felix2000jp.springboottemplate.security.domain.Appuser;
import dev.felix2000jp.springboottemplate.security.domain.AppuserPublisher;
import dev.felix2000jp.springboottemplate.security.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.security.domain.exceptions.AppuserAlreadyExistsException;
import dev.felix2000jp.springboottemplate.security.domain.exceptions.AppuserNotFoundException;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Password;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AppuserService {

    private static final Logger log = LoggerFactory.getLogger(AppuserService.class);

    private final AppuserRepository appuserRepository;
    private final AppuserPublisher appuserPublisher;
    private final AppuserMapper appuserMapper;
    private final SecurityService securityService;

    AppuserService(
            AppuserRepository appuserRepository,
            AppuserPublisher appuserPublisher,
            AppuserMapper appuserMapper,
            SecurityService securityService
    ) {
        this.appuserRepository = appuserRepository;
        this.appuserPublisher = appuserPublisher;
        this.appuserMapper = appuserMapper;
        this.securityService = securityService;
    }

    public AppuserDto get() {
        var user = securityService.loadUserFromSecurityContext();
        var idValueObject = new AppuserId(user.getId());

        var appuser = appuserRepository
                .findById(idValueObject)
                .orElseThrow(AppuserNotFoundException::new);

        return appuserMapper.toDto(appuser);
    }

    public void create(CreateAppuserDto createAppuserDto) {
        var appuserToCreate = Appuser.from(
                UUID.randomUUID(),
                createAppuserDto.username(),
                securityService.generateEncodedPassword(createAppuserDto.password()),
                SecurityScope.APPLICATION
        );

        var doesUsernameExist = appuserRepository.existsByUsername(appuserToCreate.getUsername());
        if (doesUsernameExist) {
            throw new AppuserAlreadyExistsException();
        }

        appuserRepository.save(appuserToCreate);
        log.info("Appuser with id {} created", appuserToCreate.getId());
    }

    public void update(UpdateAppuserDto updateAppuserDto) {
        var user = securityService.loadUserFromSecurityContext();
        var idValueObject = new AppuserId(user.getId());
        var usernameValueObject = new Username(updateAppuserDto.username());
        var passwordValueObject = new Password(securityService.generateEncodedPassword(updateAppuserDto.password()));

        var appuserToUpdate = appuserRepository
                .findById(idValueObject)
                .orElseThrow(AppuserNotFoundException::new);

        var isUsernameNew = !usernameValueObject.equals(appuserToUpdate.getUsername());
        var doesUsernameExist = appuserRepository.existsByUsername(usernameValueObject);

        if (isUsernameNew && doesUsernameExist) {
            throw new AppuserAlreadyExistsException();
        }

        appuserToUpdate.setUsername(usernameValueObject);
        appuserToUpdate.setPassword(passwordValueObject);
        appuserRepository.save(appuserToUpdate);
        log.info("Appuser with id {} updated", appuserToUpdate.getId());
    }

    @Transactional
    public void delete() {
        var user = securityService.loadUserFromSecurityContext();
        var idValueObject = new AppuserId(user.getId());

        var appuserToDelete = appuserRepository
                .findById(idValueObject)
                .orElseThrow(AppuserNotFoundException::new);

        appuserRepository.delete(appuserToDelete);
        log.info("Appuser with id {} deleted", appuserToDelete.getId());

        appuserPublisher.publishAppuserDeletedEvent(appuserToDelete);
        log.info("Published AppuserDeletedEvent with appuserId {}", appuserToDelete.getId());
    }

    public String login() {
        var securityUser = securityService.loadUserFromSecurityContext();

        return securityService.generateToken(
                securityUser.getId(),
                securityUser.getUsername(),
                securityUser.getAuthorities()
        );
    }
}
