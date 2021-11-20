package view;

import controller.WriterController;
import model.Writer;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WriterView {

    private static String writersMessage =
            "Writers\n" +
                    "Input the point for continue...\n" +
                    "1. Create a new writer\n" +
                    "2. Get all writers list\n" +
                    "3. Get writer by 'id'\n" +
                    "4. Get posts by writer id\n" +
                    "5. Get posts by writer name\n" +
                    "6. Update writer name by id\n" +
                    "7. Update writer name by name\n" +
                    "8. Delete writer by id\n" +
                    "9. Delete writer by name\n" +
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
            case "1" -> createWriter();
            case "2" -> getAll();
            case "3" -> printById();
            case "4" -> getPostsByWriterId();
            case "5" -> getPostsByWriterName();
            case "6" -> updateNameById();
            case "7" -> updateNameByName();
            case "8" -> deleteNameById();

            case "q" -> System.exit(0);
            case "p" -> StartupView.run();
            default -> System.out.println("Wrong point");
        }
        question();
    }

    private static void question() {
        System.out.print("Show 'WriterView' again? 'y' for yes\t");
        switch (sc.nextLine().toLowerCase()) {
            case "y" -> run();
            default -> System.exit(0);
        }
    }

    private static void createWriter() {
        long id = wc.getFreeId();

        Writer w;

        System.out.println("Input new writer id.\n 'q' for quit");

        do {
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            if (!wc.contains(s)) {

                w = new Writer(
                        id,
                        s,
                        new ArrayList<>()
                );
                break;
            } else {
                System.out.println("Writer name already exist. Try another");
            }
        } while (true);

        wc.add(w);
        System.out.println("New writer created with id: " + w.getId());
    }

    private static void getAll(){
        wc.getAll().forEach(System.out::println);
    }

    private static void printById() {
        System.out.println(getById());
    }

    private static Writer getById() {
        System.out.println("Input existed writer id\n 'q' for quit");

        do {
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            try {
                long id = Long.parseLong(s);
                if (!wc.contains(id)) throw new IllegalArgumentException();
                Writer w = wc.getById(id);
                return w;
            } catch (NumberFormatException e) {
                System.out.println("Input the numeric string please");
            } catch (IllegalArgumentException e) {
                System.out.println("Such writer doesn't exist! Try another id");
            }
        } while (true);
    }

    private static void getPostsByWriterId() {
        Writer w = getById();
        wc.getWriterPosts(w.getId()).forEach(System.out::println);
    }

    private static void getPostsByWriterName() {
        System.out.println("Input existed writer name\n 'q' for quit");

        do {
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            try {
                if (!wc.contains(s)) throw new IllegalArgumentException();
                System.out.println(wc.getByName(s));
            } catch (IllegalArgumentException e) {
                System.out.println("Such writer doesn't exist! Try another name");
            }
        } while (true);
    }

    private static void updateNameById() {
        Writer w = getById();

        System.out.println("Input new writer name\n 'q' for quit");

        do {
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            try {
                if (wc.contains(s)) {
                    throw new IllegalArgumentException();
                } else {
                    w.setName(s);
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Such writer doesn't exist! Try another name");
            }
        } while (true);
        wc.update(w);
        System.out.println("Writer updated");
    }


    private static void updateNameByName() {
        Writer w = getByName();

        System.out.println("Input new writer name.\n 'q' for quit");

        do {
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            try {
                if (wc.contains(s)) {
                    throw new IllegalArgumentException();
                } else {
                    w.setName(s);
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Such writer doesn't exist! Try another name");
            }
        } while (true);
        wc.update(w);
        System.out.println("Writer updated");
    }

    private static Writer getByName() {
        System.out.println("Input existed writer name\n 'q' for quit");
        do {
            String s = sc.nextLine();

            if (s.equals("q")) System.exit(0);

            if (wc.contains(s)) {
                return wc.getByName(s);
            } else {
                System.out.println("Such writer doesn't exist! Try another name");
            }
        } while (true);
    }

    private static void deleteNameById() {
        Writer w = getById();
        wc.delete(w.getId());
        System.out.println("Writer deleted");
    }

    private static void deleteNameByName() {
        Writer w = getByName();
        wc.delete(w.getId());
        System.out.println("Writer deleted");
    }



}
