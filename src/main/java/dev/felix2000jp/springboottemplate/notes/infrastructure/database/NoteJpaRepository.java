package dev.felix2000jp.springboottemplate.notes.infrastructure.database;

import dev.felix2000jp.springboottemplate.notes.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface NoteJpaRepository extends JpaRepository<Note, UUID> {

    List<Note> findAllByAppuserId(UUID appuserId);

    long countByAppuserId(UUID appuserId);

    Optional<Note> findByIdAndAppuserId(UUID id, UUID appuserId);

    void deleteAllByAppuserId(UUID appuserId);

}
