package controller;

import model.Post;
import model.Tag;
import model.Writer;
import repository.implementations.GsonWriterRepositoryImpl;
import repository.interfaces.WriterRepository;

import java.util.Optional;
import java.util.stream.Stream;

public class WriterController {

    WriterRepository wr = new GsonWriterRepositoryImpl();

    public void add(Writer w) {
        wr.add(w);
    }

    public Long add(String name) {
        return wr.add(name);
    }

    public Stream<Writer> getAll() {
        return wr.readDefaultStream();
    }

    public Writer getById(Long id) {
        return wr.getById(id);
    }

    public Stream<Post> getWriterPosts(Long id) {
        return wr.getWriterPosts(id);
    }

    public void update(Writer w) {
        wr.update(w);
    }

    public void delete(Long id) {
        wr.delete(id);
    }

    public boolean contains(String writerName) {
        return wr.contain(writerName);
    }

    public boolean contains(Long id) {
        return wr.contains(id);
    }

    public long getFreeId() {
        return wr.getFreeId();
    }

    public Writer getByName(String name) {
        return wr.readDefaultStream()
                .filter(w1 -> w1.getName().equalsIgnoreCase(name))
                .findAny()
                .get();
    }

}
