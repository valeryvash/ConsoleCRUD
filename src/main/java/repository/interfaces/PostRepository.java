package repository.interfaces;

import model.Post;
import model.PostStatus;
import model.Tag;

import java.util.stream.Stream;

public interface PostRepository extends GenericRepository<Post,Long>{

     @Override
     Stream<Post> readDefaultStream();

     @Override
     void writeDefaultStream(Stream<Post> stream);

     @Override
     Long getFreeId();

     @Override
     void add(Post p);

     @Override
     Long add(String content);

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

     void update(Tag t);

     void delete(Tag t);
}
