package controller;

import model.Tag;
import repository.implementations.GsonPostRepositoryImpl;
import repository.implementations.GsonTagRepositoryImpl;
import repository.implementations.GsonWriterRepositoryImpl;
import repository.interfaces.PostRepository;
import repository.interfaces.TagRepository;
import repository.interfaces.WriterRepository;

import java.util.stream.Stream;

public class TagController{

    TagRepository tr = new GsonTagRepositoryImpl();

    PostRepository pr = new GsonPostRepositoryImpl();

    WriterRepository wr = new GsonWriterRepositoryImpl();

    public void add(Tag t) {
        tr.add(t);
    }

    public Long add(String subject) {
        return tr.add(subject);
    }

    public Long getFreeId() {
        return tr.getFreeId();
    }

    public Tag getById(long id) {
        return tr.getById(id);
    }

    public Tag getByName(String name) {
        return tr.readDefaultStream().filter(n -> n.getName().equals(name)).findAny().get();
    }

    public Stream<Tag> getAll() {
        return tr.readDefaultStream();
    }

    public void update(Tag t) {
        tr.update(t);
        pr.update(t);
        wr.update(t);
    }

    public void delete(Tag t) {
        tr.delete(t.getId());
        pr.delete(t);
        wr.delete(t);
    }

    public boolean contains(Long id) {
        return tr.contains(id);
    }

    public boolean contains(String tagName) {
        return tr.contains(tagName);
    }
}
