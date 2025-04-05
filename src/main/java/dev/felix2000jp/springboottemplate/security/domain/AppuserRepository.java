package dev.felix2000jp.springboottemplate.security.domain;

import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Identity;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Username;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface AppuserRepository extends Repository<Appuser, Identity> {

    Optional<Appuser> findById(Identity id);

    Optional<Appuser> findByUsername(Username username);

    void delete(Appuser id);

    void save(Appuser appuser);

}
