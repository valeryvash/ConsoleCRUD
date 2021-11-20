package repository.implementations;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.Tag;
import repository.interfaces.TagRepository;
import repository.jsonFileUtil.JsonFileUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class GsonTagRepositoryImpl implements TagRepository {

    private static final String fileName = "tags.json";

    private static final String workFolderPath = "src/main/java/ignoredPackage/filesDataBase/";

    private static final File file = new File(workFolderPath + fileName);

    // Need for return Generic type from string.
    // Always check that this link shall call the 'getType()' method
    private static final Type listTypeToken = new TypeToken<List<Tag>>(){}.getType();

    @Override
    public Stream<Tag> readDefaultStream() {
        return JsonFileUtil.readFromFile(file,listTypeToken);
    }

    @Override
    public void writeDefaultStream(Stream<Tag> stream) {
        JsonFileUtil.writeToFile(stream,file);
    }

    @Override
    public Long getFreeId() {
        return JsonFileUtil.getFreeId(readDefaultStream());
    }

    @Override
    public void add(Tag newWriter) {
        if (!contains(newWriter.getId())){
            writeDefaultStream(
                    concat(readDefaultStream(),Stream.of(newWriter))
            );
        }
    }

    @Override
    public Long add(String subject) {
        long subjectId = getFreeId();
        add(new Tag(subjectId, subject));
        return subjectId;
    }

    @Override
    public Tag getById(Long id) {
        Optional<Tag> temp =
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
    public void update(Tag objectToUpdate) {
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

    public void delete(Tag t) {
        delete(t.getId());
    }

    @Override
    public boolean contains(String tagName) {
        return readDefaultStream()
                .anyMatch(iterableObject -> iterableObject.getName().equals(tagName));
    }

    @Override
    public Tag getByName(String tagName) {
        return readDefaultStream()
                .filter(t1 -> t1.getName().equalsIgnoreCase(tagName))
                .findAny()
                .get();
    }

    // class methods tests
    public static void main(String[] args) {

        GsonTagRepositoryImpl gtr = new GsonTagRepositoryImpl();
        // display all entities in console screen
        gtr.displayAll();

        // create
        gtr.add(new Tag(5L, "Games"));
        gtr.displayAll();

        // read
        System.out.println("'getById' method call \n" + gtr.getById(5L));

        // update
        gtr.update(new Tag(5L, "VideoGames"));
        gtr.displayAll();

        // delete
        gtr.delete(5L);
        gtr.displayAll();
    }

    void displayAll() {
        System.out.println("Display entire collection");
        readDefaultStream().forEach(System.out::println);
    }
}
