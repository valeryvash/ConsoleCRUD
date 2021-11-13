package model;

import java.util.List;

public class Writer {
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
}
