// Import Servlet Libraries
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

// Import Java Libraries
import java.io.*;

@WebServlet(name = "HelloServlet", urlPatterns = {"/Hello"})
public class Hello extends HttpServlet // Inheriting from HttpServlet makes this a servlet
{
    public void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet example</title>");
        out.println("</head>");
        out.println("<body>");
        out.println(" <b>Ni hao!</b><br> <!--  Chinese -->");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}