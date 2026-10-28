package dev.felix2000jp.springboottemplate.notes.domain;

import dev.felix2000jp.springboottemplate.notes.domain.valueobjects.NoteId;
import org.jmolecules.ddd.types.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends Repository<Note, NoteId> {

    List<Note> findAllByIdAppuserIdValue(UUID appuserIdValue);

    long countByIdAppuserIdValue(UUID appuserIdValue);

    Optional<Note> findById(NoteId id);

    void delete(Note note);

    void deleteAll();

    void deleteAllByIdAppuserIdValue(UUID appuserIdValue);

    void save(Note note);

}
