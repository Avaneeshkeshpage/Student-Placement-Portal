import java.sql.*;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PlacementPortal {

    static final String URL =
            "jdbc:mysql://localhost:3306/placement_portal";

    static final String USER = "root";

    // Replace this with your MySQL password
    static final String PASSWORD = "Avaneesh@123";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n--- Student Placement Portal ---");
            System.out.println("1. Add Student");
            System.out.println("2. Add Company");
            System.out.println("3. View Students");
            System.out.println("4. Check Eligibility");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    addCompany();
                    break;

                case 3:
                    viewStudents();
                    break;

                case 4:
                    checkEligibility();
                    break;

                case 5:
                    System.out.println("Thank you!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    // Database Connection
    static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    // Add Student
    static void addStudent() {

        try {

            Connection con = getConnection();

            sc.nextLine();

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Branch: ");
            String branch = sc.nextLine();

            System.out.print("Enter CGPA: ");
            double cgpa = sc.nextDouble();

            sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            // Resume
            System.out.print("Enter Resume File Path: ");
            String resumePath = sc.nextLine();

            // Remove quotes if user pasted path with quotes
            resumePath = resumePath.replace("\"", "");

            File resume = new File(resumePath);

            // Check resume exists
            if (!resume.exists()) {

                System.out.println("Resume file not found!");

                con.close();
                return;
            }

            // Check resume format
            String fileName =
                    resume.getName().toLowerCase();

            if (!fileName.endsWith(".pdf")
                    && !fileName.endsWith(".doc")
                    && !fileName.endsWith(".docx")) {

                System.out.println(
                        "Only PDF, DOC and DOCX files are allowed!"
                );

                con.close();
                return;
            }

            // Create resumes folder
            File folder = new File("resumes");

            if (!folder.exists()) {
                folder.mkdir();
            }

            // Create unique resume name
            String uniqueName =
                    System.currentTimeMillis()
                    + "_"
                    + resume.getName();

            File destination =
                    new File(folder, uniqueName);

            // Copy resume
            Files.copy(
                    resume.toPath(),
                    destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            // Insert student details
            String query =
                    "INSERT INTO students " +
                    "(name, branch, cgpa, email, resume_path) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, branch);
            ps.setDouble(3, cgpa);
            ps.setString(4, email);
            ps.setString(5, destination.getPath());

            ps.executeUpdate();

            System.out.println(
                    "\nStudent Added Successfully!"
            );

            System.out.println(
                    "Resume Uploaded Successfully!"
            );

            System.out.println(
                    "Resume Location: "
                    + destination.getPath()
            );

            ps.close();
            con.close();

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    // Add Company
    static void addCompany() {

        try {

            Connection con = getConnection();

            sc.nextLine();

            System.out.print("Enter Company Name: ");
            String company = sc.nextLine();

            System.out.print("Enter Minimum CGPA: ");
            double cgpa = sc.nextDouble();

            String query =
                    "INSERT INTO companies " +
                    "(company_name, min_cgpa) " +
                    "VALUES (?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1, company);
            ps.setDouble(2, cgpa);

            ps.executeUpdate();

            System.out.println(
                    "Company Added Successfully"
            );

            ps.close();
            con.close();

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    // View Students
    static void viewStudents() {

        try {

            Connection con = getConnection();

            Statement st =
                    con.createStatement();

            ResultSet rs =
                    st.executeQuery(
                            "SELECT * FROM students"
                    );

            System.out.println(
                    "\nID\tName\tBranch\tCGPA\tEmail\tResume"
            );

            System.out.println(
                    "------------------------------------------------------------"
            );

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id")
                        + "\t"
                        + rs.getString("name")
                        + "\t"
                        + rs.getString("branch")
                        + "\t"
                        + rs.getDouble("cgpa")
                        + "\t"
                        + rs.getString("email")
                        + "\t"
                        + rs.getString("resume_path")
                );
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    // Check Eligibility
    static void checkEligibility() {

        try {

            Connection con = getConnection();

            sc.nextLine();

            System.out.print("Enter Company Name: ");
            String company = sc.nextLine();

            // Get minimum CGPA
            String query =
                    "SELECT min_cgpa " +
                    "FROM companies " +
                    "WHERE company_name = ?";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1, company);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                double minCgpa =
                        rs.getDouble("min_cgpa");

                System.out.println(
                        "\nMinimum CGPA Required: "
                        + minCgpa
                );

                // Find eligible students
                String studentQuery =
                        "SELECT * FROM students " +
                        "WHERE cgpa >= ?";

                PreparedStatement ps2 =
                        con.prepareStatement(
                                studentQuery
                        );

                ps2.setDouble(1, minCgpa);

                ResultSet rs2 =
                        ps2.executeQuery();

                System.out.println(
                        "\nEligible Students:"
                );

                boolean found = false;

                while (rs2.next()) {

                    found = true;

                    System.out.println(
                            rs2.getString("name")
                            + " - CGPA: "
                            + rs2.getDouble("cgpa")
                    );
                }

                if (!found) {

                    System.out.println(
                            "No students are eligible."
                    );
                }

                rs2.close();
                ps2.close();

            } else {

                System.out.println(
                        "Company Not Found"
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }
}