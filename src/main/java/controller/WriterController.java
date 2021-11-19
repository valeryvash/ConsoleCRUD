package controller;

import model.Post;
import model.Tag;
import model.Writer;
import repository.implementations.GsonWriterRepositoryImpl;
import repository.interfaces.WriterRepository;

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

    public void update(Tag t1) {
        wr.writeDefaultStream(
                wr.readDefaultStream()
                        .map(w1 ->{
                            if (w1.contains(t1)){
                                w1.update(t1);
                            }
                            return w1;
                        })
        );
    }
}
