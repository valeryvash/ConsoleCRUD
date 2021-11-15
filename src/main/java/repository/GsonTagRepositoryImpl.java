package repository;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.Post;
import model.Tag;
import model.Writer;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class GsonTagRepositoryImpl implements TagRepository{

    private static final String fileName = "tags.json";

    private static final String workFolderPath = "src/main/java/ignoredPackage/filesDataBase/";

    private static final File file = new File(workFolderPath + fileName);

    // Need for return Generic type from string.
    // Always check that this link shall call the 'getType()' method
    private static final Type listTypeToken = new TypeToken<List<Tag>>(){}.getType();

     public Stream<Tag> readFromFile(File toReadFrom, Type token) {
        Stream<Tag> resultStream = Stream.empty();

        try (BufferedReader gsonBufferedReader = new BufferedReader(new FileReader(toReadFrom))) {
            List<Tag> buffer = new Gson().fromJson(gsonBufferedReader.readLine(), token);
            resultStream = buffer.stream();
        } catch (FileNotFoundException exception) {
            System.err.println(toReadFrom.getName() + " not found in the next location:\n" +
                    toReadFrom.getPath());
            exception.printStackTrace();
        } catch (IOException exception) {
            System.err.println("'IOException' during " + toReadFrom.getName() + " read in the next location:\n" +
                    toReadFrom.getPath());
            exception.printStackTrace();
        } catch (JsonSyntaxException exception) {
            System.err.println("'JsonSyntaxException' during "+ toReadFrom.getName() +" read in the next location:\n" +
                    toReadFrom.getPath());
            System.err.println("Check file " + toReadFrom.getName() + " content please");
            exception.printStackTrace();
        }
        return resultStream;
    }

    public void writeToFile(Stream<Tag> incomingTagStream, File file) {
        try (BufferedWriter gsonBufferedWriter = new BufferedWriter(new FileWriter(file))) {
            List<Tag> buffer = incomingTagStream.collect(Collectors.toList());
            gsonBufferedWriter
                    .write(
                            new Gson()
                                    .toJson(buffer)
                    );

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            System.err.println("'FileNotFoundException' during "+file.getName()+" write in the next location:\n" +
                    file.getPath());
            System.err.println(file.getName() + " 'writeToFile()' method returns: " + file.canWrite());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("'IOException' during "+file.getName()+" write in the next location:\n" +
                    file.getPath());
        }
    }

    @Override
    public Stream<Tag> readDefaultStream() {
        return readFromFile(file,listTypeToken);
    }

    @Override
    public void writeDefaultStream(Stream<Tag> stream) {
        writeToFile(stream,file);
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
