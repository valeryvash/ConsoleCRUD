package repository;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.Post;
import model.PostStatus;
import model.Tag;
import model.Writer;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GsonPostRepositoryImpl implements PostRepository {

    private static final String workFolderPath = "src/main/java/ignoredPackage/filesDataBase/";

    private static final String fileName = "posts.json";

    private static final File file = new File(workFolderPath + fileName);

    private static final Type listTypeToken = new TypeToken<List<Post>>(){}.getType();

    public GsonPostRepositoryImpl(){}

    @Override
    public Stream<Post> readFromFile(File toReadFrom, Type token) {
        Stream<Post> resultStream = Stream.empty();

        try (BufferedReader gsonBufferedReader = new BufferedReader(new FileReader(toReadFrom))) {
            List<Post> buffer = new Gson().fromJson(gsonBufferedReader.readLine(), token);
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

    @Override
    public void writeToFile(Stream<Post> incomingTagStream, File toWriteToFile) {
        try (BufferedWriter gsonBufferedWriter = new BufferedWriter(new FileWriter(toWriteToFile))) {
            List<Post> buffer = incomingTagStream.collect(Collectors.toList());
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

    @Override
    public Stream<Post> readDefaultStream() {
        return readFromFile(file,listTypeToken);
    }

    @Override
    public void writeDefaultStream(Stream<Post> stream) {
        writeToFile(stream, file);
    }

    @Override
    public void add(Post p) {
        writeDefaultStream(
                Stream.concat(
                        readDefaultStream(),
                        Stream.of(p)
                )
        );
    }

    @Override
    public Post getById(Long id) {
        Optional<Post> result =
                readDefaultStream()
                .filter(n -> n.getId() == id)
                .findAny();
        if (result.isEmpty()){
            throw new IllegalArgumentException("Always check element availability with 'contains()' method call\n" +
                    "Message from GsonPostRepositoryImp");
        } else {
            return result.get();
        }
    }

    @Override
    public void update(Post p) {
        writeDefaultStream(
                readDefaultStream()
                        .map( iterableObject -> {
                            if (iterableObject.getId() == p.getId()){
                                return p;
                            } else {
                                return iterableObject;
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
                .anyMatch( iterableObject -> iterableObject.getId() == id);
    }

    @Override
    public boolean contains(Post p) {
        return contains(p.getId());
    }

    @Override
    public void delete(Post p) {
        delete(p.getId());
    }

    @Override
    public Stream<Tag> getTagStreamFromPost(Post p) {
        return getById(p.getId()).getTags().stream();
    }

    @Override
    public Stream<Post> getPostByStatus(PostStatus status) {
        return readDefaultStream()
                .filter(iterableObject -> iterableObject.getStatus() == status);
    }

    void reversePostStatus(Post p) {
        writeDefaultStream(
                readDefaultStream()
                        .map( iterableObject -> {
                                if (iterableObject.getId() == p.getId()) {
                                    if (p.getStatus() == PostStatus.ACTIVE){
                                        p.setStatus(PostStatus.DELETED);
                                    } else {
                                        p.setStatus(PostStatus.ACTIVE);
                                    }
                                    return p;
                                } else {
                                    return iterableObject;
                                }
                            }
                        )
        );
    }

    // class tests
    public static void main(String[] args) {
        GsonPostRepositoryImpl gpri = new GsonPostRepositoryImpl();
        // display all entities
        gpri.displayAll();

        // create
        gpri.add(new Post(4L, "It is a new message!",List.of(new Tag(1,"l")),PostStatus.ACTIVE));

        // read
        System.out.println("Object with id 4");
        System.out.println(gpri.getById(4L));

        //update
        Post p = gpri.getById(4L);
        p.setStatus(PostStatus.DELETED);
        gpri.update(p);
        gpri.displayAll();

        //delete
        gpri.delete(4L);
        gpri.displayAll();
    }

    void displayAll() {
        System.out.println("Entire collection print");
        readDefaultStream().forEach(System.out::println);
        System.out.println();
    }
}
