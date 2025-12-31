import java.io.IOException;
import java.io.PrintWriter;

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

        if ("Omkar1122".equals(uname) && "Omkar@1234".equals(pass)) {
            out.println("<script>");
            out.println("alert('Login Successful');");
            out.println("window.location.href='Home.html';");
            out.println("</script>");
        } else {
            out.println("<script>");
            out.println("alert('Login Failed');");
            out.println("window.location.href='Login.html';");
            out.println("</script>");
        }
    }
}
