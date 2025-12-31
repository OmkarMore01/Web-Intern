

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
 * Servlet implementation class Regi
 */
@WebServlet("/Regi")
public class Regi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Regi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String user=request.getParameter("uname");
		String pass=request.getParameter("pass");
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("Driver Loaded successfully");
		
		String url="jdbc:mysql://localhost:3306/webintern";
		String userj="root";
		String passj="8284";
		Connection con=DriverManager.getConnection(url,userj,passj);
		System.out.println("Connection established successfully...");
		
		String sql=" insert into regi (username,password) values(?,?);";
		PreparedStatement ps=con.prepareStatement(sql);
		ps.setString(1, user);
		ps.setString(2, pass);
		
		if(ps.executeUpdate()!=0)
		{
			out.println("<script>");
			out.println("alert('Registered successfull');");
            out.println("window.location.href='Login.html';");
            out.println("</script>");
            System.out.println("Record inserted successfulyy..");
		}
		else {
			out.println("<script>");
			out.println("alert('Registeration Failed');");
            out.println("window.location.href='Register.html';");
            out.println("</script>");
		}
		
		
		}
		catch (Exception e) {
			System.out.println(e);
		}
		out.println(user+pass);
		
		

	
	
	
	}

}
