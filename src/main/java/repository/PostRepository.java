package repository;

import model.Post;
import model.PostStatus;
import model.Tag;

import java.io.*;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public interface PostRepository extends GenericRepository<Post,Long>{
     @Override
     Stream<Post> readFromFile(File toReadFrom, Type token);

     @Override
     void writeToFile(Stream<Post> incomingTagStream, File toWriteToFile);

     @Override
     Stream<Post> readDefaultStream();

     @Override
     void writeDefaultStream(Stream<Post> stream);

     @Override
     void add(Post p);

     @Override
     Post getById(Long id);

     @Override
     void update(Post p);

     @Override
     void delete(Long id);

     @Override
     boolean contains(Long id);

     boolean contains(Post p);

     void delete(Post p);

     Stream<Tag> getTagStreamFromPost(Post p);

     Stream<Post> getPostByStatus(PostStatus status);

}
