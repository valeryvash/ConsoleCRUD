package view;

import controller.PostController;
import model.Post;
import model.PostStatus;
import model.Tag;
import model.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PostView {
    private static String postsMessage =
            "Posts\n" +
                    "Input the point to continue...\n" +
                    "1. Create a new post by writer id\n" +
                    "2. Create a new post by writer name\n" +
                    "3. Get all posts\n" +
                    "4. Get all post by status\n" +
                    "5. Get post by id\n" +
                    "6. Get posts by tags\n" +
                    "7. Update post content by post id\n" +
                    "8. Update post tags by post id\n" +
                    "9. Update post status by post id\n" +
                    "10. Delete post by id\n" +
                    "11. Delete posts by tag list\n" +
                    "12. Delete posts by status\n" +
                    "\t'q' for quit\n" +
                    "\t'p' for previous screen\n";

    private static Scanner sc = new Scanner(System.in);

    private static PostController pc = new PostController();

    public static void run() {
        System.out.println(postsMessage);
        choice();
    }

    private static void choice() {
        switch (sc.nextLine().toLowerCase()) {
            case "1" -> createPostByWriterId(getWriterById());
            case "2" -> createPostByWriterId(getWriterByName());
            case "3" -> getAllPosts();
            case "4" -> getAllPostsByStatus();
            case "5" -> getPostById();
            case "6" -> getPostsByTags();
            case "7" -> updatePostContentByPostId();
            case "8" -> updatePostTagsByPostId();
            case "9" -> updatePostStatusByPostId();
            case "10" -> deletePostByPostId();
            case "11" -> deletePostsByTagList();
            case "12" -> deletePostsByStatus();

            case "q" -> System.exit(0);
            case "p" -> StartupView.run();
            default -> System.out.println("Wrong point");
        }
        question();
    }

    private static void question() {
        System.out.print("Show 'PostView' again? 'y' for yes\t");
        switch (sc.nextLine().toLowerCase()) {
            case "y" -> PostView.run();
            default -> System.exit(0);
        }
    }

    private static void createPostByWriterId(Writer w) {

        long id = pc.getFreeId();

        System.out.println("Input content for new post.\n 'q' for quit");

        String s = sc.nextLine();

        if (s.equals("q") || s.equals("Q")) System.exit(0);

        List<Tag> tagList = getTagList(true);

        Post p = new Post(
                id,
                s,
                tagList,
                PostStatus.ACTIVE
        );

        pc.add(p);
        w.addPost(p);
        pc.updateWriterPosts(w);
    }

    private static Writer getWriterById() {
        System.out.println("Input existed writer id\n 'q' for quit");

        do {
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            try {
                long id = Long.parseLong(s);
                if (!pc.writerExist(id)) throw new IllegalArgumentException();
                Writer w = pc.getWriterById(id);
                return w;
            } catch (NumberFormatException e) {
                System.out.println("Input the numeric string please");
            } catch (IllegalArgumentException e) {
                System.out.println("Such writer doesn't exist! Try another id");
            }

        } while (true);
    }

    private static Writer getWriterByName() {
        System.out.println("Input existed writer name\n 'q' for quit");

        do {
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            try {
                if (!pc.writerExist(s)) throw new IllegalArgumentException();
                Writer w = pc.getWriterByName(s);
                return w;
            } catch (NumberFormatException e) {
                System.out.println("Input the numeric string please");
            } catch (IllegalArgumentException e) {
                System.out.println("Such writer doesn't exist! Try another name");
            }
        } while (true);
    }

    private static List<Tag> getTagList(boolean newAllowed){
        List<Tag> result = new ArrayList<Tag>();

        do {
            System.out.println("Input tag for post\n 's' for skip\n 'q' for quit");

            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);
            if (s.equals("s")) break;
            
            if (newAllowed){
                result.add(pc.getExistingOrCreateNewTag(s));
            } else {
                if (pc.tagExist(s)){
                    result.add(pc.getExistTag(s));
                } else {
                    System.out.println("Tag doesn't exist, try another");
                }
            }
        } while (true);

        return result;
    }

    private static void getAllPosts(){
        pc.getAll().forEach(System.out::println);
    }

    private static void getAllPostsByStatus() {
        PostStatus ps;
        System.out.println("Choose post status which you prefer\n" +
                            "1. ACTIVE\n" +
                            "2. DELETED\n" +
                            " 'q' for quit");

        ps = getPostStatus();

        pc.getPostByStatus(ps).forEach(System.out::println);
    }

    private static PostStatus getPostStatus() {

        System.out.println("Choose post status which you prefer\n" +
                "1. ACTIVE\n" +
                "2. DELETED\n" +
                " 'q' for quit");

        do {
            String s = sc.nextLine().toLowerCase();

            if (s.equals("q")) System.exit(0);

            switch (s) {
                case "1" -> {
                    return PostStatus.ACTIVE;
                }
                case "2" -> {
                    return PostStatus.DELETED;
                }
                default -> System.out.println("Wrong point. Input other");
            }
        } while (true);
    }

    public static Post getPostById() {
        System.out.println("Input existed post id\n 'q' for quit");

        do {
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            try {
                long id = Long.parseLong(s);
                if (!pc.contains(id)) throw new IllegalArgumentException();
                Post p = pc.getById(id);
                System.out.println(pc.getById(id));
                return p;
            } catch (NumberFormatException e) {
                System.out.println("Input the numeric string please");
            } catch (IllegalArgumentException e) {
                System.out.println("Such post doesn't exist! Try another name");
            }
        } while (true);
    }

    private static void getPostsByTags() {
        List<Tag> tagList = getTagList(false);
        pc.getPostsByTagsList(tagList).forEach(System.out::println);
    }

    private static void updatePostContentByPostId(){
        Post p = getPostById();

        System.out.println("Input new content.\n 'q' for quit");

        String s = sc.nextLine();

        if (s.equals("q")) System.exit(0);

        p.setContent(s);
        pc.updatePost(p);
        System.out.println("Post updated");
    }

    private static void updatePostTagsByPostId() {
        Post p = getPostById();

        List<Tag> tagList = getTagList(true);

        p.setTags(tagList);
        pc.updatePost(p);
        System.out.println("Post updated");
    }

    private static void updatePostStatusByPostId() {
        Post p = getPostById();

        PostStatus ps = getPostStatus();

        p.setStatus(ps);
        pc.updatePost(p);
        System.out.println("Post updated");
    }

    private static void deletePostByPostId(){
        Post p = getPostById();
        pc.delete(p);
        System.out.println("Post deleted");
    }

    private static void deletePostsByTagList() {
        List<Tag> tagList = getTagList(false);

        List<Post> postList = pc.getPostsByTagsList(tagList).collect(Collectors.toList());

        postList.forEach(p -> pc.delete(p));

        System.out.println("Posts deleted");
    }

    private static void deletePostsByStatus() {
        PostStatus ps = getPostStatus();
        pc.getPostByStatus(ps).forEach(pc::delete);
        System.out.println("Posts deleted");
    }
}
