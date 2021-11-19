package controller;

import model.Post;
import model.Tag;
import repository.implementations.GsonPostRepositoryImpl;
import repository.implementations.GsonTagRepositoryImpl;
import repository.interfaces.PostRepository;
import repository.interfaces.TagRepository;

import java.util.stream.Stream;

public class PostController {

    PostRepository pr = new GsonPostRepositoryImpl();

    TagRepository tr = new GsonTagRepositoryImpl();

    public void add(Post p){
        pr.add(p);
    }

    public Long add(String content) {
        return pr.add(content);
    }

    public Stream<Post> getAll() {
        return pr.readDefaultStream();
    }

    public Post getById(Long id) {
        return pr.getById(id);
    }

    public boolean contains(long id) {
        return pr.contains(id);
    }

    public void update(Tag t1) {
        tr.update(t1);
        pr.writeDefaultStream(
                pr.readDefaultStream()
                        .map(post -> {
                            if (post.contain(t1)){
                                post.update(t1);
                            }
                            return post;
                        })
        );
    }

    public void delete(Tag t1) {

    }


}
