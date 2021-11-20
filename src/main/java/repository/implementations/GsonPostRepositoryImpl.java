package repository.implementations;

import com.google.gson.reflect.TypeToken;
import model.Post;
import model.PostStatus;
import model.Tag;
import repository.interfaces.PostRepository;
import repository.jsonFileUtil.JsonFileUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GsonPostRepositoryImpl implements PostRepository {

    private static final String workFolderPath = "src/main/java/ignoredPackage/filesDataBase/";

    private static final String fileName = "posts.json";

    private static final File file = new File(workFolderPath + fileName);

    private static final Type listTypeToken = new TypeToken<List<Post>>(){}.getType();

    public GsonPostRepositoryImpl(){}

    @Override
    public Stream<Post> readDefaultStream() {
        return JsonFileUtil.readFromFile(file,listTypeToken);
    }

    @Override
    public void writeDefaultStream(Stream<Post> stream) {
        JsonFileUtil.writeToFile(stream, file);
    }

    @Override
    public Long getFreeId() {
        return JsonFileUtil.getFreeId(readDefaultStream());
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
    public Long add(String subject) {
        long subjectId = getFreeId();
        add(
                new Post(subjectId,
                        subject,
                        new ArrayList<Tag>(),
                        PostStatus.ACTIVE)
        );
        return subjectId;
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

    @Override
    public void update(Tag t1) {
        writeDefaultStream(
                readDefaultStream()
                        .map(p1 ->{
                            if (p1.contain(t1)){
                                p1.update(t1);
                            }
                            return p1;
                        })
        );
    }

    @Override
    public void delete(Tag t) {
        writeDefaultStream(
                readDefaultStream()
                        .map(p1 ->{
                            if (p1.contain(t)) {
                                p1.delete(t);
                            }
                            return p1;
                        })
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
