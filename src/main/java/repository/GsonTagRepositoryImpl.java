package repository;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.Tag;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GsonTagRepositoryImpl {

    private static final String tagsFilePath = "src/main/java/ignoredPackage/filesDataBase/" + "tags.json";

    private static final File tagsFile = new File(tagsFilePath);

    // Need for return Generic type from string.
    // Always check that this link shall call the 'getType()' method
    private static final Type listTypeToken = new TypeToken<List<Tag>>(){}.getType();

    public static Stream<Tag> readFromFile() {
        Stream<Tag> result = Stream.empty();

        try (BufferedReader gsonBufferedReader = new BufferedReader(new FileReader(tagsFile))) {
            List<Tag> buffer = new Gson().fromJson(gsonBufferedReader.readLine(), listTypeToken);
            result = buffer.stream();
        } catch (FileNotFoundException exception) {
            System.err.println("'tags.json' not found in the next location:\n" +
                    tagsFilePath);
            exception.printStackTrace();
        } catch (IOException exception) {
            System.err.println("'IOException' during 'tags.json' read in the next location:\n" +
                    tagsFilePath);
            exception.printStackTrace();
        } catch (JsonSyntaxException exception) {
            System.err.println("'JsonSyntaxException' during 'tags.json' read in the next location:\n" +
                    tagsFilePath);
            exception.printStackTrace();
        }

        return result;
    }

    public static void writeToFile(Stream<Tag> incomingTagStream) {
        try (BufferedWriter gsonBufferedWriter = new BufferedWriter(new FileWriter(tagsFilePath))) {
            List<Tag> buffer = incomingTagStream.collect(Collectors.toList());
            gsonBufferedWriter
                    .write(
                            new Gson()
                                    .toJson(buffer)
                    );

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            System.err.println("'FileNotFoundException' during 'tags.json' write in the next location:\n" +
                    tagsFilePath);
            System.err.println("'tag.json' 'writeToFile()' method returns: " + tagsFile.canWrite());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("'IOException' during 'tags.json' write in the next location:\n" +
                    tagsFilePath);
        }
    }

    public static Optional<Tag> getMaxTagId() {
        return readFromFile()
                .max((a, b) -> ((int) (a.getId() - b.getId())));
    }

    /**
     * Method add new Tag in the 'tags.json' file by name and return it id.
     * If such tag already exist, return id of existing tag.
     * Blocks file for write till method end
     */

    public static long addTagByName(String proposedTagName){
        long resultId = 0L;
        tagsFile.setWritable(false);
        Optional<Tag> temp = getTagByName(proposedTagName);
        if (temp.isPresent()){
            resultId = temp.get().getId();
            tagsFile.setWritable(true);
        } else {
            if ((temp = getMaxTagId()).isPresent()) {
                resultId = temp.get().getId() + 1L;
            }
            List<Tag> tempList = readFromFile().collect(Collectors.toList());
            tempList.add(new Tag(resultId, proposedTagName));
            tagsFile.setWritable(true);
            writeToFile(tempList.stream());
        }
        return resultId;
    }

    /**
     *  Return Tag from json file
     */
    public static Optional<Tag> getTagById(long id){
        return readFromFile()
                .filter(tag -> tag.getId() == id)
                .findAny();
    }

    /**
     *  Return Tag from json file
     */
    public static Optional<Tag> getTagByName(String tagName) {
        return readFromFile()
                .filter(tag -> tag.getName().equals(tagName))
                .findAny();
    }

    /**
     *  Update Tag in json file, if such Tag already presented.
     *  Method block 'tags.json' file till the method end.
     */
    public static boolean updateTag(Optional<Tag> targetTag, String newTagName){
        boolean methodResult = false;
        tagsFile.setWritable(false);
        if (targetTag.isPresent()) {
            Tag tempTag = targetTag.get();
            List<Tag> tempList = readFromFile().filter(n -> n.getId() != tempTag.getId()).collect(Collectors.toList());
            tempList.add(new Tag(tempTag.getId(), newTagName));
            tagsFile.setWritable(true);
            writeToFile(tempList.stream());
            methodResult = true;
        }
        tagsFile.setWritable(true);
        return methodResult;
    }

    /**
     *  Delete Tag from json file
     *  return true if tag deleted and false in case of Tag absence
     */
    public static boolean deleteTag(Optional<Tag> targetTag){
        boolean methodResult = false;
        if (targetTag.isPresent()) {
            writeToFile(readFromFile().filter(tag -> tag.getId() != targetTag.get().getId()));
            methodResult = true;
        }
        return methodResult;
    }

    // class methods tests
    public static void main(String[] args) {

        System.out.println("getInnerTagsListStream() method test");
        readFromFile().forEach(System.out::println);

        System.out.println("Add tag in file test");
        System.out.println("Tag id is : " + addTagByName("555"));

        System.out.println("getInnerTagsListStream() method test");
        readFromFile().forEach(System.out::println);

        System.out.print("Update '555' tag name by '999' result is ");
        System.out.println(updateTag(getTagByName("555"),"999"));

        System.out.print("Update '555' tag name by '999' result is ");
        System.out.println(updateTag(getTagByName("555"),"999"));

        System.out.println("getInnerTagsListStream() method test");
        readFromFile().forEach(System.out::println);

        Consumer<Optional<Tag>> displayTag = n -> {
            if (n.isPresent()) {
                System.out.println(n.get());
            } else {
                System.out.println("'tag.json' doesn't contain such item");
            }
        };

        System.out.println("getTagById() method test");
        IntStream.range(0,10).forEach(n -> displayTag.accept(getTagById(n)));

        System.out.println("getTagByName() method test");
        Stream.of("dogs", "cats","JamesBaxter","JakeTheDog","Rock", "IT")
                .forEach(n-> displayTag.accept(getTagByName(n)));

        System.out.println("deleteTagById() method test");
        IntStream.range(5,10).forEach(n -> deleteTag(getTagById(n)));

    }
}
