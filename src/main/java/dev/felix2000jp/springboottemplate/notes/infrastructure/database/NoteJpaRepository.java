package dev.felix2000jp.springboottemplate.notes.infrastructure.database;

import dev.felix2000jp.springboottemplate.notes.domain.Note;
import dev.felix2000jp.springboottemplate.notes.domain.valueobjects.NoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
interface NoteJpaRepository extends JpaRepository<Note, NoteId> {

    List<Note> findAllByIdAppuserIdValue(UUID appuserId);

    void deleteAllByIdAppuserIdValue(UUID appuserId);

}
