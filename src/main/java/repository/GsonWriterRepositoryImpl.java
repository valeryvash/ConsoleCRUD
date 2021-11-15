package repository;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.Post;
import model.Writer;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class GsonWriterRepositoryImpl implements WriterRepository{

    private static final String fileName = "writers.json";

    private static final String workFolderPath = "src/main/java/ignoredPackage/filesDataBase/";

    private static final File file = new File(workFolderPath + fileName);

    // Need for return Generic type from string.
    // Always check that this link shall call the 'getType()' method
    private static final Type listTypeToken = new TypeToken<List<Writer>>(){}.getType();
    @Override
    public Stream<Writer> readFromFile(File file,Type token){
        Stream<Writer> resultStream = Stream.empty();

        try (BufferedReader gsonBufferedReader = new BufferedReader(new FileReader(file))) {
            List<Writer> buffer = new Gson().fromJson(gsonBufferedReader.readLine(), token);
            resultStream = buffer.stream();
        } catch (FileNotFoundException exception) {
            System.err.println(file.getName() + " not found in the next location:\n" +
                    file.getPath());
            exception.printStackTrace();
        } catch (IOException exception) {
            System.err.println("'IOException' during" + file.getName() + " read in the next location:\n" +
                    file.getPath());
            exception.printStackTrace();
        } catch (JsonSyntaxException exception) {
            System.err.println("'JsonSyntaxException' during" + file.getName() + " read in the next location:\n" +
                    file.getPath());
            exception.printStackTrace();
        }
        return resultStream;
    }
    @Override
    public void writeToFile(Stream<Writer> incomingStream, File file){
        try (BufferedWriter gsonBufferedWriter = new BufferedWriter(new FileWriter(file))) {
            List<Writer> buffer = incomingStream.collect(Collectors.toList());
            gsonBufferedWriter
                    .write(
                            new Gson()
                                    .toJson(buffer)
                    );
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            System.err.println("'FileNotFoundException' during" + file.getName() + " write in the next location:\n" +
                    file.getPath());
            System.err.println(file.getName() +" 'writeToFile()' method returns: " + file.canWrite());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("'IOException' during "+ file.getName() + "write in the next location:\n" +
                    file.getPath());
        }
    };

    @Override
    public Stream<Writer> readDefaultStream() {
        return readFromFile(file,listTypeToken);
    }

    @Override
    public void writeDefaultStream(Stream<Writer> stream) {
        writeToFile(stream,file);
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
