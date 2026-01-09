

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HistoryOfIssuedMaterial
 */
@WebServlet("/HistoryOfIssuedMaterial")
public class HistoryOfIssuedMaterial extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HistoryOfIssuedMaterial() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("""
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Return History</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
            <style>
                body { background-color: #f8f9fa; }
                .table thead { background-color: #0d6efd; color: white; }
                .table-hover tbody tr:hover { background-color: #e3f2fd; }
                h2 { font-family: 'Segoe UI', sans-serif; }
            </style>
        </head>
        <body>
        """);

        // Navbar
        out.println("""
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
            <div class="container-fluid">
                <a class="navbar-brand fw-bold" href="index.html">EduManage</a>
            </div>
        </nav>
        """);

        // Page Heading
        out.println("""
        <div class="container mt-4 mb-5">
     <h2 class="text-primary text-center mb-4">Returned Material History
   <a href="ExportHistoryToExcel" class="btn btn-success  btn-lg shadow-sm"
   style="font-weight:bold; margin-left:30px;">
   Export to Excel
</a>
</h2>
 <div class="card shadow-sm">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover align-middle text-center">
                            <thead>
                                <tr>
                                    <th>History ID</th>
                                    <th>Issue ID</th>
                                    <th>Student ID</th>
                                    <th>Student Name</th>
                                    <th>Material ID</th>
                                    <th>Material Type</th>
                                    <th>Material Name</th>
                                    <th>Quantity</th>
                                    <th>Issue Date</th>
                                    <th>Return Date</th>
                                </tr>
                            </thead>
                            <tbody>
        """);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/webintern", "root", "8284");

            PreparedStatement ps = con.prepareStatement("SELECT * FROM history_of_issue ORDER BY return_date DESC");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("history_id") + "</td>");
                out.println("<td>" + rs.getInt("issue_id") + "</td>");
                out.println("<td>" + rs.getString("student_id") + "</td>");
                out.println("<td class='fw-bold'>" + rs.getString("student_name") + "</td>");
                out.println("<td>" + rs.getString("material_id") + "</td>");
                out.println("<td><span class='badge bg-secondary'>" + rs.getString("material_type") + "</span></td>");
                out.println("<td>" + rs.getString("material_name") + "</td>");
                out.println("<td>" + rs.getInt("quantity") + "</td>");
                out.println("<td>" + rs.getDate("issue_date") + "</td>");
                out.println("<td>" + rs.getDate("return_date") + "</td>");
                out.println("</tr>");
            }

            con.close();
        } catch (Exception e) {
            out.println("<tr><td colspan='10' class='text-danger'>" + e.getMessage() + "</td></tr>");
        }

        out.println("""
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <footer class="bg-primary text-white text-center py-3 mt-5 shadow-sm">
            © 2025 Sports Club Management System | College Project
        </footer>

        </body>
        </html>
        """);
    }


}
