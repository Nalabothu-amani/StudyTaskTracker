package Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class TaskService {

    private ArrayList<StudyTask> tasks;

    private int nextId;

    private final String[] priorities =
            {"Low", "Medium", "High"};


    public TaskService() throws IOException {

        tasks = FileManager.loadTasks();

        nextId = calculateNextId();
    }


    private int calculateNextId() {

        int maxId = 0;

        for (StudyTask task : tasks) {

            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }

        return maxId + 1;
    }


    public String[] getPriorities() {
        return priorities;
    }


    public boolean addReadingTask(
            String title,
            String subject,
            String priority,
            int minutes) {

        StudyTask task =
                new ReadingTask(
                        nextId,
                        title,
                        subject,
                        priority,
                        minutes,
                        TaskStatus.PENDING);


        tasks.add(task);

        try {

            FileManager.saveTasks(tasks);

            System.out.println(
                    "Task " + nextId
                            + " saved. Status: PENDING");

            nextId++;

            return true;

        } catch (IOException e) {

            tasks.remove(task);

            System.out.println(
                    "Unable to save task: "
                            + e.getMessage());

            return false;
        }
    }


    public boolean addCodingTask(
            String title,
            String subject,
            String priority,
            int minutes) {

        StudyTask task =
                new CodingTask(
                        nextId,
                        title,
                        subject,
                        priority,
                        minutes,
                        TaskStatus.PENDING);


        tasks.add(task);

        try {

            FileManager.saveTasks(tasks);

            System.out.println(
                    "Task " + nextId
                            + " saved. Status: PENDING");

            nextId++;

            return true;

        } catch (IOException e) {

            tasks.remove(task);

            System.out.println(
                    "Unable to save task: "
                            + e.getMessage());

            return false;
        }
    }


    public void viewAllTasks() {

        if (tasks.isEmpty()) {

            System.out.println("No records found.");

            return;
        }

        System.out.println("\n----- ALL TASKS -----");

        for (StudyTask task : tasks) {

            System.out.println(task);
        }
    }


    public void viewPendingTasks() {

        boolean found = false;

        System.out.println("\n----- PENDING TASKS -----");

        for (StudyTask task : tasks) {

            if (task.getStatus()
                    == TaskStatus.PENDING) {

                System.out.println(task);

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No records found.");
        }
    }


    public void markDone(int id) {

        StudyTask selectedTask = null;

        for (StudyTask task : tasks) {

            if (task.getId() == id) {

                selectedTask = task;

                break;
            }
        }


        if (selectedTask == null) {

            System.out.println(
                    "Task ID not found.");

            return;
        }


        if (selectedTask.getStatus()
                == TaskStatus.DONE) {

            System.out.println(
                    "This task is already done.");

            return;
        }


        selectedTask.markDone();

        try {

            FileManager.saveTasks(tasks);

            System.out.println(
                    "Task saved. Status: DONE");

        } catch (IOException e) {

            System.out.println(
                    "Unable to save task: "
                            + e.getMessage());
        }
    }


    public String generateProgressReport() {

        if (tasks.isEmpty()) {

            return "No tasks yet.\n";
        }


        int[] counts = new int[2];

        long pendingMinutes = 0;

        long doneMinutes = 0;


        HashSet<String> subjects =
                new HashSet<>();


        for (StudyTask task : tasks) {

            String subjectKey =
                    task.getSubject()
                            .trim()
                            .toUpperCase(Locale.ROOT);

            subjects.add(subjectKey);


            if (task.getStatus()
                    == TaskStatus.PENDING) {

                counts[0]++;

                pendingMinutes +=
                        task.getEstimatedMinutes();

            } else {

                counts[1]++;

                doneMinutes +=
                        task.getEstimatedMinutes();
            }
        }


        int total =
                counts[0] + counts[1];


        double completion =
                counts[1] * 100.0 / total;


        StringBuilder report =
                new StringBuilder();


        report.append(
                "\n===== PROGRESS REPORT =====\n");

        report.append(
                "Total Tasks: ")
                .append(total)
                .append("\n");

        report.append(
                "Pending Tasks: ")
                .append(counts[0])
                .append("\n");

        report.append(
                "Done Tasks: ")
                .append(counts[1])
                .append("\n");

        report.append(
                "Distinct Subjects: ")
                .append(subjects.size())
                .append("\n");

        report.append(
                "Pending Estimated Minutes: ")
                .append(pendingMinutes)
                .append("\n");

        report.append(
                "Done Estimated Minutes: ")
                .append(doneMinutes)
                .append("\n");

        report.append(
                "Total Estimated Minutes: ")
                .append(
                        pendingMinutes
                                + doneMinutes)
                .append("\n");

        report.append(
                String.format(
                        "Completion: %.2f%%\n",
                        completion));


        return report.toString();
    }


    public void showProgress() {

        String report =
                generateProgressReport();

        System.out.println(report);
    }


    public void saveProgressReport() {

        if (tasks.isEmpty()) {

            System.out.println(
                    "No tasks yet.");

            return;
        }

        try {

            FileManager.saveReport(
                    generateProgressReport());

            System.out.println(
                    "Report saved successfully.");

        } catch (IOException e) {

            System.out.println(
                    "Unable to save report: "
                            + e.getMessage());
        }
    }
}