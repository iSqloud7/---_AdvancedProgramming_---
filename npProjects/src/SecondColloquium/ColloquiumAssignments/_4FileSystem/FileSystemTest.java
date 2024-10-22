package SecondColloquium.ColloquiumAssignments._4FileSystem;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

class File implements Comparable<File> {

    private String fileName;
    private int fileSize;
    private LocalDateTime dateOfCreation;

    public File(String fileName, int fileSize, LocalDateTime dateOfCreation) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.dateOfCreation = dateOfCreation;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    @Override
    public int compareTo(File other) {
        int dateComparison = this.getDateOfCreation().compareTo(other.getDateOfCreation());

        if (dateComparison == 0) {
            int nameComparison = this.getFileName().compareTo(other.getFileName());
            if (nameComparison == 0) {
                return Integer.compare(this.getFileSize(), other.getFileSize());
            }

            return nameComparison;
        }

        return dateComparison;
    }

    @Override
    public String toString() {
        // %-10[name] %5[size]B %[createdAt]
        return String.format("%-10s %5dB %s",
                getFileName(), getFileSize(), getDateOfCreation());
    }
}

class FileSystem {

    // T:cvl:326:2688
    private Map<Character, Set<File>> filesByFolder;

    public FileSystem() {
        this.filesByFolder = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        File newFile = new File(name, size, createdAt);

        filesByFolder.putIfAbsent(folder, new TreeSet<>());
        filesByFolder.get(folder).add(newFile);
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        List<File> hiddenFiles = new ArrayList<>();

        for (Set<File> files : filesByFolder.values()) {
            files.stream()
                    .filter(file -> file.getFileName().startsWith(".") &&
                            file.getFileSize() < size)
                    .forEach(file -> hiddenFiles.add(file));
        }

        return hiddenFiles;
    }

    public int totalSizeOfFilesFromFolders(List<Character> folders) {
        return folders.stream()
                .filter(filesByFolder::containsKey)
                .flatMap(folder -> filesByFolder.get(folder).stream())
                .mapToInt(File::getFileSize)
                .sum();
    }

    public Map<Integer, Set<File>> byYear() {
        Map<Integer, Set<File>> filesByYear = new HashMap<>();

        for (Set<File> files : filesByFolder.values()) {
            for (File file : files) {
                int year = file.getDateOfCreation().getYear();
                filesByYear.putIfAbsent(year, new TreeSet<>());
                filesByYear.get(year).add(file);
            }
        }

        return filesByYear;
    }

    public Map<String, Long> sizeByMonthAndDay() {
        Map<String, Long> sizeByMonthAndDay = new TreeMap<>((key1, key2) -> {
            String[] parts1 = key1.split("-");
            String[] parts2 = key2.split("-");

            int month1 = Month.valueOf(parts1[0].toUpperCase()).getValue();
            int month2 = Month.valueOf(parts2[0].toUpperCase()).getValue();

            if (month1 != month2) {
                return Integer.compare(month1, month2);
            }

            int day1 = Integer.parseInt(parts1[1]);
            int day2 = Integer.parseInt(parts2[1]);

            return Integer.compare(day1, day2);
        });

        for (Set<File> files : filesByFolder.values()) {
            for (File file : files) {
                String month = file.getDateOfCreation().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
                String key = String.format("%s-%d", month, file.getDateOfCreation().getDayOfMonth());
                sizeByMonthAndDay.put(key, sizeByMonthAndDay.getOrDefault(key, 0L) + file.getFileSize());
            }
        }

        return sizeByMonthAndDay;
    }
}

public class FileSystemTest {

    public static void main(String[] args) {

        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}