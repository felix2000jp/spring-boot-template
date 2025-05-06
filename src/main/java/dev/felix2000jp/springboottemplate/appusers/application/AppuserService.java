package dev.felix2000jp.springboottemplate.appusers.application;

import dev.felix2000jp.springboottemplate.system.security.SecurityScope;
import dev.felix2000jp.springboottemplate.system.security.SecurityService;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.AppuserDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.CreateAppuserDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.UpdateAppuserDto;
import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import dev.felix2000jp.springboottemplate.appusers.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.appusers.domain.exceptions.AppuserAlreadyExistsException;
import dev.felix2000jp.springboottemplate.appusers.domain.exceptions.AppuserNotFoundException;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Password;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AppuserService {

    private static final Logger log = LoggerFactory.getLogger(AppuserService.class);

    private final AppuserRepository appuserRepository;
    private final AppuserMapper appuserMapper;
    private final SecurityService securityService;

    AppuserService(AppuserRepository appuserRepository, AppuserMapper appuserMapper, SecurityService securityService) {
        this.appuserRepository = appuserRepository;
        this.appuserMapper = appuserMapper;
        this.securityService = securityService;
    }

    public String login() {
        var user = securityService.loadUserFromSecurityContext();

        return securityService.generateToken(
                user.getId(),
                user.getUsername(),
                user.getAuthorities()
        );
    }

    @Transactional(readOnly = true)
    public AppuserDto get() {
        var user = securityService.loadUserFromSecurityContext();
        var idValueObject = new AppuserId(user.getId());

        var appuser = appuserRepository
                .findById(idValueObject)
                .orElseThrow(AppuserNotFoundException::new);

        return appuserMapper.toDto(appuser);
    }

    @Transactional
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

    @Transactional
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

        appuserToDelete.delete();
        appuserRepository.delete(appuserToDelete);
        log.info("Appuser with id {} deleted", appuserToDelete.getId());
    }

}
