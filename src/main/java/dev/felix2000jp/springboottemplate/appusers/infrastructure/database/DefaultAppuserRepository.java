package dev.felix2000jp.springboottemplate.appusers.infrastructure.database;

import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import dev.felix2000jp.springboottemplate.appusers.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.AppuserId;
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
    public Optional<Appuser> findById(AppuserId id) {
        return appuserJpaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(AppuserId id) {
        return appuserJpaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Appuser> findByUsername(Username username) {
        return appuserJpaRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(Username username) {
        return appuserJpaRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public void delete(Appuser appuser) {
        appuserJpaRepository.delete(appuser);
    }

    @Override
    @Transactional
    public void deleteAll() {
        appuserJpaRepository.deleteAll();
    }

    @Override
    @Transactional
    public void save(Appuser appuser) {
        appuserJpaRepository.save(appuser);
    }

}
