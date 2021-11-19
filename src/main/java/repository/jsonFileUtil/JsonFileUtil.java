package repository.jsonFileUtil;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.interfaces.Entity;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonFileUtil {

    public static <T> Stream<T> readFromFile(File toReadFrom, Type token) {
        Stream<T> resultStream = Stream.empty();

        try (BufferedReader gsonBufferedReader = new BufferedReader(new FileReader(toReadFrom))) {
            List<T> buffer = new Gson().fromJson(gsonBufferedReader.readLine(), token);
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

    public static <T> void writeToFile(Stream<T> incomingTagStream, File toWriteToFile) {
        try (BufferedWriter gsonBufferedWriter = new BufferedWriter(new FileWriter(toWriteToFile))) {
            List<T> buffer = incomingTagStream.collect(Collectors.toList());
            gsonBufferedWriter
                    .write(
                            new Gson()
                                    .toJson(buffer)
                    );

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            System.err.println("'FileNotFoundException' during "+toWriteToFile.getName()+" write in the next location:\n" +
                    toWriteToFile.getPath());
            System.err.println(toWriteToFile.getName() + " 'writeToFile()' method returns: " + toWriteToFile.canWrite());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("'IOException' during "+toWriteToFile.getName()+" write in the next location:\n" +
                    toWriteToFile.getPath());
        }
    }

    public static <T extends Entity> long getFreeId(Stream<T> readDefaultStream) {
        long result = 0L;
        long size = readDefaultStream.count();
        Optional<Long> maxId = readDefaultStream.map(T::getId).max(Long::compareTo);
        if (size != 0) {
            if (maxId.isPresent()) {
                long last = maxId.get();
                if (size <= last) {
                    result = last + 1;
                } else {
                    result = size + 1;
                }
            }
        }
        return result;
    }

}
