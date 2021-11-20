package repository.interfaces;

import model.Post;
import model.Tag;
import model.Writer;

import java.util.stream.Stream;

public interface WriterRepository extends GenericRepository<Writer, Long> {

    @Override
    Stream<Writer> readDefaultStream();

    @Override
    void writeDefaultStream(Stream<Writer> stream);

    @Override
    Long getFreeId();

    @Override
    void add(Writer w);

    @Override
    Long add(String subject);

    @Override
    Writer getById(Long id);

    @Override
    void update(Writer w);

    @Override
    void delete(Long id);

    @Override
    boolean contains(Long id);

    Writer getByName(String writerName);

    boolean contain(String writerName);

    Stream<Post> getWriterPosts(Long writerId);

    void update(Tag t1);

    void delete(Tag t1);

    boolean contains(String writerName);

    void delete(Post p1);
}
