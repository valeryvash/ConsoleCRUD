package ignoredPackage.filesDataBase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Post;
import model.PostStatus;
import model.Tag;
import model.Writer;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/*
    This is utility class. It is used for database generation
 */
public class DataBaseGenerate {

    static String workFolderPath = "src/main/java/ignoredPackage/filesDataBase/";

    static List<Tag> tagsToGson;
    static List<Writer> writesToGson;
    static List<Post> postsToGson;

    static String jsonString = "";

    private static final Type tagsTypeToken = new TypeToken<List<Tag>>() {}.getType();
    private static final Type writersTypeToken = new TypeToken<List<Writer>>() {}.getType();
    private static final Type postsTypeToken = new TypeToken<List<Writer>>() {}.getType();

    static <T> void collectionToFile(List<T> incomingCollection, String fileName){
        try (FileOutputStream gsonOutputStream = new FileOutputStream(workFolderPath + fileName))
        {
            gsonOutputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
            System.out.println("Collection successfully written down to " +fileName+ " file");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Exception during json object write in folder:\n" +
                    workFolderPath +
                     fileName);
        }
    }

    static void defaultJsonTagFileGenerate(){
        tagsToGson = new ArrayList<>();

        tagsToGson.add(new Tag(1L,"cats"));
        tagsToGson.add(new Tag(2L,"dogs"));
        tagsToGson.add(new Tag(3L,"IT"));
        tagsToGson.add(new Tag(4L,"JamesBaxter"));

        jsonString = new Gson().toJson(tagsToGson);

        collectionToFile(tagsToGson,"tags.json");
    }

    public static void defaultJsonWriterFileGenerate() {
        writesToGson = new ArrayList<Writer>();

        writesToGson.add(new Writer(1L, "Agniya Barto", new ArrayList<Post>()));
        writesToGson.add(new Writer(2L, "Sergey Mikhalkov", new ArrayList<Post>()));
        writesToGson.add(new Writer(3L, "Nikolay Korneichukov", new ArrayList<Post>()));

        jsonString = new Gson().toJson(writesToGson);

        collectionToFile(writesToGson, "writers.json");
    }

    public static void defaultJsonPostsFileGenerate()
    {
        postsToGson = new ArrayList<Post>();

        postsToGson.add(
                new Post(
                        1L,
                        "How dy world!",
                        List.of(new Tag (1L,"cat"),new Tag(3L,"IT")),
                        PostStatus.ACTIVE
                )
        );

        postsToGson.add(
                new Post(
                        2L,
                        "Jaaaaames Baxteeeer",
                        List.of(new Tag (4L,"JamesBaxter")),
                        PostStatus.ACTIVE
                )
        );

        postsToGson.add(
                new Post(
                        3L,
                        "sdfgsdfgsdfg",
                        new ArrayList<Tag>(),
                        PostStatus.DELETED
                )
        );

        jsonString = new Gson().toJson(postsToGson);

        collectionToFile(postsToGson,"posts.json");

    }

    public static void main(String[] args) {
//        defaultJsonTagFileGenerate();
//        defaultJsonWriterFileGenerate();
        defaultJsonPostsFileGenerate();
    }
}
