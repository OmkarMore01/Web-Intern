import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/IssueMaterial")
public class IssueMaterial extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int studentId = Integer.parseInt(request.getParameter("student_id"));
        String studentName = request.getParameter("student_name");
        int materialId = Integer.parseInt(request.getParameter("material_id"));
        String materialType = request.getParameter("material_type");
        String materialName = request.getParameter("material_name");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        LocalDate returnDate = LocalDate.now().plusDays(7);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/webintern", "root", "8284");

            // Check if student exists
            String studentCheck = "SELECT * FROM studentdata WHERE sid = ?";
            PreparedStatement studentStmt = con.prepareStatement(studentCheck);
            studentStmt.setInt(1, studentId);
            ResultSet studentRs = studentStmt.executeQuery();
            if (!studentRs.next()) {
                out.println("<script>alert('Student does not exist!'); window.location='IssueMaterial.html';</script>");
                return;
            }

            // Check if material exists
            String materialCheck = "SELECT * FROM materials WHERE material_id = ?";
            PreparedStatement materialStmt = con.prepareStatement(materialCheck);
            materialStmt.setInt(1, materialId);
            ResultSet materialRs = materialStmt.executeQuery();
            if (!materialRs.next()) {
                out.println("<script>alert('Material does not exist!'); window.location='IssueMaterial.html';</script>");
                return;
            }

            int availableQty = materialRs.getInt("available_quantity");
            if (availableQty < quantity) {
                out.println("<script>alert('Stock Not Available! Only " + availableQty + " items left.'); window.location='IssueMaterial.html';</script>");
                return;
            }

            // Insert issued material record
            String insertQuery = "INSERT INTO issued_materials "
                    + "(student_id, student_name, material_id, material_type, material_name, quantity, return_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(insertQuery);
            insertStmt.setInt(1, studentId);
            insertStmt.setString(2, studentName);
            insertStmt.setInt(3, materialId);
            insertStmt.setString(4, materialType);
            insertStmt.setString(5, materialName);
            insertStmt.setInt(6, quantity);
            insertStmt.setDate(7, Date.valueOf(returnDate));
            insertStmt.executeUpdate();

            // Update available quantity
            String updateQuery = "UPDATE materials SET available_quantity = available_quantity - ? WHERE material_id = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, materialId);
            updateStmt.executeUpdate();

            // Show confirm dialog using JavaScript
            out.println("<script>");
            out.println("if(confirm('Material issued successfully!\\nDo you want to issue another material to students?')) {");
            out.println("    window.location='IssueMaterial.html';");
            out.println("} else {");
            out.println("    window.location='Home.html';");
            out.println("}");
            out.println("</script>");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('Error issuing material!'); window.location='IssueMaterial.html';</script>");
        }
    }
}
