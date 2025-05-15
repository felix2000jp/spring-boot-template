package dev.felix2000jp.springboottemplate.appusers.infrastructure.database;

import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface AppuserJpaRepository extends JpaRepository<Appuser, UUID> {

    Optional<Appuser> findByUsername(String username);

    boolean existsByUsername(String username);

}