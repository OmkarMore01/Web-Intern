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

@WebServlet("/MaterialData")
public class MaterialData extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
            <title>Material Data</title>

            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

            <style>
                body { background-color: #f8f9fa; }
                .table thead { background-color: #0d6efd; color: white; }
                .table-hover tbody tr:hover { background-color: #e3f2fd; }
                .action-btn { min-width: 70px; }
                h2 { font-family: 'Segoe UI', sans-serif; }
            </style>
        </head>
        <body>
        """);

        out.println("""
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
            <div class="container-fluid">
                <a class="navbar-brand fw-bold" href="index.html">EduManage</a>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item"><a class="nav-link" href="Home.html">Home</a></li>
                        <li class="nav-item"><a class="nav-link" href="AddStudent.html">Add Student</a></li>
                        
                        <li class="nav-item"><a class="nav-link" href="StudentData">Registered Students</a></li>
                        <li class="nav-item"><a class="nav-link" href="AddMaterial.html">Add Material</a></li>
                        <li class="nav-item"><a class="nav-link active" href="MaterialData">Material Data</a></li>
                         
             <li class="nav-item">
                    <a class="nav-link" href="IssueMaterial.html">Issue Material</a>
                </li>
            
                    </ul>

                    <form class="d-flex">
                        <input class="form-control me-2" type="search" placeholder="Search Material">
                        <button class="btn btn-outline-light">Search</button>
                    </form>
                </div>
            </div>
        </nav>
        """);

        out.println("""
        <div class="container mt-5 mb-5">
            <h2 class="text-center text-primary mb-4">Sports Material Data</h2>

            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover align-middle text-center">
                            <thead>
                                <tr>
                                    <th>Material ID</th>
                                    <th>Material Name</th>
                                    <th>Sport Type</th>
                                    <th>Quantity</th>
                                    <th>Available Quantity</th>
                                    <th>Price</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
        """);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/webintern",
                    "root",
                    "8284"
            );

            PreparedStatement ps = con.prepareStatement("SELECT * FROM materials");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String mid = rs.getString("material_id");

                out.println("<tr>");
                out.println("<td>" + mid + "</td>");
                out.println("<td>" + rs.getString("material_name") + "</td>");
                out.println("<td><span class='badge bg-info text-dark'>" + rs.getString("sport_type") + "</span></td>");
                out.println("<td>" + rs.getInt("quantity") + "</td>");
                out.println("<td>" + rs.getInt("available_quantity") + "</td>");
                out.println("<td>" + rs.getDouble("price") + "</td>");

                out.println("""
                    <td>
                        <a href="EditMaterial?material_id=""" + mid + """
                        " class="btn btn-warning btn-sm action-btn me-1">Edit</a>

                        <a href="DeleteMaterial?material_id=""" + mid + """
                        " class="btn btn-danger btn-sm action-btn"
                        onclick="return confirm('Are you sure you want to delete this material?');">
                        Delete</a>
                    </td>
                """);

                out.println("</tr>");
            }

            con.close();

        } catch (Exception e) {
            out.println("<tr><td colspan='7' class='text-danger'>Error: " + e.getMessage() + "</td></tr>");
        }

        out.println("""
                            </tbody>
                        </table>
                    </div>

                    <div class="text-center mt-3">
                        <a href="AddMaterial.html" class="btn btn-success btn-lg">
                            Add New Material
                        </a>
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
