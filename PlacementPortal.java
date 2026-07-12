import java.sql.*;
import java.util.Scanner;

public class PlacementPortal {

    static final String URL =
"jdbc:mysql://localhost:3306/placement_portal";

static final String USER = "root";

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
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                URL, USER, PASSWORD);
    }

    static void addStudent() {
        try {
            Connection con = getConnection();

            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Branch: ");
            String branch = sc.nextLine();

            System.out.print("Enter CGPA: ");
            double cgpa = sc.nextDouble();

            sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            String q =
                    "INSERT INTO students(name,branch,cgpa,email) VALUES(?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(q);

            ps.setString(1, name);
            ps.setString(2, branch);
            ps.setDouble(3, cgpa);
            ps.setString(4, email);

            ps.executeUpdate();

            System.out.println("Student Added Successfully");

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void addCompany() {
        try {
            Connection con = getConnection();

            sc.nextLine();

            System.out.print("Enter Company Name: ");
            String company = sc.nextLine();

            System.out.print("Enter Minimum CGPA: ");
            double cgpa = sc.nextDouble();

            String q =
                    "INSERT INTO companies(company_name,min_cgpa) VALUES(?,?)";

            PreparedStatement ps =
                    con.prepareStatement(q);

            ps.setString(1, company);
            ps.setDouble(2, cgpa);

            ps.executeUpdate();

            System.out.println("Company Added Successfully");

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void viewStudents() {
        try {
            Connection con = getConnection();

            Statement st = con.createStatement();

            ResultSet rs =
                    st.executeQuery("SELECT * FROM students");

            System.out.println("\nID\tName\tBranch\tCGPA");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("branch") + "\t" +
                        rs.getDouble("cgpa"));
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void checkEligibility() {
        try {
            Connection con = getConnection();

            sc.nextLine();

            System.out.print("Enter Company Name: ");
            String company = sc.nextLine();

            String q =
                    "SELECT min_cgpa FROM companies WHERE company_name=?";

            PreparedStatement ps =
                    con.prepareStatement(q);

            ps.setString(1, company);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                double minCgpa =
                        rs.getDouble("min_cgpa");

                PreparedStatement ps2 =
                        con.prepareStatement(
                                "SELECT * FROM students WHERE cgpa>=?");

                ps2.setDouble(1, minCgpa);

                ResultSet rs2 =
                        ps2.executeQuery();

                System.out.println(
                        "\nEligible Students:");

                while (rs2.next()) {
                    System.out.println(
                            rs2.getString("name") +
                            " - " +
                            rs2.getDouble("cgpa"));
                }
            }
            else {
                System.out.println("Company Not Found");
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

