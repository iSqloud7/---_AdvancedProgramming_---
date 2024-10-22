package FirstColloquium.AudVezhbi01.StaticVSNonstatic;

public class FirstClass {

    private int a;

    private static int rand() {
        return 3;
    }

    private double b;

    public static void main(String[] args) {

        FirstClass fc = new FirstClass();

        // if static a == 3 and static rand() returns 3, all instances have the same value for a

        // if some instance change value for a into 7, all other instances have this value

        // if some instance change value for b into 4, all other instances have 0 or undefined value

        // static method can only use static variable otherwise it cannot

        System.out.println(fc.a);

        System.out.println("This is the println method.");
        System.out.print("This is the print method.");
        System.out.print(" And has no space.");
        System.out.printf("\n%d", 50);
    }
}
