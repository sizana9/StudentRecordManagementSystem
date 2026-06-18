import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StudentRecordManager manager = new StudentRecordManager();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            try {
                System.out.println("\n=== STUDENT RECORD MANAGEMENT SYSTEM ===");
                System.out.println("1. Add Student\n2. Search Student by ID\n3. Update Student Information");
                System.out.println("4. Delete Student\n5. Display All Students\n6. Save Data (All Formats)");
                System.out.println("7. Load Data\n8. Generate Statistical Report\n9. Backup Records (Buffered)");
                System.out.println("10. View File Metadata Properties\n11. Exit");
                System.out.print("Choose an option: ");
                
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.print("ID: "); String id = scanner.nextLine();
                        System.out.print("Name: "); String name = scanner.nextLine();
                        System.out.print("Dept: "); String dept = scanner.nextLine();
                        System.out.print("GPA: "); double gpa = Double.parseDouble(scanner.nextLine());
                        manager.addStudent(new Student(id, name, dept, gpa));
                        break;
                    case 2:
                        System.out.print("Enter ID: ");
                        Student found = manager.searchStudent(scanner.nextLine());
                        System.out.println(found != null ? found : "Not found.");
                        break;
                    case 3:
                        System.out.print("ID to update: "); String uId = scanner.nextLine();
                        System.out.print("New Name: "); String uName = scanner.nextLine();
                        System.out.print("New Dept: "); String uDept = scanner.nextLine();
                        System.out.print("New GPA: "); double uGpa = Double.parseDouble(scanner.nextLine());
                        System.out.println(manager.updateStudent(uId, uName, uDept, uGpa) ? "Updated!" : "Not found.");
                        break;
                    case 4:
                        System.out.print("ID to delete: ");
                        System.out.println(manager.deleteStudent(scanner.nextLine()) ? "Deleted!" : "Not found.");
                        break;
                    case 5: manager.displayAllStudents(); break;
                    case 6: manager.saveToTextFile(); manager.saveToBinaryFile(); manager.saveBySerialization(); System.out.println("Saved everywhere!"); break;
                    case 7:
                        System.out.print("Load from: 1. Text | 2. Binary | 3. Serialized: ");
                        int type = Integer.parseInt(scanner.nextLine());
                        if (type == 1) manager.loadFromTextFile();
                        else if (type == 2) manager.loadFromBinaryFile();
                        else if (type == 3) manager.loadBySerialization();
                        System.out.println("Loaded!");
                        break;
                    case 8: manager.generateReport(); break;
                    case 9: manager.backupRecords(); break;
                    case 10: manager.displayFileProperties(); break;
                    case 11: System.out.println("Exiting..."); scanner.close(); System.exit(0);
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
