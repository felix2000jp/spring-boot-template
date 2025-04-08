package dev.felix2000jp.springboottemplate.security.infrastructure.database;

import dev.felix2000jp.springboottemplate.security.domain.Appuser;
import dev.felix2000jp.springboottemplate.security.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Username;
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
    public boolean existsById(AppuserId id) {
        return appuserJpaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Appuser> findByUsername(Username username) {
        return appuserJpaRepository.findByUsername(username);
    }

    @Override
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
