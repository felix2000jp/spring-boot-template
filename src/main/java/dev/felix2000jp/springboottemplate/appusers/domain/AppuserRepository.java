package dev.felix2000jp.springboottemplate.appusers.domain;

import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Username;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;
import java.util.UUID;

public interface AppuserRepository extends Repository<Appuser, AppuserId> {

    Optional<Appuser> findById(AppuserId id);

    boolean existsById(AppuserId id);

    Optional<Appuser> findByUsername(Username username);

    boolean existsByUsername(Username username);

    void delete(Appuser appuser);

    void save(Appuser appuser);

}
