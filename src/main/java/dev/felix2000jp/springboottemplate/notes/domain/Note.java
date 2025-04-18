package dev.felix2000jp.springboottemplate.notes.domain;

import dev.felix2000jp.springboottemplate.notes.domain.valueobjects.Content;
import dev.felix2000jp.springboottemplate.notes.domain.valueobjects.NoteId;
import dev.felix2000jp.springboottemplate.notes.domain.valueobjects.Title;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import org.jmolecules.ddd.types.AggregateRoot;

import java.util.UUID;

@jakarta.persistence.Table(name = "note")
@jakarta.persistence.Entity
public class Note implements AggregateRoot<Note, NoteId> {

    @EmbeddedId
    @AttributeOverride(name = "appuserIdValue", column = @Column(name = "appuser_id"))
    @AttributeOverride(name = "noteIdValue", column = @Column(name = "id"))
    private NoteId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title"))
    private Title title;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "content"))
    private Content content;

    protected Note() {
    }

    protected Note(NoteId id, Title title, Content content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static Note from(UUID appuserId, UUID noteId, String title, String content) {
        return new Note(
                new NoteId(appuserId, noteId),
                new Title(title),
                new Content(content)
        );
    }

    @Override
    public NoteId getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}