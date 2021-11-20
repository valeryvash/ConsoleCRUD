package model;

import model.interfaces.Entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Writer implements Entity {
    private long id;
    private String name;
    private List<Post> posts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Writer(long id, String name, List<Post> posts) {
        setId(id);
        setName(name);
        setPosts(posts);
    }

    public Writer(Writer writerForClone) {
        setId(writerForClone.getId());
        setName(writerForClone.getName());
        setPosts(writerForClone.getPosts());
    }

    private Writer(){}

    @Override
    public String toString() {
        return "Writer [id: " + getId() +
                ", name: " + getName() +
                ", posts amount: " + posts.size() + "]";
    }

    public boolean contains(Tag t1){
        return getPosts()
                .stream()
                .anyMatch(w1 -> w1.contain(t1));
    }

    public void update(Tag t1) {
        setPosts(
                getPosts()
                        .stream()
                        .map(p1 ->{
                            if (p1.contain(t1)) {
                                p1.update(t1);
                            }
                            return p1;
                        })
                        .collect(Collectors.toList())
        );
    }

    public void delete(Tag t1) {
        setPosts(
                getPosts()
                        .stream()
                        .map(p1 ->{
                            if(p1.contain(t1)){
                                p1.delete(t1);
                            }
                            return p1;
                        })
                        .collect(Collectors.toList())
        );
    }

    public void addPost(Post p1) {
        setPosts(
                Stream.concat(
                        getPosts().stream(),
                        Stream.of(p1)
                        )
                        .collect(Collectors.toList())
        );
    }

    public void update(Post p1) {
        setPosts(
                getPosts().stream()
                        .map(p2 -> {
                            if (p2.getId() == p1.getId()) {
                                return p1;
                            } else {
                                return p2;
                            }
                        })
                        .collect(Collectors.toList())
        );
    }

}
