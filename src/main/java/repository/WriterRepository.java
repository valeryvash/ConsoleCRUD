package repository;

import model.Post;
import model.Writer;

import java.io.File;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public interface WriterRepository extends GenericRepository<Writer, Long> {
    @Override
    Stream<Writer> readFromFile(File toReadFrom, Type token);

    @Override
    void writeToFile(Stream<Writer> incomingTagStream, File toWriteToFile);

    @Override
    Stream<Writer> readDefaultStream();

    @Override
    void writeDefaultStream(Stream<Writer> stream);

    @Override
    void add(Writer w);

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
}
