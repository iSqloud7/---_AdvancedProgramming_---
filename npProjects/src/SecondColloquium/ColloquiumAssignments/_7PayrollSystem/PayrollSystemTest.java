//package SecondColloquium.ColloquiumAssignments._7PayrollSystem;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//class FreelanceEmployee extends Employee {
//
//}
//
//class HourlyEmployee extends Employee {
//
//}
//
//class Employee {
//
//}
//
//class PayrollSystem {
//
//    private Map<String, Double> hourlyRateByLevel;
//    private Map<String, Double> ticketRateByLevel;
//
//    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
//        this.hourlyRateByLevel = hourlyRateByLevel;
//        this.ticketRateByLevel = ticketRateByLevel;
//    }
//
//    /*
//       H;Stefan;level1;45.0 10%
//       F;Gjorgji;level2;1;5;10;6
//    */
//    public Employee createEmployee(String line) {
//        String[] parts = line.split(";");
//
//        String employeeType = parts[0];
//        String employeeID = parts[1];
//        String employeeLevel = parts[2];
//
//
//    }
//
//    public Map<String, Double> getOvertimeSalaryForLevels() {
//
//    }
//
//    public void printStatisticsForOvertimeSalary() {
//
//    }
//
//    public Map<String, Integer> ticketsDoneByLevel() {
//
//    }
//
//    public Collection<Employee> getFirstNEmployeesByBonus(int n) {
//
//    }
//}
//
//public class PayrollSystemTest2 {
//
//    public static void main(String[] args) {
//
//        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
//        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
//        for (int i = 1; i <= 10; i++) {
//            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
//            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
//        }
//
//        Scanner sc = new Scanner(System.in);
//
//        int employeesCount = Integer.parseInt(sc.nextLine());
//
//        PayrollSystem ps = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
//        Employee emp = null;
//        for (int i = 0; i < employeesCount; i++) {
//            try {
//                emp = ps.createEmployee(sc.nextLine());
//            } catch (BonusNotAllowedException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//        int testCase = Integer.parseInt(sc.nextLine());
//
//        switch (testCase) {
//            case 1: //Testing createEmployee
//                if (emp != null)
//                    System.out.println(emp);
//                break;
//            case 2: //Testing getOvertimeSalaryForLevels()
//                ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
//                    System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
//                });
//                break;
//            case 3: //Testing printStatisticsForOvertimeSalary()
//                ps.printStatisticsForOvertimeSalary();
//                break;
//            case 4: //Testing ticketsDoneByLevel
//                ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
//                    System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
//                });
//                break;
//            case 5: //Testing getFirstNEmployeesByBonus (int n)
//                ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
//                break;
//        }
//    }
//}