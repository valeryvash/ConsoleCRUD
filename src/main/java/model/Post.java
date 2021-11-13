package model;

import java.util.List;

public class Post {
    private long id;
    private String content;
    private List<Tag> tags;
    private PostStatus status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public Post(long id, String content, List<Tag> tags, PostStatus status) {
        setId(id);
        setContent(content);
        setTags(tags);
        setStatus(status);
    }

    private Post(){}

    @Override
    public String toString() {
        return "===Post===\n" +
                "\t id \t" + getId() + "\n" +
                "\t status \t" + getStatus().toString() + "\n"+
                "\t content \t" + getContent() + "\n"+
                "==============\n\n ";
    }
}
