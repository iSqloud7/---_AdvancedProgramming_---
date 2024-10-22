package SecondColloquium.ColloquiumAssignments._16BlockContainer;

import java.util.*;

class BlockContainer<T extends Comparable<T>> {

    private int blockMaxSizeOfElements;
    private List<TreeSet<T>> blocksSet;

    public BlockContainer(int n) {
        this.blockMaxSizeOfElements = n;
        this.blocksSet = new ArrayList<>();
    }

    public void add(T element) {
        if (blocksSet.isEmpty() ||
                blocksSet.get(blocksSet.size() - 1).size() == blockMaxSizeOfElements) {
            blocksSet.add(new TreeSet<>());
        }

        blocksSet.get(blocksSet.size() - 1).add(element);
    }

    public boolean remove(T element) {
        if (blocksSet.isEmpty()) {
            return false;
        }

        TreeSet<T> lastBlock = blocksSet.get(blocksSet.size() - 1);
        boolean removedElement = lastBlock.remove(element);

        if (lastBlock.isEmpty()) {
            blocksSet.remove(blocksSet.size() - 1);
        }

        return removedElement;
    }

    public void sort() {
        List<T> allElements = new ArrayList<>();

        for (TreeSet<T> block : blocksSet) {
            allElements.addAll(block);
        }

        allElements.sort(null);

        blocksSet.clear();
        for (int i = 0; i < allElements.size(); i += blockMaxSizeOfElements) {
            TreeSet<T> block = new TreeSet<>();
            for (int j = i; j < i + blockMaxSizeOfElements && j < allElements.size(); j++) {
                block.add(allElements.get(j));
            }

            blocksSet.add(block);
        }
    }

    @Override
    public String toString() {
        // [[12, 89], [11, 54], [1, 5], [7, 8], [2, 4], [14]]
        StringBuilder builder = new StringBuilder();

        // builder.append("[");
        for (int i = 0; i < blocksSet.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }

            builder.append("[");
            Iterator<T> iterator = blocksSet.get(i).iterator();
            while(iterator.hasNext()){
                builder.append(iterator.next());
                if(iterator.hasNext()){
                    builder.append(", ");
                }
            }

            builder.append("]");
        }

        // builder.append("]");

        return builder.toString();
    }
}

public class BlockContainerTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for (int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for (int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}