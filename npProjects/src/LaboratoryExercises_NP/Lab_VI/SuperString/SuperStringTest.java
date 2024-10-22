package LaboratoryExercises_NP.Lab_VI.SuperString;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

class SuperString {

    private LinkedList<String> list;
    private Stack<String> stack;

    public SuperString() {
        this.list = new LinkedList<>();
        this.stack = new Stack<>();
    }

    public void append(String s) {
        list.addLast(s);
        stack.push(s);
    }

    public void insert(String s) {
        list.addFirst(s);
        stack.push(s);
    }

    public boolean contains(String s) {
        return toString().contains(s);
    }

    public void reverse() {
        Collections.reverse(list);

        list = list.stream().map(i -> new StringBuilder(i).reverse().toString()).collect(Collectors.toCollection(LinkedList::new));
    }

    public void removeLast(int k) {
        for (int i = 0; i < k; i++) {
            StringBuilder sb = new StringBuilder(stack.pop());

            list.remove(sb.toString());
            list.remove(sb.reverse().toString());
        }
    }

    @Override
    public String toString() {
        return String.join("", list);
    }
}

public class SuperStringTest {

    public static void main(String[] args) {

        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            SuperString s = new SuperString();
            while (true) {
                int command = jin.nextInt();
                if (command == 0) {//append(String s)
                    s.append(jin.next());
                }
                if (command == 1) {//insert(String s)
                    s.insert(jin.next());
                }
                if (command == 2) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if (command == 3) {//reverse()
                    s.reverse();
                }
                if (command == 4) {//toString()
                    System.out.println(s);
                }
                if (command == 5) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if (command == 6) {//end
                    break;
                }
            }
        }
    }
}