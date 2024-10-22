package LaboratoryExercises_NP.Lab_VI.IntegerList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class IntegerList {

    private List<Integer> list;

    public IntegerList() {
        this.list = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        this.list = new ArrayList<>();
        IntStream.range(0, numbers.length).forEach(i -> list.add(numbers[i]));
    }

    public void add(int e, int i) {
        if (i >= list.size()) {
            IntStream.range(list.size(), i).forEach(x -> list.add(x, 0));
        }

        list.add(i, e);
    }

    public int remove(int i) {
        if (i < 0 || i >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return list.remove(i);
    }

    public void set(int e, int i) {
        if (i < 0 || i >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        list.set(i, e);
    }

    public int get(int i) {
        if (i < 0 || i >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public int count(int e) {
        return (int) list.stream().filter(i -> i == e).count();
    }

    public void removeDuplicates() {
        Collections.reverse(list);
        list = list.stream().distinct().collect(Collectors.toList());
        Collections.reverse(list);
    }

    public int sumFirst(int k) {
        return list.stream().limit(k).mapToInt(i -> i).sum();
    }

    public int sumLast(int k) {
        return list.stream().skip((long) list.size() - k).mapToInt(i -> i).sum();
    }

    public void shiftRight(int i, int k) {
        if (i < 0 || i >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        int a = list.get(i);

        list.remove(i);

        list.add((i + k) % (list.size() + 1), a);
    }

    public void shiftLeft(int i, int k) {
        if (i < 0 || i >= list.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        int a = list.get(i);

        list.remove(i);

        list.add(Math.floorMod(i - k, list.size() + 1), a);
    }

    public IntegerList addValue(int a) {
        IntegerList il = new IntegerList();

        il.list = list.stream().map(i -> i + a).collect(Collectors.toList());

        return il;
    }
}

public class IntegerListTest {

    public static void main(String[] args) {

        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }
}