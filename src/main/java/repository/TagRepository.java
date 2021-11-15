package repository;

import model.Tag;

import java.io.File;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public interface TagRepository extends GenericRepository<Tag,Long>{
    @Override
    Stream<Tag> readFromFile(File toReadFrom, Type token);

    @Override
    void writeToFile(Stream<Tag> incomingTagStream, File toWriteToFile);

    @Override
    Stream<Tag> readDefaultStream();

    @Override
    void writeDefaultStream(Stream<Tag> stream);

    @Override
    void add(Tag p);

    @Override
    Tag getById(Long aLong);

    @Override
    void update(Tag tag);

    @Override
    void delete(Long aLong);

    @Override
    boolean contains(Long aLong);
}
