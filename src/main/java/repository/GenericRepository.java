package repository;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.Tag;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;

public interface GenericRepository<T,ID> {

    Stream<T> readFromFile(File toReadFrom, Type token);

    void writeToFile(Stream<T> incomingTagStream, File toWriteToFile);

    Stream<T> readDefaultStream();

    void writeDefaultStream(Stream<T> stream);

    void add(T p);

    T getById(ID id);

    void update(T t);

    void delete(ID id);

    boolean contains(ID id);
}
