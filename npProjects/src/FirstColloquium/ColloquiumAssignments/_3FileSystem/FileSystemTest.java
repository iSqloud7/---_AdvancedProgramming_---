package FirstColloquium.ColloquiumAssignments._3FileSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Scanner;

interface IFile extends Comparable<IFile> {

    String getFileName();

    long getFileSize();

    String getFileInfo(int indent);

    void sortBySize();

    long findLargestFile();
}

class FileNameExistsException extends Exception {

    public FileNameExistsException(String fileName, String folderName) {
        super(String.format("There is already a file named %s in the folder %s", fileName, folderName));
    }
}

class IndentPrinter {

    public static String printIndentation(int level) {
        return IntStream.range(0, level)
                .mapToObj(i -> "\t")
                .collect(Collectors.joining());
        // or return " ".repeat(level * 4);
    }
}

class File implements IFile {

    protected String fileName;
    protected long fileSize;

    public File(String fileName) {
        this.fileName = fileName;
    }

    public File(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public long getFileSize() {
        return this.fileSize;
    }

    @Override
    public String getFileInfo(int indent) {
        // String репрезентацијата на една обична датотека е
        // File name [името на фајлот со 10 места порамнето на десно] File size: [големината на фајлот со 10 места пораменета на десно ]

        return String.format("%sFile name: %10s File size: %10d\n",
                IndentPrinter.printIndentation(indent), getFileName(), getFileSize());
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public long findLargestFile() {
        return this.fileSize;
    }

    @Override
    public int compareTo(IFile other) {
        return Long.compare(this.getFileSize(), other.getFileSize());
    }
}

class Folder extends File implements IFile {

    private List<IFile> files;

    public Folder(String fileName) {
        super(fileName);
        this.files = new ArrayList<>();
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public long getFileSize() {
        return files.stream()
                .mapToLong(i -> i.getFileSize()) // IFile::getFileSize()
                .sum();
    }

    @Override
    public String getFileInfo(int indent) {
        // String репрезентацијата на еден директориум е
        // Folder name [името на директориумот со 10 места порамнето на десно] Folder size: [големината на директориумот со 10 места пораменета на десно ]

        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%sFolder name: %10s Folder size: %10d\n",
                IndentPrinter.printIndentation(indent), getFileName(), getFileSize()));

        files.stream()
                .forEach(file -> builder.append(file.getFileInfo(indent + 1)));

        return builder.toString();
    }

    @Override
    public void sortBySize() {
        Comparator<IFile> comparator = Comparator.comparingLong(IFile::getFileSize);
        files.sort(comparator);

        files.stream()
                .sorted()
                .forEach(IFile::sortBySize);
    }

    @Override
    public long findLargestFile() {
        OptionalLong largest = files.stream()
                .mapToLong(IFile::findLargestFile).max();

        if (largest.isPresent()) {
            return largest.getAsLong();
        } else {
            return 0L;
        }
    }

    public void addFile(IFile file) throws FileNameExistsException {
        if (ifNameExists(file.getFileName())) {
            throw new FileNameExistsException(file.getFileName(), this.fileName);
        }

        files.add(file);
    }

    private boolean ifNameExists(String fileName) {
        return files.stream()
                .map(IFile::getFileName)
                .anyMatch(name -> name.equals(fileName));
    }
}

class FileSystem {

    private Folder rootDirectory;

    public FileSystem() {
        this.rootDirectory = new Folder("root");
    }

    public void addFile(IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }

    public long findLargestFile() {
        return rootDirectory.findLargestFile();
    }

    public void sortBySize() {
        rootDirectory.sortBySize();
    }

    @Override
    public String toString() {
        return this.rootDirectory.getFileInfo(0);
    }
}

public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        //file reading from input

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());
    }
}