package view;

import controller.PostController;
import controller.TagController;
import controller.WriterController;
import model.Tag;

import java.util.Scanner;

public class TagView {
    private static String tagMessage =
            "Tags\n" +
                    "Input the point to continue...\n" +
                    "1. Create a new tag by name\n" +
                    "2. Get all tags\n" +
                    "3. Get tag by id\n" +
                    "4. Get tag by id\n" +
                    "5. Update tag by id\n" +
                    "6. Update tag by name\n" +
                    "7. Delete tag by id\n" +
                    "8. Delete tag by name\n" +
                    "\t'q' for quit\n" +
                    "\t'p' for previous screen\n";

    private static Scanner sc = new Scanner(System.in);

    private static TagController tc = new TagController();

    public static void run() {
        System.out.println(tagMessage);
        choice();
    }

    private static void choice() {
        switch (sc.nextLine().toLowerCase()) {
            case "1" -> createTag();
            case "2" -> getAllTags();
            case "3" -> getTagById();
            case "4" -> getTagByName();
            case "5" -> updateTagById();
            case "6" -> updateTagByName();
            case "7" -> deleteTagById();
            case "8" -> deleteTagByName();
            case "q" -> System.exit(0);
            case "p" -> StartupView.run();
            default -> System.out.println("Wrong point");
        }
        question();
    }



    private static void question() {
        System.out.print("Show 'TagView' again? 'y' for yes\t");
        switch (sc.nextLine().toLowerCase()) {
            case "y" -> TagView.run();
            default -> System.exit(0);
        }
    }

    private static Tag createTag() {

        long id = tc.getFreeId();

        do {
            System.out.println("Input new tag name\n 'q' for quit");
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            if (tc.contains(s)){
                System.out.println("Already exist. Try another!\n");
            } else {
                Tag t = new Tag(id, s);
                tc.add(t);
                System.out.println("Object with id: " + id + " created");
                return t;
            }
        } while(true);
    }

    private static void getAllTags() {
        tc.getAll().forEach(System.out::println);
    }

    private static Tag getTagById() {
        do {
            System.out.println("Input existed tag id\n 'q' for quit");
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            try {
                long id = Long.parseLong(s);
                if (tc.contains(id)){
                    Tag t = tc.getById(id);
                    System.out.println(tc.toString());
                    return t;
                } else {
                    System.out.println("Doesn't exist. Try another!\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong number format. Repeat please");
            }
        } while(true);
    }

    private static Tag getTagByName() {
        do {
            System.out.println("Input existed tag name\n 'q' for quit");
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

                if (tc.contains(s)){
                    Tag t = tc.getByName(s);
                    System.out.println(tc.toString());
                    return t;
                } else {
                    System.out.println("Doesn't exist. Try another!\n");
                }
        } while(true);
    }

    private static void updateTagById() {
        Long id = getTagById().getId();

        do {
            System.out.println("Input new tag name\n 'q' for quit");
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            if (tc.contains(s)){
                System.out.println("Already exist. Try another!\n");
            } else {
                Tag t = new Tag(id, s);
                tc.update(t);
                System.out.println("Object with id: " + id + " updated.");
                break;
            }
        } while(true);
    }

    private static void updateTagByName() {
        Tag t1 = getTagByName();

        do {
            System.out.println("Input new tag name\n 'q' for quit");
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            if (tc.contains(s)){
                System.out.println("Already exist. Try another!\n");
            } else {
                t1.setName(s);
                tc.update(t1);
                System.out.println("Object with id: " + t1.getId() + " updated.");
                break;
            }
        } while(true);
    }

    private static void deleteTagById(){
        Tag t1 = getTagById();
        tc.delete(t1);
        System.out.println("Object with id: " + t1.getId() + " deleted.");
    }

    private static void deleteTagByName() {
        Tag t1 = getTagByName();
        tc.delete(t1);
        System.out.println("Object with id: " + t1.getId() + " updated.");
    }

}
