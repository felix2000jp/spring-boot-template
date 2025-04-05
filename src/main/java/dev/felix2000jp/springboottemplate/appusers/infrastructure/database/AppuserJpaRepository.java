package dev.felix2000jp.springboottemplate.appusers.infrastructure.database;

import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Identity;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface AppuserJpaRepository extends JpaRepository<Appuser, Identity> {

    Optional<Appuser> findByUsername(Username username);

}