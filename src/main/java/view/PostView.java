package view;

import controller.PostController;
import controller.WriterController;

import java.util.Scanner;

public class PostView {
    private static String postsMessage =
            "Posts\n" +
                    "Input the point to continue...\n" +
                    "1. Create a new post by writer id\n" +
                    "2. Get all posts\n" +
                    "3. Get post by id\n" +
                    "4. Update post by id\n" +
                    "5. Delete post by id\n" +
                    "\t'q' for quit\n" +
                    "\t'p' for previous screen\n";

    private static Scanner sc = new Scanner(System.in);

    private static WriterController wc = new WriterController();

    private static PostController pc = new PostController();

    public static void run() {
        System.out.println(postsMessage);

        choice();
    }

    private static void choice() {
        switch (sc.nextLine().toLowerCase()) {
            case "1" -> createPost();
            case "2" -> getAllPosts();
            case "3" -> getPostById(true);
//            case "4" -> updatePostById();
//            case "5" -> deletePostById();
            case "q" -> System.exit(0);
            case "p" -> StartupView.run();
            default -> {
                System.out.println("Try another one");
                choice();
            }
        }
        question();
    }

    private static void question() {
        System.out.print("Continue? 'y' for yes\t");
        switch (sc.nextLine().toLowerCase()) {
            case "y" -> run();
            case "n" -> System.exit(0);
            default -> question();
        }
    }

    private static void createPost() {
        System.out.println("Input new content\n " +
                "'q' for quit, 'p' for previous menu");

        long postId;
        String content = sc.nextLine().toLowerCase();

        switch(content){
            case "q" -> System.exit(0);
            case "p" -> run();
            default -> {
                postId = pc.add(content);
                System.out.println("New post id is " + postId);
            }
        }
    }


    private static void addTagsforPost() {
        long postId = getPostById(true);
        repeat:{
            while (true) {
                System.out.println("Input tags for post\n " +
                        "'q' for quit, 'p' for previous menu");

                String tagName = sc.nextLine();

                switch(tagName){
                    case "q" -> System.exit(0);
                    case "p" -> {
                        run();
                        break repeat;
                    }
                    default -> {
//                        long tagId = addTag(postId,tagName);
//                        System.out.println("New tag id is " + postId);
                    }
                }
            }
        }
    }

    private static void getAllPosts() {
        pc.getAll().forEach(System.out::println);
    }

    private static long getPostById(boolean print) {
        System.out.println("Input existing post id \n" +
                "'q' for quit, 'p' for previous menu");

        String string = sc.nextLine();

        switch(string){
            case "q" -> System.exit(0);
            case "p" -> run();
            default  -> {
                try {
                    long id = Long.parseLong(string);
                    if (pc.contains(id)) {
                        if (print) System.out.println(pc.getById(id));
                        return id;
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Wrong number format. Try another.");
                    getPostById(print);
                } catch (IllegalArgumentException e) {
                    System.out.println("No such id in database. Try another");
                    getPostById(print);
                }
            }
        }
        return -1L;//unreachable statement
    }







}
