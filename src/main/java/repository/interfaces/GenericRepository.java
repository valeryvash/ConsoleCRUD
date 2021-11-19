package repository.interfaces;

import java.util.stream.Stream;

public interface GenericRepository<T,ID> {

    Stream<T> readDefaultStream();

    void writeDefaultStream(Stream<T> stream);

    ID getFreeId();

    ID add(String subject);

    void add(T p);

    T getById(ID id);

    void update(T t);

    void delete(ID id);

    boolean contains(ID id);
}
