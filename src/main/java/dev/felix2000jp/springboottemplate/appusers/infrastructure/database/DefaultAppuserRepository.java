package dev.felix2000jp.springboottemplate.appusers.infrastructure.database;

import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import dev.felix2000jp.springboottemplate.appusers.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Identity;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Username;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
class DefaultAppuserRepository implements AppuserRepository {

    private final AppuserJpaRepository appuserJpaRepository;

    DefaultAppuserRepository(AppuserJpaRepository appuserJpaRepository) {
        this.appuserJpaRepository = appuserJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Appuser> findById(Identity id) {
        return appuserJpaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Appuser> findByUsername(Username username) {
        return appuserJpaRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void delete(Appuser appuser) {
        appuserJpaRepository.delete(appuser);
    }

    @Override
    @Transactional
    public void save(Appuser appuser) {
        appuserJpaRepository.save(appuser);
    }

}
