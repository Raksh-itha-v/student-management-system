import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private final String url;
    private final String user;
    private final String password;

    public DBHelper(String url, String user, String password) throws ClassNotFoundException {
        this.url = url;
        this.user = user;
        this.password = password;
        // Load MySQL JDBC driver (optional for newer JDBC, but safe)
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Authentication (very simple - compares the stored password string)
    public boolean authenticate(String username, String passwordInput) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String stored = rs.getString("password_hash");
                    return stored.equals(passwordInput); // demo only
                }
            }
        } catch (SQLException e) {
            System.err.println("Auth error: " + e.getMessage());
        }
        return false;
    }

    // CREATE
    public boolean addStudent(String rollNo, String name, String email, String branch, int year) {
        String sql = "INSERT INTO students (roll_no, name, email, branch, year) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, rollNo);
            pst.setString(2, name);
            pst.setString(3, email);
            pst.setString(4, branch);
            pst.setInt(5, year);
            int affected = pst.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            System.err.println("Add student error: " + e.getMessage());
            return false;
        }
    }

    // READ - all
    public List<String> getAllStudents() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT id, roll_no, name, email, branch, year, created_at FROM students ORDER BY id";
        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String row = String.format("ID:%d | Roll:%s | Name:%s | Email:%s | Branch:%s | Year:%d | Added:%s",
                        rs.getInt("id"),
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("branch"),
                        rs.getInt("year"),
                        rs.getTimestamp("created_at").toString());
                list.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Get students error: " + e.getMessage());
        }
        return list;
    }

    // READ - by roll_no
    public String getStudentByRoll(String rollNo) {
        String sql = "SELECT id, roll_no, name, email, branch, year, created_at FROM students WHERE roll_no = ?";
        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, rollNo);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return String.format("ID:%d | Roll:%s | Name:%s | Email:%s | Branch:%s | Year:%d | Added:%s",
                            rs.getInt("id"),
                            rs.getString("roll_no"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("branch"),
                            rs.getInt("year"),
                            rs.getTimestamp("created_at").toString());
                }
            }
        } catch (SQLException e) {
            System.err.println("Get student error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateStudentEmail(String rollNo, String newEmail) {
        String sql = "UPDATE students SET email = ? WHERE roll_no = ?";
        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, newEmail);
            pst.setString(2, rollNo);
            int affected = pst.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            System.err.println("Update student error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteStudent(String rollNo) {
        String sql = "DELETE FROM students WHERE roll_no = ?";
        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, rollNo);
            int affected = pst.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            System.err.println("Delete student error: " + e.getMessage());
            return false;
        }
    }
}
