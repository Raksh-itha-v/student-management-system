import java.util.List;
import java.util.Scanner;

public class StudentManagement {
    private static DBHelper db;

    public static void main(String[] args) {
        // DB connection parameters - EDIT these
        String url = "jdbc:mysql://localhost:3306/studentdb?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String pass = "your_mysql_password_here";

        try {
            db = new DBHelper(url, user, pass);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Make sure the connector is in classpath.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("=== Student Management System ===");
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        if (!db.authenticate(username, password)) {
            System.out.println("Authentication failed. Exiting.");
            return;
        }

        System.out.println("Login successful. Welcome, " + username + "!");
        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("Choose option: ");
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1":
                    addStudent(sc);
                    break;
                case "2":
                    listStudents();
                    break;
                case "3":
                    viewStudent(sc);
                    break;
                case "4":
                    updateEmail(sc);
                    break;
                case "5":
                    deleteStudent(sc);
                    break;
                case "0":
                    exit = true;
                    System.out.println("Exiting. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        sc.close();
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add student");
        System.out.println("2. List all students");
        System.out.println("3. View student by roll no");
        System.out.println("4. Update student email");
        System.out.println("5. Delete student");
        System.out.println("0. Exit");
    }

    private static void addStudent(Scanner sc) {
        System.out.print("Roll No: ");
        String roll = sc.nextLine().trim();
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Branch: ");
        String branch = sc.nextLine().trim();
        System.out.print("Year (int): ");
        int year = Integer.parseInt(sc.nextLine().trim());

        boolean ok = db.addStudent(roll, name, email, branch, year);
        System.out.println(ok ? "Student added." : "Failed to add student (maybe duplicate roll no).");
    }

    private static void listStudents() {
        List<String> students = db.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students:");
            students.forEach(System.out::println);
        }
    }

    private static void viewStudent(Scanner sc) {
        System.out.print("Enter roll no: ");
        String roll = sc.nextLine().trim();
        String s = db.getStudentByRoll(roll);
        if (s == null) System.out.println("Student not found.");
        else System.out.println(s);
    }

    private static void updateEmail(Scanner sc) {
        System.out.print("Enter roll no: ");
        String roll = sc.nextLine().trim();
        System.out.print("Enter new email: ");
        String email = sc.nextLine().trim();
        boolean ok = db.updateStudentEmail(roll, email);
        System.out.println(ok ? "Email updated." : "Update failed (student maybe not found).");
    }

    private static void deleteStudent(Scanner sc) {
        System.out.print("Enter roll no to delete: ");
        String roll = sc.nextLine().trim();
        boolean ok = db.deleteStudent(roll);
        System.out.println(ok ? "Student deleted." : "Delete failed (student maybe not found).");
    }
}
