package repository;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.Post;
import model.Tag;
import model.Writer;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GsonWriterRepositoryImpl {

    private static final String writerFilePath = "src/main/java/ignoredPackage/filesDataBase/" + "writers.json";

    private static final File writersFile = new File(writerFilePath);

    // Need for return Generic type from string.
    // Always check that this link shall call the 'getType()' method
    private static final Type listTypeToken = new TypeToken<List<Writer>>(){}.getType();

    public static Stream<Writer> readFromFile(){
        Stream<Writer> resultStream = Stream.empty();

        try (BufferedReader gsonBufferedReader = new BufferedReader(new FileReader(writersFile))) {
            List<Writer> buffer = new Gson().fromJson(gsonBufferedReader.readLine(), listTypeToken);
            resultStream = buffer.stream();
        } catch (FileNotFoundException exception) {
            System.err.println("'writers.json' not found in the next location:\n" +
                    writerFilePath);
            exception.printStackTrace();
        } catch (IOException exception) {
            System.err.println("'IOException' during 'writers.json' read in the next location:\n" +
                    writerFilePath);
            exception.printStackTrace();
        } catch (JsonSyntaxException exception) {
            System.err.println("'JsonSyntaxException' during 'writers.json' read in the next location:\n" +
                    writerFilePath);
            exception.printStackTrace();
        }
        return resultStream;
    }

    public static void writeToFile(Stream<Writer> incomingWriterStream) {
        try (BufferedWriter gsonBufferedWriter = new BufferedWriter(new FileWriter(writersFile))) {
            List<Writer> buffer = incomingWriterStream.collect(Collectors.toList());
            gsonBufferedWriter
                    .write(
                            new Gson()
                                    .toJson(buffer)
                    );
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            System.err.println("'FileNotFoundException' during 'tags.json' write in the next location:\n" +
                    writerFilePath);
            System.err.println("'tag.json' 'writeToFile()' method returns: " + writersFile.canWrite());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("'IOException' during 'tags.json' write in the next location:\n" +
                    writerFilePath);
        }
    }

    public static Optional<Writer> getWriterById(long id) {
        return readFromFile()
                .filter(writer -> writer.getId() == id)
                .findAny();
    }

    public static Optional<Writer> getWriterByName(String writerName) {
        return readFromFile()
                .filter(writer -> writer.getName().equalsIgnoreCase(writerName))
                .findAny();
    }

    public static Optional<Writer> getMaxWriterId() {
        return readFromFile()
                .max((a, b) -> ((int) (a.getId() - b.getId())));
    }


    public static long addWriterByName(String proposedWriterName){
        long resultId = 0L;
        writersFile.setWritable(false);
        Optional<Writer> temp = getWriterByName(proposedWriterName);
        if (temp.isPresent()){
            resultId = temp.get().getId();
            writersFile.setWritable(true);
        } else {
            if ((temp = getMaxWriterId()).isPresent()) {
                resultId = temp.get().getId() + 1L;
            }
            List<Writer> tempList = readFromFile().collect(Collectors.toList());
            tempList.add(new Writer(resultId, proposedWriterName,new ArrayList<Post>()));
            writersFile.setWritable(true);
            writeToFile(tempList.stream());
        }
        return resultId;
    }

    public static void updateWriter(Writer targetWriter){
           writeToFile(readFromFile().map( n -> {
                if(n.getId() == targetWriter.getId()) {
                    return new Writer(targetWriter);
                } else {
                    return n;
                }
            }));
    }

    // class tests
    public static void main(String[] args) {

        System.out.println("Read from stream from file:");
        readFromFile().forEach(System.out::println);

        writeToFile(readFromFile().filter(n -> n.getId() != 2));

        System.out.println("Read from stream from file:");
        readFromFile().forEach(System.out::println);

        addWriterByName("Sergey Mikhalkov");

        System.out.println("Read from stream from file:");
        readFromFile().forEach(System.out::println);
    }
}
