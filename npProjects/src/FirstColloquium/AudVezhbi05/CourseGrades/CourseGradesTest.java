package FirstColloquium.AudVezhbi05.CourseGrades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CourseGradesTest {

    public static void main(String[] args) {

        Course course = new Course();

        File inputFile = new File("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\FirstColloquium\\AudVezhbi05\\CourseGrades\\Input.txt");
        File outputFile = new File("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\FirstColloquium\\AudVezhbi05\\CourseGrades\\Output.txt");

        try {
            course.readData(new FileInputStream(inputFile));

            System.out.println("=== Printing sorted students to screen. ===");
            course.printSortedData(System.out);

            System.out.println("=== Printing detailed report to file -> " + "Output.txt" + ". ===");
            course.printDetailedData(new FileOutputStream(outputFile));

            System.out.println("=== Printing grade distribution to screen. ===");
            course.printDistribution(System.out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
