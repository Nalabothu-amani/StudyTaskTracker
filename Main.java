package Student;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static TaskService service;


    public static void main(String[] args) {

        try {

            service = new TaskService();

        } catch (IOException e) {

            System.out.println(
                    "Unable to load tasks.");

            System.out.println(
                    e.getMessage());

            return;
        }


        int choice;

        do {

            showMenu();

            choice =
                    readInteger(
                            "Enter your choice: ");


            switch (choice) {

                case 1:
                    addReadingTask();
                    break;

                case 2:
                    addCodingTask();
                    break;

                case 3:
                    service.viewAllTasks();
                    break;

                case 4:
                    service.viewPendingTasks();
                    break;

                case 5:
                    markTaskDone();
                    break;

                case 6:
                    progressMenu();
                    break;

                case 0:
                    System.out.println(
                            "Thank you for using Study Task Tracker.");
                    break;

                default:
                    System.out.println(
                            "Invalid menu choice.");
            }

        } while (choice != 0);

        scanner.close();
    }


    public static void showMenu() {

        System.out.println(
                "\n==============================");

        System.out.println(
                "       STUDY TASK MENU");

        System.out.println(
                "==============================");

        System.out.println(
                "1. Add reading task");

        System.out.println(
                "2. Add coding task");

        System.out.println(
                "3. View all tasks");

        System.out.println(
                "4. View pending tasks");

        System.out.println(
                "5. Mark task done");

        System.out.println(
                "6. View progress report");

        System.out.println(
                "0. Exit");

        System.out.println(
                "==============================");
    }


    private static void addReadingTask() {

        String title =
                readRequiredText(
                        "Title: ");

        String subject =
                readRequiredText(
                        "Subject: ");

        String priority =
                readPriority();

        int minutes =
                readMinutes();

        service.addReadingTask(
                title,
                subject,
                priority,
                minutes);
    }


    private static void addCodingTask() {

        String title =
                readRequiredText(
                        "Title: ");

        String subject =
                readRequiredText(
                        "Subject: ");

        String priority =
                readPriority();

        int minutes =
                readMinutes();

        service.addCodingTask(
                title,
                subject,
                priority,
                minutes);
    }


    private static String readRequiredText(
            String message) {

        while (true) {

            System.out.print(message);

            String value =
                    scanner.nextLine().trim();


            if (value.isEmpty()) {

                System.out.println(
                        "Value cannot be empty.");

                continue;
            }


            if (value.contains("|")) {

                System.out.println(
                        "Character | is not allowed.");

                continue;
            }


            return value;
        }
    }


    private static String readPriority() {

        String[] priorities =
                service.getPriorities();


        while (true) {

            System.out.println(
                    "Priority:");

            System.out.println(
                    "1. Low");

            System.out.println(
                    "2. Medium");

            System.out.println(
                    "3. High");


            int choice =
                    readInteger(
                            "Enter priority choice: ");


            if (choice >= 1
                    && choice <= 3) {

                return priorities[
                        choice - 1];
            }


            System.out.println(
                    "Choose priority from 1 to 3.");
        }
    }


    private static int readMinutes() {

        while (true) {

            int minutes =
                    readInteger(
                            "Estimated minutes (1-1440): ");


            if (minutes >= 1
                    && minutes <= 1440) {

                return minutes;
            }


            System.out.println(
                    "Minutes must be between 1 and 1440.");
        }
    }


    private static void markTaskDone() {

        int id =
                readInteger(
                        "Task ID: ");

        service.markDone(id);
    }


    private static void progressMenu() {

        service.showProgress();


        System.out.print(
                "Save report? (yes/no): ");

        String answer =
                scanner.nextLine().trim();


        if (answer.equalsIgnoreCase("yes")
                || answer.equalsIgnoreCase("y")) {

            service.saveProgressReport();
        }
    }


    private static int readInteger(
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number.");
            }
        }
    }
}