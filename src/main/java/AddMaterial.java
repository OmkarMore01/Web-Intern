

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddMaterial
 */
@WebServlet("/AddMaterial")
public class AddMaterial extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMaterial() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter(); 
		String materialId = request.getParameter("materialId");
	        String materialName = request.getParameter("materialName");
	        String sportType = request.getParameter("sportType");

	        int quantity = Integer.parseInt(request.getParameter("quantity"));
	        double price = Double.parseDouble(request.getParameter("price"));

	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	        	
	    		String url="jdbc:mysql://localhost:3306/webintern";
	    		String userj="root";
	    		String passj="8284";
	    		Connection con=DriverManager.getConnection(url,userj,passj);
	    		System.out.println("Connection established successfully...");
	    		
	            String sql = "INSERT INTO materials VALUES (?, ?, ?, ?, ?)";
	            PreparedStatement ps = con.prepareStatement(sql);

	            ps.setString(1, materialId);
	            ps.setString(2, materialName);
	            ps.setString(3, sportType);
	            ps.setInt(4, quantity);
	            ps.setDouble(5, price);

	            if (ps.executeUpdate() != 0) {
	                out.println("<script>"
	                        + "alert('Material added successfully');"
	                        + "window.location.href='Home.html';"
	                        + "</script>");
	            }


	            ps.close();
	            con.close();


	        } catch (Exception e) {
	            e.printStackTrace();
	            response.sendRedirect("Error.html");
	        }
	    }
	

}


