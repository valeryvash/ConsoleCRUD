package repository.interfaces;

import model.Tag;

import java.util.stream.Stream;

public interface TagRepository extends GenericRepository<Tag,Long>{

    @Override
    Stream<Tag> readDefaultStream();

    @Override
    void writeDefaultStream(Stream<Tag> stream);

    @Override
    Long getFreeId();

    @Override
    void add(Tag p);

    @Override
    Long add(String subject);

    @Override
    Tag getById(Long aLong);

    @Override
    void update(Tag tag);

    @Override
    void delete(Long aLong);

    @Override
    boolean contains(Long aLong);

    boolean contains(String tagName);
}
