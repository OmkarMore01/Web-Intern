import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginCheck")
public class LoginCheck extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String uname = request.getParameter("username");
        String pass = request.getParameter("password");

        try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		System.out.println("Driver Loaded successfully");
    		
    		String url="jdbc:mysql://localhost:3306/webintern";
    		String userj="root";
    		String passj="8284";
    		Connection con=DriverManager.getConnection(url,userj,passj);
    		System.out.println("Connection established successfully...");
    		
    		String sql="select *from regi ";
    		Statement ps=con.createStatement();
    		
    		ResultSet rs=ps.executeQuery(sql);
    		while(rs.next()){
    			 if (rs.getString("username").equals(uname) && rs.getString("password").equals(pass)) {
    		            out.println("<script>");
    		            out.println("alert('Login Successful');");
    		            out.println("window.location.href='Home.html';");
    		            out.println("</script>");
    		        } else {
    		            out.println("<script>");
    		            out.println("alert('Login Failed User is not registered');");
    		            out.println("window.location.href='Login.html';");
    		            out.println("</script>");
    		        }
    		}
    		
    		
        }catch (Exception e) {
			System.out.println(e);
		}
        
        
        
       
    }
}
