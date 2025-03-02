import java.util.*;

public class Scheduler {

    public static Map<String, Map<String, List<String>>> collectSampleEmployeeData() {
        Map<String, Map<String, List<String>>> data = new HashMap<>();

        Map<String, List<String>> frankMap = new HashMap<>();
        frankMap.put("Monday", Arrays.asList("morning"));
        frankMap.put("Tuesday", Arrays.asList("morning"));
        frankMap.put("Wednesday", Arrays.asList("morning"));
        frankMap.put("Thursday", Arrays.asList("afternoon"));
        frankMap.put("Friday", Arrays.asList("morning"));
        frankMap.put("Saturday", Arrays.asList("evening"));
        frankMap.put("Sunday", Arrays.asList("morning"));
        data.put("Frank", frankMap);

        Map<String, List<String>> rajeshMap = new HashMap<>();
        rajeshMap.put("Monday", Arrays.asList("morning"));
        rajeshMap.put("Tuesday", Arrays.asList("afternoon"));
        rajeshMap.put("Wednesday", Arrays.asList("morning"));
        rajeshMap.put("Thursday", Arrays.asList("morning"));
        rajeshMap.put("Friday", Arrays.asList("evening"));
        rajeshMap.put("Saturday", Arrays.asList("morning"));
        rajeshMap.put("Sunday", Arrays.asList("afternoon", "morning"));
        data.put("Rajesh", rajeshMap);

        Map<String, List<String>> harryMap = new HashMap<>();
        harryMap.put("Monday", Arrays.asList("evening", "afternoon"));
        harryMap.put("Tuesday", Arrays.asList("evening", "afternoon"));
        harryMap.put("Wednesday", Arrays.asList("evening"));
        harryMap.put("Thursday", Arrays.asList("afternoon"));
        harryMap.put("Friday", Arrays.asList("afternoon"));
        harryMap.put("Saturday", Arrays.asList("afternoon"));
        harryMap.put("Sunday", Arrays.asList("evening"));
        data.put("Harry", harryMap);

        Map<String, List<String>> srikantMap = new HashMap<>();
        srikantMap.put("Monday", Arrays.asList("morning"));
        srikantMap.put("Tuesday", Arrays.asList("morning"));
        srikantMap.put("Wednesday", Arrays.asList("morning"));
        srikantMap.put("Thursday", Arrays.asList("morning"));
        srikantMap.put("Friday", Arrays.asList("morning"));
        srikantMap.put("Saturday", Arrays.asList("morning"));
        srikantMap.put("Sunday", Arrays.asList("morning"));
        data.put("Srikant", srikantMap);

        Map<String, List<String>> danielMap = new HashMap<>();
        danielMap.put("Monday", Arrays.asList("morning"));
        danielMap.put("Tuesday", Arrays.asList("afternoon"));
        danielMap.put("Wednesday", Arrays.asList("morning", "afternoon"));
        danielMap.put("Thursday", Arrays.asList("morning"));
        danielMap.put("Friday", Arrays.asList("morning"));
        danielMap.put("Saturday", Arrays.asList("afternoon"));
        danielMap.put("Sunday", Arrays.asList("evening"));
        data.put("Daniel", danielMap);

        Map<String, List<String>> jashmineMap = new HashMap<>();
        jashmineMap.put("Monday", Arrays.asList("afternoon"));
        jashmineMap.put("Tuesday", Arrays.asList("afternoon"));
        jashmineMap.put("Wednesday", Arrays.asList("morning"));
        jashmineMap.put("Thursday", Arrays.asList("morning"));
        jashmineMap.put("Friday", Arrays.asList("morning"));
        jashmineMap.put("Saturday", Arrays.asList("afternoon"));
        jashmineMap.put("Sunday", Arrays.asList("morning"));
        data.put("Jashmine", jashmineMap);

        Map<String, List<String>> annaMap = new HashMap<>();
        annaMap.put("Monday", Arrays.asList("afternoon"));
        annaMap.put("Tuesday", Arrays.asList("afternoon"));
        annaMap.put("Wednesday", Arrays.asList("morning"));
        annaMap.put("Thursday", Arrays.asList("afternoon"));
        annaMap.put("Friday", Arrays.asList("morning"));
        annaMap.put("Saturday", Arrays.asList("afternoon"));
        annaMap.put("Sunday", Arrays.asList("evening"));
        data.put("Anna", annaMap);

        Map<String, List<String>> catherineMap = new HashMap<>();
        catherineMap.put("Monday", Arrays.asList("afternoon"));
        catherineMap.put("Tuesday", Arrays.asList("afternoon", "morning"));
        catherineMap.put("Wednesday", Arrays.asList("morning"));
        catherineMap.put("Thursday", Arrays.asList("afternoon"));
        catherineMap.put("Friday", Arrays.asList("morning"));
        catherineMap.put("Saturday", Arrays.asList("morning"));
        catherineMap.put("Sunday", Arrays.asList("morning", "afternoon"));
        data.put("Catherine", catherineMap);

        Map<String, List<String>> johnMap = new HashMap<>();
        johnMap.put("Monday", Arrays.asList("afternoon"));
        johnMap.put("Tuesday", Arrays.asList("afternoon", "morning"));
        johnMap.put("Wednesday", Arrays.asList("morning"));
        johnMap.put("Thursday", Arrays.asList("morning", "afternoon"));
        johnMap.put("Friday", Arrays.asList("morning"));
        johnMap.put("Saturday", Arrays.asList("morning"));
        johnMap.put("Sunday", Arrays.asList("morning", "afternoon"));
        data.put("John", johnMap);

        return data;
    }

