package repository;

import java.util.stream.Stream;

public interface GenericRepository<T,ID> {

    public Stream<T> readFromFile();

    void writeToFile(Stream<T> incomingStream);

    T getById(ID id);

    void setById(T object);

    ID getId(T type);

}
