package Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileManager {

    private static final Path DATA_FOLDER =
            Paths.get("data");

    private static final Path TASK_FILE =
            DATA_FOLDER.resolve("tasks.txt");

    private static final Path REPORT_FILE =
            DATA_FOLDER.resolve("report.txt");


    public static void createDataFolder() throws IOException {

        if (!Files.exists(DATA_FOLDER)) {
            Files.createDirectories(DATA_FOLDER);
        }
    }


    public static ArrayList<StudyTask> loadTasks()
            throws IOException {

        createDataFolder();

        ArrayList<StudyTask> tasks = new ArrayList<>();

        if (!Files.exists(TASK_FILE)) {
            return tasks;
        }

        Set<Integer> ids = new HashSet<>();

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             TASK_FILE,
                             StandardCharsets.UTF_8)) {

            String line;

            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields =
                        line.split("\\|", -1);

                if (fields.length != 7) {

                    throw new IOException(
                            "Invalid data in tasks.txt at line "
                                    + lineNumber);
                }

                try {

                    int id =
                            Integer.parseInt(fields[0]);

                    String type =
                            fields[1];

                    String title =
                            fields[2];

                    String subject =
                            fields[3];

                    String priority =
                            fields[4];

                    int minutes =
                            Integer.parseInt(fields[5]);

                    TaskStatus status =
                            TaskStatus.valueOf(fields[6]);


                    if (id <= 0) {
                        throw new IllegalArgumentException();
                    }


                    if (ids.contains(id)) {

                        throw new IOException(
                                "Duplicate task ID at line "
                                        + lineNumber);
                    }


                    if (title.trim().isEmpty()
                            || subject.trim().isEmpty()) {

                        throw new IOException(
                                "Title or subject is empty at line "
                                        + lineNumber);
                    }


                    if (minutes < 1
                            || minutes > 1440) {

                        throw new IOException(
                                "Invalid estimated minutes at line "
                                        + lineNumber);
                    }


                    if (!priority.equals("Low")
                            && !priority.equals("Medium")
                            && !priority.equals("High")) {

                        throw new IOException(
                                "Invalid priority at line "
                                        + lineNumber);
                    }


                    StudyTask task;

                    if (type.equalsIgnoreCase("Reading")) {

                        task = new ReadingTask(
                                id,
                                title,
                                subject,
                                priority,
                                minutes,
                                status);

                    } else if (type.equalsIgnoreCase("Coding")) {

                        task = new CodingTask(
                                id,
                                title,
                                subject,
                                priority,
                                minutes,
                                status);

                    } else {

                        throw new IOException(
                                "Invalid task type at line "
                                        + lineNumber);
                    }


                    tasks.add(task);

                    ids.add(id);

                } catch (IllegalArgumentException e) {

                    throw new IOException(
                            "Invalid value in tasks.txt at line "
                                    + lineNumber);
                }
            }
        }

        return tasks;
    }


    public static void saveTasks(
            List<StudyTask> tasks)
            throws IOException {

        createDataFolder();

        Path temporaryFile =
                DATA_FOLDER.resolve("tasks_temp.txt");


        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             temporaryFile,
                             StandardCharsets.UTF_8)) {

            for (StudyTask task : tasks) {

                writer.write(
                        task.getId()
                                + "|"
                                + task.getTaskType()
                                + "|"
                                + task.getTitle()
                                + "|"
                                + task.getSubject()
                                + "|"
                                + task.getPriority()
                                + "|"
                                + task.getEstimatedMinutes()
                                + "|"
                                + task.getStatus()
                );

                writer.newLine();
            }
        }


        Files.move(
                temporaryFile,
                TASK_FILE,
                StandardCopyOption.REPLACE_EXISTING);
    }


    public static void saveReport(
            String report)
            throws IOException {

        createDataFolder();

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             REPORT_FILE,
                             StandardCharsets.UTF_8)) {

            writer.write(report);
        }
    }
}
