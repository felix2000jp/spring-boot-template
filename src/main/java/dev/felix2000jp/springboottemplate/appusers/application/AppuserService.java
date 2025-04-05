package dev.felix2000jp.springboottemplate.appusers.application;


import dev.felix2000jp.springboottemplate.appusers.application.dtos.CreateAppuserDto;
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
        var appuser = Appuser.from(UUID.randomUUID(), createAppuserDto.username(), createAppuserDto.password());
        appuser.addScopeApplication();

        if (appuserRepository.findByUsername(appuser.getUsername()).isPresent()) {
            throw new AppuserAlreadyExistsException();
        }

        appuserRepository.save(appuser);
        log.info("Appuser with id {} created", appuser.getId());

        appuserPublisher.publishAppuserCreatedEvent(appuser);
        log.info("Published AppuserCreatedEvent with appuserId {}", appuser.getId());
    }

    @Transactional
    public void delete() {
        var user = securityService.loadUserFromToken();

        var appuser = appuserRepository
                .findById(new AppuserId(user.getId()))
                .orElseThrow(AppuserNotFoundException::new);

        appuserRepository.delete(appuser);
        log.info("Appuser with id {} deleted", appuser.getId());

        appuserPublisher.publishAppuserDeletedEvent(appuser);
        log.info("Published AppuserDeletedEvent with appuserId {}", appuser.getId());
    }

}
