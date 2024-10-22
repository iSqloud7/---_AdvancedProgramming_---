package FirstColloquium.ColloquiumAssignments._8ArchiveStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NonExistingItemException extends Exception {

    public NonExistingItemException(String message) {
        super(message);
    }
}

class SpecialArchive extends Archive {

    private int maxOpen;
    private int currentOpenDate;

    public SpecialArchive(int archiveID, int maxOpen) {
        super(archiveID);
        this.maxOpen = maxOpen;
        this.currentOpenDate = 0;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    public void setMaxOpen(int maxOpen) {
        this.maxOpen = maxOpen;
    }

    @Override
    public String open(LocalDate date) {
        // Item [id] cannot be opened more than [maxOpen] times
        if (currentOpenDate >= maxOpen) {
            return String.format("Item %d cannot be opened more than %d times",
                    getArchiveID(), getMaxOpen());
        }

        currentOpenDate++;
        return String.format("Item %d opened at %s",
                getArchiveID(), date);
    }
}

class LockedArchive extends Archive {

    private LocalDate dateToOpen;

    public LockedArchive(int archiveID, LocalDate dateToOpen) {
        super(archiveID);
        this.dateToOpen = dateToOpen;
    }

    public LocalDate getDateToOpen() {
        return dateToOpen;
    }

    public void setDateToOpen(LocalDate dateToOpen) {
        this.dateToOpen = dateToOpen;
    }

    @Override
    public String open(LocalDate date) {
        // Item [id] cannot be opened before [date]
        if (date.isBefore(getDateToOpen())) {
            return String.format("Item %d cannot be opened before %s",
                    getArchiveID(), getDateToOpen());
        }

        // Item [id] opened at [date]
        return String.format("Item %d opened at %s",
                getArchiveID(), date);
    }
}

abstract class Archive {

    private int archiveID;
    private LocalDate dateArchived;

    public Archive(int archiveID) {
        this.archiveID = archiveID;
    }

    public int getArchiveID() {
        return archiveID;
    }

    public void setArchiveID(int archiveID) {
        this.archiveID = archiveID;
    }

    public LocalDate getDateArchived() {
        return dateArchived;
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }

    public abstract String open(LocalDate date);
}

class ArchiveStore {

    private List<Archive> archives;
    private StringBuilder builder;

    public ArchiveStore() {
        this.archives = new ArrayList<>();
        this.builder = new StringBuilder();
    }

    // Item [id] archived at [date]
    public void archiveItem(Archive item, LocalDate date) {
        item.setDateArchived(date);
        archives.add(item);
        builder.append(String.format("Item %d archived at %s\n",
                item.getArchiveID(), date));
    }

    private Archive findArchiveById(int ID) {
        for (Archive archive : archives) {
            if (archive.getArchiveID() == ID) {
                return archive;
            }
        }

        return null;
    }

    // Item [id] opened at [date]
    public void openItem(int id, LocalDate date) throws NonExistingItemException {
        Archive archive = findArchiveById(id);
        if (archive == null) {
            // Item with id [id] doesn't exist
            throw new NonExistingItemException(String.format("Item with id %d doesn't exist", id));
        }

        String result = archive.open(date);
        builder.append(result).
                append("\n");
    }

    public String getLog() {
        return builder.toString();
    }
}

public class ArchiveStoreTest {

    public static void main(String[] args) {

        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}