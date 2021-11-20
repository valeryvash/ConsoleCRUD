package view;

import java.util.Scanner;

public class StartupView {

    private static String startUpMessage =
            "Welcome to the 'ConsoleCRUD' app!\n" +
                    "Input the point for continue...\n" +
                    "1. Writers\n" +
                    "2. Posts\n" +
                    "3. Tags\n" +
                    "\t 'q' for quit";

    static Scanner sc = new Scanner(System.in);

    public static void run() {
        System.out.println(startUpMessage);
        choice();
    }

    private static void choice() {
        switch (sc.nextLine().toLowerCase()) {
            case "1" -> WriterView.run();
            case "2" -> PostView.run();
            case "3" -> TagView.run();

            case "q" -> System.out.println("Bye!");
            default -> System.out.println("Try another one");
        }
        question();
    }

    private static void question() {
        System.out.print("Show 'StartUpView' again? 'y' for yes\t");
        switch (sc.nextLine().toLowerCase()) {
            case "y" -> StartupView.run();
            default -> System.exit(0);
        }
    }
}
