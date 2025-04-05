package dev.felix2000jp.springboottemplate.appusers.domain;

import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Identity;
import org.jmolecules.ddd.types.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppuserRepository extends Repository<Appuser, Identity> {

    List<Appuser> findAll(int pageNumber);

    Optional<Appuser> findById(UUID id);

    Optional<Appuser> findByUsername(String username);

    void deleteById(UUID id);

    void save(Appuser appuser);

}