    public static Map<String, Map<String, List<String>>> collectEmployeeDataFromUser() {
        Scanner scanner = new Scanner(System.in);
        Map<String, Map<String, List<String>>> data = new HashMap<>();

        System.out.print("How many employees in your company? ");
        int numEmployees = Integer.parseInt(scanner.nextLine());
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (int i = 0; i < numEmployees; i++) {
            System.out.println("\nEnter name of employee #" + (i+1) + ": ");
            String employeeName = scanner.nextLine().trim();
            Map<String, List<String>> dayPreferences = new HashMap<>();
            for (String day : daysOfWeek) {
                System.out.println("Enter preferred shifts for " + employeeName + " on " + day + 
                                   " (morning/afternoon/evening). Please separate multiple with commas, or skip using Enter:");
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    String[] shiftsArray = input.split(",");
                    List<String> shiftsList = new ArrayList<>();
                    for (String s : shiftsArray) {
                        shiftsList.add(s.trim());
                    }
                    dayPreferences.put(day, shiftsList);
                }
            }
            data.put(employeeName, dayPreferences);
        }
        
        return data;
    }

    private static Map<String, Map<String, List<String>>> initializeSchedule() {
        Map<String, Map<String, List<String>>> schedule = new LinkedHashMap<>();
        List<String> days = Arrays.asList(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        );
        List<String> shifts = Arrays.asList("morning", "afternoon", "evening");

        for (String day : days) {
            Map<String, List<String>> shiftMap = new HashMap<>();
            for (String shift : shifts) {
                shiftMap.put(shift, new ArrayList<>());
            }
            schedule.put(day, shiftMap);
        }
        return schedule;
    }

    private static Map<String, Map<String, List<String>>> scheduleEmployees(
            Map<String, Map<String, List<String>>> employeeData) {

        Map<String, Map<String, List<String>>> schedule = initializeSchedule();

        Map<String, Integer> employeeDaysWorked = new HashMap<>();
        for (String emp : employeeData.keySet()) {
            employeeDaysWorked.put(emp, 0);
        }

        List<String> days = new ArrayList<>(schedule.keySet());
        List<String> shifts = Arrays.asList("morning", "afternoon", "evening");

        for (String day : days) {
            List<String> employeeList = new ArrayList<>(employeeData.keySet());
            Collections.shuffle(employeeList);

            for (String employee : employeeList) {
                if (!employeeData.get(employee).containsKey(day)) {
                    continue;
                }

                if (employeeDaysWorked.get(employee) >= 5) {
                    continue;
                }

                boolean assigned = false;
                List<String> preferredShifts = employeeData.get(employee).get(day);

                for (String prefShift : preferredShifts) {
                    if (schedule.get(day).get(prefShift).size() < 2) {
                        schedule.get(day).get(prefShift).add(employee);
                        employeeDaysWorked.put(employee, employeeDaysWorked.get(employee) + 1);
                        assigned = true;
                        break;
                    }
                }

                if (!assigned) {
                    for (String s : shifts) {
                        if (schedule.get(day).get(s).size() < 2 && employeeDaysWorked.get(employee) < 5) {
                            schedule.get(day).get(s).add(employee);
                            employeeDaysWorked.put(employee, employeeDaysWorked.get(employee) + 1);
                            assigned = true;
                            break;
                        }
                    }
                }

                if (!assigned) {
                    int currentDayIndex = days.indexOf(day);
                    int nextDayIndex = currentDayIndex + 1;
                    if (nextDayIndex < days.size()) {
                        String nextDay = days.get(nextDayIndex);
                        for (String s : shifts) {
                            if (schedule.get(nextDay).get(s).size() < 2 && employeeDaysWorked.get(employee) < 5) {
                                schedule.get(nextDay).get(s).add(employee);
                                employeeDaysWorked.put(employee, employeeDaysWorked.get(employee) + 1);
                                break;
                            }
                        }
                    }
                }
            }
        }

        // ensure each shift has at least 2 people
        Random rand = new Random();
        for (String day : days) {
            for (String shift : shifts) {
                while (schedule.get(day).get(shift).size() < 2) {
                    List<String> potential = new ArrayList<>();
                    for (String emp : employeeData.keySet()) {
                        if (employeeDaysWorked.get(emp) < 5 &&
                            !schedule.get(day).get(shift).contains(emp)) {
                            potential.add(emp);
                        }
                    }

                    if (potential.isEmpty()) {
                        break;
                    }

                    String chosen = potential.get(rand.nextInt(potential.size()));
                    schedule.get(day).get(shift).add(chosen);
                    employeeDaysWorked.put(chosen, employeeDaysWorked.get(chosen) + 1);
                }
            }
        }

        return schedule;
    }

    private static void printSchedule(Map<String, Map<String, List<String>>> schedule) {
        for (String day : schedule.keySet()) {
            System.out.println("===================================================");
            System.out.println("                    " + day);
            System.out.println("===================================================");
            Map<String, List<String>> shifts = schedule.get(day);
            for (String shift : shifts.keySet()) {
                List<String> employees = shifts.get(shift);
                String employeesStr = employees.isEmpty() 
                    ? "N/A" 
                    : String.join(", ", employees);
                System.out.println("  " + capitalize(shift) + ": " + employeesStr);
            }
            System.out.println();
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // main driver method: user chooses sample or manual input
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===================================================");
        System.out.println("       EMPLOYEE SHIFT SCHEDULER");
        System.out.println("===================================================");
        System.out.println("Choose one of the below:");
        System.out.println("  1) Show sample employee schedule (hardcoded-data)");
        System.out.println("  2) Input custom employee data");

        System.out.print("Enter choice (1 or 2): ");
        String choice = scanner.nextLine();

        Map<String, Map<String, List<String>>> employeeData;

        if (choice.equals("1")) {
            employeeData = collectSampleEmployeeData();
        } else {
            employeeData = collectEmployeeDataFromUser();
        }
        System.out.println("\nGenerating schedule...");
        Map<String, Map<String, List<String>>> finalSchedule = scheduleEmployees(employeeData);

        printSchedule(finalSchedule);
        scanner.close();
    }
}