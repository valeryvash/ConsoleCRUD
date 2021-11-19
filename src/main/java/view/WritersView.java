package view;

import controller.WriterController;
import model.Writer;

import java.util.Scanner;
import java.util.stream.Collectors;

public class WritersView {

    private static String writersMessage =
            "Writers\n" +
                    "Input the point for continue...\n" +
                    "1. Create a new writer\n" +
                    "2. Get all writers list\n" +
                    "3. Get writer by 'id'\n" +
                    "4. Get posts by writer id\n" +
                    "5. Update writer by id\n" +
                    "6. Delete writer by id\n" +
                    "\t'q' for quit\n" +
                    "\t'p' for previous screen\n";

    private static Scanner sc = new Scanner(System.in);

    private static WriterController wc = new WriterController();

    public static void run() {
            System.out.println(writersMessage);
            choice();
    }

    private static void choice() {
        switch (sc.nextLine().toLowerCase()) {
            case "1" -> add();
            case "2" -> getAll();
            case "3" -> getById(true);
            case "4" -> getPostsByWriterId();
            case "5" -> updateById();
            case "6" -> deleteById();
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
        System.out.print("Continue? y/n\t");
        switch (sc.nextLine().toLowerCase()) {
            case "y" -> run();
            case "n" -> System.exit(0);
            default -> question();
        }
    }

    private static void add() {
        System.out.println("Input new writer name\n " +
                "'q' for quit, 'p' for previous menu");

        String name = sc.nextLine().toLowerCase();

        switch(name){
            case "q" -> System.exit(0);
            case "p" -> run();
            default -> {
                if (wc.contains(name)) {
                    System.out.println("Name already exist. Try another");
                    add();
                }
                long id = wc.add(name);
                System.out.println("New writer id is " + id);
            }
        }
    }

    private static void getAll() {
        wc.getAll().forEach(System.out::println);
    }

    private static long getById(boolean print) {
        System.out.println("Input existing writer id \n" +
                "'q' for quit, 'p' for previous menu");

        String string = sc.nextLine();

        switch(string){
            case "q" -> System.exit(0);
            case "p" -> run();
            default  -> {
                try {
                    long id = Long.parseLong(string);
                    if (wc.contains(id)) {
                        if (print) System.out.println(wc.getById(id));
                        return id;
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Wrong number format. Try another.");
                    getById(print);
                } catch (IllegalArgumentException e) {
                    System.out.println("No such id in database. Try another");
                    getById(print);
                }
            }
        }
        return -1L;//unreachable statement
    }

    private static void getPostsByWriterId() {
        long id = getById(false);
        wc.getWriterPosts(id).forEach(System.out::println);
    }

    private static void updateById() {
        long id = getById(true);

        repeat:
        {
            while (true) {
                System.out.println("Enter new name:\n" +
                        "'q' for quit, 'p' for previous menu\"");
                String newName = sc.nextLine();
                switch (newName) {
                    case "q" -> System.exit(0);
                    case "p" -> {
                        run();
                        break repeat;
                    }
                    case "" -> System.out.println("Cannot be used as name");
                    default -> {
                        wc.update(new Writer(
                                id,
                                newName,
                                wc.getWriterPosts(id).collect(Collectors.toList())));
                        break repeat;
                    }
                }
            }
        }
    }

    public static void deleteById() {
        long id = getById(true);

        repeat:
        {
            while (true){
                System.out.println("User will be deleted. Are you sure? y/n");
                switch (sc.nextLine().toLowerCase()) {
                    case "y" -> {
                        wc.delete(id);
                        break repeat;
                    }
                    case "n" -> {
                        run();
                        break repeat;
                    }
                   default -> {}
                }
            }
        }
    }



}
