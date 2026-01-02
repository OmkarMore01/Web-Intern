

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
 * Servlet implementation class StudentRegister
 */
@WebServlet("/StudentRegister")
public class StudentRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentRegister() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String sname = request.getParameter("sname");

		int sid = Integer.parseInt(request.getParameter("sid"));

		String course = request.getParameter("course");

		int year = Integer.parseInt(request.getParameter("year"));

		String college = request.getParameter("college");

		String sport = request.getParameter("sport");

		out.println(sid+" "+sport+" "+year);
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("Driver Loaded successfully");
		
		String url="jdbc:mysql://localhost:3306/webintern";
		String userj="root";
		String passj="8284";
		Connection con=DriverManager.getConnection(url,userj,passj);
		System.out.println("Connection established successfully...");
		String sql = "INSERT INTO StudentData (sid, sname, course, year, college, sport) VALUES (?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = con.prepareStatement(sql);

		ps.setInt(1, sid);
		ps.setString(2, sname);
		ps.setString(3, course);
		ps.setInt(4, year);
		ps.setString(5, college);
		ps.setString(6, sport);

		ps.executeUpdate();
		out.println("Student Saved Successfully...");
		
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	
		
	}

}
