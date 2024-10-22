package SecondColloquium.ColloquiumAssignments._9Stadium;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

class SeatTakenException extends Exception {

    public SeatTakenException(String message) {
        super(message);
    }
}

class SeatNotAllowedException extends Exception {

    public SeatNotAllowedException(String message) {
        super(message);
    }
}

class Sector {

    private String sectorCode;
    private int numOfSeats;
    private Map<Integer, Boolean> isSeatTaken;
    private int seatType;
    private int takenSeat;

    public Sector(String sectorCode, int numOfSeats) {
        this.sectorCode = sectorCode;
        this.numOfSeats = numOfSeats;
        this.isSeatTaken = new HashMap<>();

        for (int i = 0; i <= numOfSeats; i++) {
            isSeatTaken.put(i, false);
        }

        this.seatType = 0;
        this.takenSeat = 0;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public void buyTicket(int seat, int type) throws SeatTakenException, SeatNotAllowedException { // (type, 0 - неутрален, 1 - домашен, 2 - гостински)
        if (isSeatTaken.get(seat)) {
            throw new SeatTakenException("SeatTakenException");
        }

        if (this.seatType == 0) {
            this.seatType = type;
        } else if (type == 1 && this.seatType == 2) {
            throw new SeatNotAllowedException("SeatNotAllowedException");
        } else if (type == 2 && this.seatType == 1) {
            throw new SeatNotAllowedException("SeatNotAllowedException");
        }

        isSeatTaken.put(seat, true);
        takenSeat++;
    }

    public int getNumberOfAvailableSeats() {
        return getNumOfSeats() - takenSeat;
    }

    public double full() {
        return (double) takenSeat / numOfSeats * 100.00;
    }

    /* Format:
       H	88/100	12.0%
       A	85/100	15.0%
       E	85/100	15.0%
       F	85/100	15.0%
       D	84/100	16.0%
       C	82/100	18.0%
       B	80/100	20.0%
       G	76/100	24.0%
    */
    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%",
                getSectorCode(), getNumberOfAvailableSeats(), numOfSeats, full());
    }
}

class Stadium {

    private String stadiumName;
    private Map<String, Sector> sectorsByStadium;

    public Stadium(String name) {
        this.stadiumName = name;
        this.sectorsByStadium = new HashMap<>();
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void createSectors(String[] sectorNames, int[] sizes) {
        for (int i = 0; i < sectorNames.length; i++) {
            Sector newSector = new Sector(sectorNames[i], sizes[i]);
            sectorsByStadium.put(sectorNames[i], newSector);
        }
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException { // (type, 0 - неутрален, 1 - домашен, 2 - гостински)
        sectorsByStadium.get(sectorName).buyTicket(seat, type);
    }

    public void showSectors() {
        sectorsByStadium.values().stream()
                .sorted(Comparator.comparing(Sector::getNumberOfAvailableSeats)
                        .reversed()
                        .thenComparing(Sector::getSectorCode))
                .forEach(sector -> System.out.println(sector));
    }
}

public class StadiumTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}