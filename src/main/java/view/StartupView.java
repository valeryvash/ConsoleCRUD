package view;

import java.util.Locale;
import java.util.Scanner;

public class StartupView {

    private static String startUpMessage =
            "Welcome to the 'ConsoleCRUD' app!\n" +
                    "Input the point for continue...\n" +
                    "1. Writers\n" +
                    "2. Posts\n" +
                    "3. Tags\n" +
                    "\tor 'q' for quit";

    static Scanner sc = new Scanner(System.in);

    public static void run() {
        System.out.println(startUpMessage);
        choice();
    }

    private static void choice() {
        switch (sc.nextLine().toLowerCase()) {
            case "1" -> WritersView.run();
//            case "2" -> PostsView.run();
//            case "3" -> TagsView.run();
            case "q" -> System.out.println("Bye!");
            default -> {
                System.out.println("Try another one");
                choice();
            }
        }
    }
}
