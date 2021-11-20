package controller;

import model.Post;
import model.PostStatus;
import model.Tag;
import model.Writer;
import repository.implementations.GsonPostRepositoryImpl;
import repository.implementations.GsonTagRepositoryImpl;
import repository.implementations.GsonWriterRepositoryImpl;
import repository.interfaces.PostRepository;
import repository.interfaces.TagRepository;
import repository.interfaces.WriterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostController {

    PostRepository pr = new GsonPostRepositoryImpl();

    TagRepository tr = new GsonTagRepositoryImpl();

    WriterRepository wr = new GsonWriterRepositoryImpl();

    public long getFreeId() {
        return pr.getFreeId();
    }

    public void add(Post p) {
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

    public Tag getExistingOrCreateNewTag(String tagName) {
        Tag t1;

        Optional<Tag> t2 = tr.readDefaultStream()
                .filter(t3 -> t3.getName().equalsIgnoreCase(tagName))
                .findAny();

        t1 = t2.orElseGet(
                () -> new Tag(
                        tr.getFreeId(),
                        tagName
                ));

        return t1;
    }

    public boolean writerExist(long id) {
        return wr.contains(id);
    }

    public boolean writerExist(String writerName) {
        return wr.contains(writerName);
    }

    public Writer getWriterById(long id) {
        return wr.getById(id);
    }

    public Writer getWriterByName(String writerName) {
        return wr.getByName(writerName);
    }

    public void updateWriterPosts(Writer w1) {
        wr.writeDefaultStream(
                wr.readDefaultStream()
                        .map(w2 -> {
                            if (w2.getId() == w1.getId()) {
                                return w1;
                            } else {
                                return w2;
                            }
                        })
        );
    }

    public Stream<Post> getPostByStatus(PostStatus status) {
        return pr.getPostByStatus(status);
    }

    public boolean tagExist(String tagName) {
        return tr.contains(tagName);
    }


    public Tag getExistTag(String tagName) {
        return tr.getByName(tagName);
    }

    public Stream<Post> getPostsByTagsList(List<Tag> tagList) {
        return pr.readDefaultStream()
                .filter(p1 -> {
                    Set<Long> p1TagsIdSet =
                            p1.getTags()
                                    .stream()
                                    .map(t1 -> t1.getId())
                                    .collect(Collectors.toSet());
                    Set<Long> p2TagsIdSet =
                            tagList.stream()
                                    .map(t2 -> t2.getId())
                                    .collect(Collectors.toSet());
                    if ((p2TagsIdSet.size() == 0) & (p1TagsIdSet.size() == 0)) {
                        return true;
                    } else {
                        return p1TagsIdSet.retainAll(p2TagsIdSet);
                    }
                });
    }

    public void updatePost(Post p1) {
        pr.writeDefaultStream(
                pr.readDefaultStream()
                        .map(p2 -> {
                            if (p1.getId() == p2.getId()) {
                                return p1;
                            } else {
                                return p2;
                            }
                        })
        );

        wr.writeDefaultStream(
                wr.readDefaultStream()
                        .map(w1 -> {
                            Stream<Post> pStream = w1.getPosts().stream();
                            if (pStream.anyMatch(p2 -> p2.getId() == p1.getId())) {
                                w1.update(p1);
                            }
                            return w1;
                        })
        );

    }

    public void delete(Post p1) {
        pr.delete(p1);
        wr.delete(p1);
    }


}
