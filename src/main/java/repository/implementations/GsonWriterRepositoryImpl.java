package repository.implementations;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.Post;
import model.Tag;
import model.Writer;
import repository.interfaces.WriterRepository;
import repository.jsonFileUtil.JsonFileUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class GsonWriterRepositoryImpl implements WriterRepository {

    private static final String fileName = "writers.json";

    private static final String workFolderPath = "src/main/java/ignoredPackage/filesDataBase/";

    private static final File file = new File(workFolderPath + fileName);

    // Need for return Generic type from string.
    // Always check that this link shall call the 'getType()' method
    private static final Type listTypeToken = new TypeToken<List<Writer>>(){}.getType();


    @Override
    public Stream<Writer> readDefaultStream() {
        return JsonFileUtil.readFromFile(file,listTypeToken);
    }

    @Override
    public void writeDefaultStream(Stream<Writer> stream) {
        JsonFileUtil.writeToFile(stream, file);
    }

    @Override
    public Long getFreeId() {
        return JsonFileUtil.getFreeId(readDefaultStream());
    }

    @Override
    public void add(Writer newWriter) {
        if (!contains(newWriter.getId())){
            writeDefaultStream(
                    concat(readDefaultStream(),Stream.of(newWriter))
            );
        }
    }

    @Override
    public Long add(String subject) {
        long subjectId = getFreeId();
        add(
                new Writer(
                        subjectId,
                        subject,
                        new ArrayList<Post>()
                )
        );
        return subjectId;
    }

    @Override
    public Writer getById(Long id) {
        Optional<Writer> temp =
                readDefaultStream()
                .filter(writer -> writer.getId() == id)
                .findAny();
        if (temp.isEmpty()) {
            throw new IllegalArgumentException(
                    "Object with id: " + id + " is absent in " + file.getName() + " in work folder " + file.getPath()+
                    "\nAlways check object availability with 'contains()' method before getMethod usage."
                    );
        }
        return temp.get();
    }
    @Override
    public void update(Writer objectToUpdate) {
        writeDefaultStream(
                readDefaultStream()
                        .map( iteratedObject -> {
                            if (iteratedObject.getId() == objectToUpdate.getId()){
                                return objectToUpdate;
                            } else {
                                return iteratedObject;
                            }
                        })
        );
    }
    @Override
    public void delete(Long id) {
        writeDefaultStream(
                readDefaultStream()
                        .filter(
                                iterableObject -> iterableObject.getId() != id
                        )
        );
    }

    @Override
    public boolean contains(Long id) {
        return readDefaultStream()
                .anyMatch(iterableObject -> iterableObject.getId() == id);
    }

    @Override
    public Writer getByName(String writerName) {
        Optional<Writer> temp =
                readDefaultStream()
                        .filter(writer -> writer.getName().equalsIgnoreCase(writerName))
                        .findAny();
        if (temp.isEmpty()) {
            throw new IllegalArgumentException(
                    "Object with name: " + writerName + " is absent in " + file.getName() +
                            " in work folder " + file.getPath()+
                            "\nAlways check object availability with 'contains()' " +
                            "method before getMethod usage.");
        }
        return temp.get();
    }

    @Override
    public boolean contain(String writerName) {
        return readDefaultStream()
                .anyMatch(iterableObject -> iterableObject.getName().equalsIgnoreCase(writerName));
    }

    @Override
    public Stream<Post> getWriterPosts(Long writerId) {
        return getById(writerId).getPosts().stream();
    }

    @Override
    public void update(Tag t1) {
        writeDefaultStream(
                readDefaultStream()
                        .map(w1 ->{
                            if (w1.contains(t1)) {
                                w1.update(t1);
                            }
                            return w1;
                        })
        );
    }

    @Override
    public void delete(Tag t1) {
        writeDefaultStream(
                readDefaultStream()
                        .map(w1 -> {
                            if (w1.contains(t1)) {
                                w1.delete(t1);
                            }
                            return w1;
                        })
        );
    }

    public boolean contains(String writerName) {
        return readDefaultStream()
                .anyMatch(w1 -> w1.getName().equalsIgnoreCase(writerName));
    }

    @Override
    public void delete(Post p1) {
        writeDefaultStream(
                readDefaultStream()
                        .map(w1 ->{
                            w1.setPosts(
                                    w1.getPosts().stream()
                                            .filter(p2 -> p2.getId() != p1.getId())
                                            .collect(Collectors.toList())
                            );
                            return w1;
                        })
        );
    }

    // class tests
    public static void main(String[] args) {

        GsonWriterRepositoryImpl gwr = new GsonWriterRepositoryImpl();
        // display all entities in console screen
        gwr.displayAll();

        // create
        gwr.add(new Writer(4L, "Lev Tolstoy", new ArrayList<>()));
        gwr.displayAll();

        //read
        System.out.println("'getById' method call \n "  + gwr.getById(4L));

        // update
        gwr.update(new Writer(4L, "Alexey Tolstoy", new ArrayList<>()));
        gwr.displayAll();

        // delete
        gwr.delete(4L);
        gwr.displayAll();
    }

    void displayAll() {
        System.out.println("Display entire collection");
        readDefaultStream().forEach(System.out::println);
    }
}
