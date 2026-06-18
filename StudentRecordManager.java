import java.io.*;
import java.util.*;

public class StudentRecordManager {
    private List<Student> students = new ArrayList<>();
    private final String DIR = "data", TXT = "data/students.txt", BIN = "data/students.dat", SER = "data/students.ser", BAK = "data/students_backup.bak";

    public StudentRecordManager() { new File(DIR).mkdirs(); }

    // CRUD Operations
    public void addStudent(Student s) { students.add(s); System.out.println("Added locally."); }
    public Student searchStudent(String id) { return students.stream().filter(s -> s.getStudentId().equalsIgnoreCase(id)).findFirst().orElse(null); }
    public boolean deleteStudent(String id) { Student s = searchStudent(id); return s != null && students.remove(s); }
    public void displayAllStudents() { if (students.isEmpty()) System.out.println("Empty."); else students.forEach(System.out::println); }
    
    public boolean updateStudent(String id, String name, String dept, double gpa) {
        Student s = searchStudent(id);
        if (s == null) return false;
        s.setName(name); s.setDepartment(dept); s.setGpa(gpa);
        return true;
    }

    // 1. Text File Storage
    public void saveToTextFile() throws IOException {
        try (PrintWriter w = new PrintWriter(new FileWriter(TXT))) {
            for (Student s : students) w.println(s.getStudentId() + "," + s.getName() + "," + s.getDepartment() + "," + s.getGpa());
        }
    }
    public void loadFromTextFile() throws IOException {
        File f = new File(TXT); if (!f.exists()) return; students.clear();
        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String[] t = s.nextLine().split(",");
                if (t.length == 4) students.add(new Student(t[0], t[1], t[2], Double.parseDouble(t[3])));
            }
        }
    }

    // 2. Binary File Storage
    public void saveToBinaryFile() throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(BIN))) {
            dos.writeInt(students.size());
            for (Student s : students) {
                dos.writeUTF(s.getStudentId()); dos.writeUTF(s.getName()); dos.writeUTF(s.getDepartment()); dos.writeDouble(s.getGpa());
            }
        }
    }
    public void loadFromBinaryFile() throws IOException {
        File f = new File(BIN); if (!f.exists()) return; students.clear();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int count = dis.readInt();
            for (int i = 0; i < count; i++) students.add(new Student(dis.readUTF(), dis.readUTF(), dis.readUTF(), dis.readDouble()));
        }
    }

    // 3. Object Serialization
    public void saveBySerialization() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SER))) { oos.writeObject(students); }
    }
    @SuppressWarnings("unchecked")
    public void loadBySerialization() throws IOException, ClassNotFoundException {
        File f = new File(SER); if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) { students = (List<Student>) ois.readObject(); }
    }

    // Reports & File Utilities
    public void generateReport() {
        if (students.isEmpty()) return;
        double max = students.stream().mapToDouble(Student::getGpa).max().orElse(0);
        double min = students.stream().mapToDouble(Student::getGpa).min().orElse(0);
        double avg = students.stream().mapToDouble(Student::getGpa).average().orElse(0);
        System.out.printf("\nTotal: %d | Max: %.2f | Min: %.2f | Avg: %.2f\n", students.size(), max, min, avg);
    }

    public void backupRecords() throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(TXT));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(BAK))) {
            byte[] b = new byte[1024]; int r;
            while ((r = bis.read(b)) != -1) bos.write(b, 0, r);
            System.out.println("Backup complete.");
        }
    }

    public void displayFileProperties() {
        for (String p : new String[]{TXT, BIN, SER, BAK}) {
            File f = new File(p);
            if (f.exists()) System.out.printf("Name: %s | Size: %d bytes | Path: %s\n", f.getName(), f.length(), f.getAbsolutePath());
        }
    }
}
