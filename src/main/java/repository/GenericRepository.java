package repository;

import java.util.stream.Stream;

public interface GenericRepository<T,ID> {

    Stream<T> readFromFile();

    void writeToFile(Stream<T> incomingStream);

    void add(T object);

    T getById(ID id);

    void update(T object);

    void delete(T object);

}
