package dev.felix2000jp.springboottemplate.security.infrastructure.database;

import dev.felix2000jp.springboottemplate.security.domain.Appuser;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Identity;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface AppuserJpaRepository extends JpaRepository<Appuser, Identity> {

    Optional<Appuser> findByUsername(Username username);

}