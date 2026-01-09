import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/IssuedMaterialData")
public class IssuedMaterialData extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public IssuedMaterialData() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String returnId = request.getParameter("return_id");
        String success = request.getParameter("success");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/webintern", "root", "8284");

            // 🔁 Inline Return Material Logic
            if (returnId != null) {
                int issue_id = Integer.parseInt(returnId);

                // Fetch issued material details
                PreparedStatement getPS = con.prepareStatement("SELECT * FROM issued_materials WHERE issue_id=?");
                getPS.setInt(1, issue_id);
                ResultSet getRS = getPS.executeQuery();

                if (getRS.next()) {
                    String material_id = getRS.getString("material_id");
                    int qty = getRS.getInt("quantity");
                    String student_id = getRS.getString("student_id");
                    String student_name = getRS.getString("student_name");
                    String material_type = getRS.getString("material_type");
                    String material_name = getRS.getString("material_name");
                    Date issue_date = getRS.getDate("issue_date");
                    Date return_date = new Date(System.currentTimeMillis()); // Current date

                    // 1️⃣ Update stock in materials table
                    PreparedStatement updateMaterialPS = con.prepareStatement(
                        "UPDATE materials SET available_quantity = available_quantity + ? WHERE material_id=?"
                    );
                    updateMaterialPS.setInt(1, qty);
                    updateMaterialPS.setString(2, material_id);
                    updateMaterialPS.executeUpdate();

                    // 2️⃣ Insert into history_of_issue
                    PreparedStatement historyPS = con.prepareStatement(
                        "INSERT INTO history_of_issue(issue_id, student_id, student_name, material_id, material_type, material_name, quantity, issue_date, return_date) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    );
                    historyPS.setInt(1, issue_id);
                    historyPS.setString(2, student_id);
                    historyPS.setString(3, student_name);
                    historyPS.setString(4, material_id);
                    historyPS.setString(5, material_type);
                    historyPS.setString(6, material_name);
                    historyPS.setInt(7, qty);
                    historyPS.setDate(8, issue_date);
                    historyPS.setDate(9, return_date);
                    historyPS.executeUpdate();

                    // 3️⃣ Delete from issued_materials
                    PreparedStatement deletePS = con.prepareStatement("DELETE FROM issued_materials WHERE issue_id=?");
                    deletePS.setInt(1, issue_id);
                    deletePS.executeUpdate();
                }

                con.close();

                // Success alert
                out.println("<script>alert('Material Returned Successfully! Stock Updated.');</script>");
            }

        } catch (Exception e) {
            out.println("<h3 class='text-danger text-center mt-3'>Error: " + e.getMessage() + "</h3>");
        }

        // ----- UI Rendering -----
        out.println("""
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Issued Materials</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
            <style>
                body { background-color: #f8f9fa; }
                .table thead { background-color: #0d6efd; color: white; }
                .table-hover tbody tr:hover { background-color: #e3f2fd; }
                .action-btn { min-width: 90px; }
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

        // Bootstrap success alert
        if (success != null) {
            out.println("""
            <div class='container mt-3'>
                <div class='alert alert-success alert-dismissible fade show text-center fw-bold' role='alert'>
                    Material returned successfully! Stock updated.
                    <button type='button' class='btn-close' data-bs-dismiss='alert'></button>
                </div>
            </div>
            """);
        }

        // Table UI
        out.println("""
        <div class="container mt-4 mb-5">
            <h2 class="text-center text-primary mb-4">Issued Sports Materials   <a href="HistoryOfIssuedMaterial" class="btn btn-outline-light btn-lg shadow-sm" 
   style="background-color:#0d6efd; color:white; border:none; font-weight:bold; margin-left:50px;">
   View Return History
</a>

           </h2>
            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover align-middle text-center table-hover">
                            <thead>
                                <tr>
                                    <th>Issue ID</th>
                                    <th>Student ID</th>
                                    <th>Student Name</th>
                                    <th>Material ID</th>
                                    <th>Material Type</th>
                                    <th>Material Name</th>
                                    <th>Quantity</th>
                                    <th>Issue Date</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
        """);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/webintern", "root", "8284");

            PreparedStatement ps = con.prepareStatement("SELECT * FROM issued_materials");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("issue_id");
                String mid = rs.getString("material_id");
                int qty = rs.getInt("quantity");
                Date issue = rs.getDate("issue_date");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + rs.getString("student_id") + "</td>");
                out.println("<td class='fw-bold'>" + rs.getString("student_name") + "</td>");
                out.println("<td>" + mid + "</td>");
                out.println("<td><span class='badge bg-secondary'>" + rs.getString("material_type") + "</span></td>");
                out.println("<td>" + rs.getString("material_name") + "</td>");
                out.println("<td>" + qty + "</td>");
                out.println("<td>" + issue + "</td>");

                if (qty > 0) {
                    out.println("""
                    <td>
                        <a href="IssuedMaterialData?return_id=""" + id + """
                        " class="btn btn-success btn-sm action-btn"
                        onclick="return confirm('Return this material?');">Return</a>
                    </td>
                    """);
                } else {
                    out.println("<td><span class='text-muted'>Returned</span></td>");
                }

                out.println("</tr>");
            }

            con.close();
        } catch (Exception e) {
            out.println("<tr><td colspan='9' class='text-danger'>" + e.getMessage() + "</td></tr>");
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

        <script>
            if (new URLSearchParams(window.location.search).has('return_id')) {
                window.history.replaceState({}, document.title, "IssuedMaterialData");
                location.reload();
            }
        </script>

        </body>
        </html>
        """);
    }
}
