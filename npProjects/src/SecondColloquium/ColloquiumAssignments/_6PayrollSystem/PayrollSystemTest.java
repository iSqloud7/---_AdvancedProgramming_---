package SecondColloquium.ColloquiumAssignments._6PayrollSystem;

import java.io.*;
import java.util.*;

// FreelanceEmployee: F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN;
class FreelanceEmployee extends Employee {

    private List<Double> ticketPoints;

    public FreelanceEmployee(String employeeID, String employeeLevel, List<Double> ticketPoints) {
        super(employeeID, employeeLevel);
        this.ticketPoints = ticketPoints;
    }

    @Override
    public void calculateSalary(Map<String, Double> ticketRateByLevel) {
        double rate = ticketRateByLevel.get(getEmployeeLevel());

        double salary = getTicketsPointsSum() * rate;

        setSalary(salary);
    }

    private int getTicketsCount() {
        return ticketPoints.size();
    }

    private double getTicketsPointsSum() {
        return ticketPoints.stream()
                .mapToDouble(i -> i.doubleValue())
                .sum();
    }

    @Override
    public String toString() {
        // Employee ID: 596ed2 Level: level10 Salary: 1290.00 Tickets count: 9 Tickets points: 43
        return super.toString() + String.format(" Tickets count: %d Tickets points: %.0f",
                getTicketsCount(),
                getTicketsPointsSum());
    }
}

// HourlyEmployee: H;ID;level;hours;
class HourlyEmployee extends Employee {

    private double hours;

    public HourlyEmployee(String employeeID, String employeeLevel, double hours) {
        super(employeeID, employeeLevel);
        this.hours = hours;
    }

    @Override
    public void calculateSalary(Map<String, Double> hourlyRateByLevel) {
        double rate = hourlyRateByLevel.get(getEmployeeLevel());
        double salary;

        if (hours <= 40) {
            salary = hours * rate;
        } else {
            salary = 40 * rate + (hours - 40) * rate * 1.5;
        }

        setSalary(salary);
    }

    @Override
    public String toString() {
        double regularHours = Math.min(40, hours);
        double overtimeHours = Math.max(0, hours - 40);
        // Employee ID: 157f3d Level: level10 Salary: 2390.72 Regular hours: 40.00 Overtime hours: 23.14
        return super.toString() + String.format(" Regular hours: %.2f Overtime hours: %.2f",
                regularHours,
                overtimeHours);
    }
}

abstract class Employee {

    private String employeeID;
    private String employeeLevel;
    private double salary;

    public Employee(String employeeID, String employeeLevel) {
        this.employeeID = employeeID;
        this.employeeLevel = employeeLevel;
        this.salary = 0.0F;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getEmployeeLevel() {
        return employeeLevel;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public abstract void calculateSalary(Map<String, Double> rateByLevel);

    @Override
    public String toString() {
        // Employee ID: 157f3d Level: level10 Salary: 2390.72
        return String.format("Employee ID: %s Level: %s Salary: %.2f",
                getEmployeeID(),
                getEmployeeLevel(),
                getSalary());
    }
}

class PayrollSystem {

    private Map<String, Double> hourlyRateByLevel;
    private Map<String, Double> ticketRateByLevel;
    List<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        this.employees = new ArrayList<>();
    }

    public Map<String, Double> getHourlyRateByLevel() {
        return hourlyRateByLevel;
    }

    public Map<String, Double> getTicketRateByLevel() {
        return ticketRateByLevel;
    }

    /*
           HourlyEmployee:
           H;ID;level;hours;
           F;72926a;level7;5;6;8;1

           FreelanceEmployee:
           F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN;
           F;c8b1bc;level5;3;8;3;4;6;7;1;3;7;4;7
        */
    public void readEmployees(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");

            String typeOfEmployee = parts[0];
            String employeeID = parts[1];
            String employeeLevel = parts[2];

            if (typeOfEmployee.equals("H")) {
                double hours = Double.parseDouble(parts[3]);
                HourlyEmployee employee = new HourlyEmployee(employeeID, employeeLevel, hours);
                employee.calculateSalary(hourlyRateByLevel);
                employees.add(employee);
            } else if (typeOfEmployee.equals("F")) {
                List<Double> ticketPoints = new ArrayList<>();
                Arrays.stream(parts)
                        .skip(3)
                        .forEach(i -> ticketPoints.add(Double.parseDouble(i)));
                FreelanceEmployee employee = new FreelanceEmployee(employeeID, employeeLevel, ticketPoints);
                employee.calculateSalary(ticketRateByLevel);
                employees.add(employee);
            }
        }
    }

    public Map<String, Collection<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        PrintWriter writer = new PrintWriter(os);
        Map<String, Collection<Employee>> result = new LinkedHashMap<>();

        for (String level : levels) {
            List<Employee> levelEmployees = new ArrayList<>();
            for (Employee employee : employees) {
                if (employee.getEmployeeLevel().equals(level)) {
                    levelEmployees.add(employee);
                }
            }

            levelEmployees.sort(Comparator.comparingDouble(Employee::getSalary)
                    .reversed());

            result.put(level, levelEmployees);
        }

        result.forEach((level, employeesForLevel) -> {
            writer.println("LEVEL: " + level);
            writer.println("Employees:");
            employeesForLevel.forEach(writer::println);
            writer.println("------------");
        });

        writer.flush();

        return result;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) throws IOException {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Collection<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
        });
    }
}