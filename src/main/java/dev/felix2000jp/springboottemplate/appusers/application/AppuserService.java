package dev.felix2000jp.springboottemplate.appusers.application;


import dev.felix2000jp.springboottemplate.appusers.application.dtos.CreateAppuserDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.UpdateAppuserDto;
import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import dev.felix2000jp.springboottemplate.appusers.domain.AppuserPublisher;
import dev.felix2000jp.springboottemplate.appusers.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.appusers.domain.exceptions.AppuserAlreadyExistsException;
import dev.felix2000jp.springboottemplate.appusers.domain.exceptions.AppuserNotFoundException;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.shared.security.SecurityService;
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

    @Transactional
    public void create(CreateAppuserDto createAppuserDto) {
        var appuserToCreate = Appuser.from(
                UUID.randomUUID(),
                createAppuserDto.username(),
                securityService.generateEncodedPassword(createAppuserDto.password())
        );
        appuserToCreate.addScopeApplication();

        var doesUsernameExist = appuserRepository.existsByUsername(appuserToCreate.getUsername());
        if (doesUsernameExist) {
            throw new AppuserAlreadyExistsException();
        }

        appuserRepository.save(appuserToCreate);
        log.info("Appuser with id {} created", appuserToCreate.getId());

        appuserPublisher.publishAppuserCreatedEvent(appuserToCreate);
        log.info("Published AppuserCreatedEvent with appuserId {}", appuserToCreate.getId());
    }

    public void update(UpdateAppuserDto updateAppuserDto) {
        var user = securityService.loadUserFromToken();

        var updatedAppuser = Appuser.from(
                user.getId(),
                updateAppuserDto.username(),
                securityService.generateEncodedPassword(updateAppuserDto.password())
        );

        var appuserToUpdate = appuserRepository
                .findById(updatedAppuser.getId())
                .orElseThrow(AppuserNotFoundException::new);

        var isUsernameNew = !updatedAppuser.getUsername().equals(appuserToUpdate.getUsername());
        var doesUsernameExist = appuserRepository.existsByUsername(updatedAppuser.getUsername());

        if (isUsernameNew && doesUsernameExist) {
            throw new AppuserAlreadyExistsException();
        }

        appuserToUpdate.updateUsername(updatedAppuser.getUsername());
        appuserToUpdate.updatePassword(updatedAppuser.getPassword());
        appuserRepository.save(appuserToUpdate);
        log.info("Appuser with id {} updated", appuserToUpdate.getId());
    }

    @Transactional
    public void delete() {
        var user = securityService.loadUserFromToken();

        var appuserToDelete = appuserRepository
                .findById(new AppuserId(user.getId()))
                .orElseThrow(AppuserNotFoundException::new);

        appuserRepository.delete(appuserToDelete);
        log.info("Appuser with id {} deleted", appuserToDelete.getId());

        appuserPublisher.publishAppuserDeletedEvent(appuserToDelete);
        log.info("Published AppuserDeletedEvent with appuserId {}", appuserToDelete.getId());
    }

}
