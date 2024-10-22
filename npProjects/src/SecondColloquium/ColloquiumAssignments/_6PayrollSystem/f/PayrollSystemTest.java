package SecondColloquium.ColloquiumAssignments._6PayrollSystem.f;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// FreelanceEmployee: F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN;
class FreelanceEmployee extends Employee {

    private List<Integer> ticketPoints;
    private double ticketRateByLevel;

    public FreelanceEmployee(String employeeID, String employeeLevel, List<Integer> ticketPoints, double ticketRateByLevel) {
        super(employeeID, employeeLevel);
        this.ticketPoints = ticketPoints;
        this.ticketRateByLevel = ticketRateByLevel;
    }

    public List<Integer> getTicketPoints() {
        return ticketPoints;
    }

    private int getTicketPointsSize() {
        return ticketPoints.size();
    }

    private int getTotalTicketPoints() {
        return ticketPoints.stream()
                .mapToInt(i -> i)
                .sum();
    }

    @Override
    public double getCalculateSalary() {
        return getTotalTicketPoints() * ticketRateByLevel;
    }

    @Override
    public String toString() {
        // Employee ID: 596ed2 Level: level10 Salary: 1290.00 Tickets count: 9 Tickets points: 43
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                getEmployeeID(), getEmployeeLevel(), getCalculateSalary(), getTicketPointsSize(), getTotalTicketPoints());
    }
}

// HourlyEmployee: H;ID;level;hours;
class HourlyEmployee extends Employee {

    private double hours;
    private double hourlyRateByLevel;
    private double regularHours;
    private double overtimeHours;

    public HourlyEmployee(String employeeID, String employeeLevel, double hours, double hourlyRateByLevel) {
        super(employeeID, employeeLevel);
        this.hours = hours;
        this.hourlyRateByLevel = hourlyRateByLevel;
        if (hours <= 40) {
            regularHours = hours;
            overtimeHours = 0;
        } else {
            regularHours = 40;
            overtimeHours = hours - 40;
        }
    }

    public double getHours() {
        return hours;
    }

    @Override
    public double getCalculateSalary() {
        return (regularHours * hourlyRateByLevel + overtimeHours * hourlyRateByLevel * 1.500);
    }

    @Override
    public String toString() {
        // Employee ID: 157f3d Level: level10 Salary: 2390.72 Regular hours: 40.00 Overtime hours: 23.14
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                getEmployeeID(), getEmployeeLevel(), getCalculateSalary(), regularHours, overtimeHours);
    }
}

// FreelanceEmployee: F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN;
// HourlyEmployee:    H;ID;level;hours;
abstract class Employee {

    private String employeeID;
    private String employeeLevel;

    public Employee(String employeeID, String employeeLevel) {
        this.employeeID = employeeID;
        this.employeeLevel = employeeLevel;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getEmployeeLevel() {
        return employeeLevel;
    }

    public static final Comparator<Employee> EMPLOYEE_COMPARATOR = Comparator.comparing(Employee::getCalculateSalary)
            .reversed()
            .thenComparing(Employee::getEmployeeLevel);

    public abstract double getCalculateSalary();


    public abstract String toString();
}

class PayrollSystem {

    private Map<String, Double> hourlyRateByLevel;
    private Map<String, Double> ticketRateByLevel;
    private List<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        this.employees = new ArrayList<>();
    }

    /*
       HourlyEmployee:
       H;ID;level;hours;
       F;72926a;level7;5;6;8;1

       FreelanceEmployee:
       F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN;
       F;c8b1bc;level5;3;8;3;4;6;7;1;3;7;4;7
    */
    public void readEmployeesData(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        List<String> allLines = reader.lines().collect(Collectors.toList());

        for (String line : allLines) {
            String[] parts = line.split(";");

            String employeeType = parts[0];
            String employeeID = parts[1];
            String employeeLevel = parts[2];

            if (employeeType.startsWith("H")) { // type H
                double hours = Double.parseDouble(parts[3]);
                HourlyEmployee newHourlyEmployee = new HourlyEmployee(employeeID, employeeLevel, hours, hourlyRateByLevel.get(employeeLevel));
                employees.add(newHourlyEmployee);
            } else { // type F
                List<Integer> ticketPoints = Arrays.stream(parts)
                        .skip(3)
                        .map(i -> Integer.parseInt(i))
                        .collect(Collectors.toList());
                FreelanceEmployee newFreelanceEmployee = new FreelanceEmployee(employeeID, employeeLevel, ticketPoints, ticketRateByLevel.get(employeeLevel));
                employees.add(newFreelanceEmployee);
            }
        }
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        PrintWriter writer = new PrintWriter(os);

        Map<String, Set<Employee>> levelToEmployees = new LinkedHashMap<>();

        levels.forEach(level -> {
            employees.stream()
                    .filter(employee -> employee.getEmployeeLevel().equals(level))
                    .sorted(Comparator.comparingDouble(Employee::getCalculateSalary)
                            .reversed())
                    .forEach(employee -> {
                        levelToEmployees.putIfAbsent(level, new TreeSet<>());
                        levelToEmployees.get(level).add(employee);
                    });
        });

        return levelToEmployees;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployeesData(System.in);


        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });
    }
}