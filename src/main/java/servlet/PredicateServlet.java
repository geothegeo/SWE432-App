/** *****************************************************************
    PredicateServlet.java   servlet example

        @author Morgan Abreau, George Tang
********************************************************************* */

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Adds servlet mapping annotation
import javax.servlet.annotation.WebServlet;
@WebServlet( name = "PredicateServlet", urlPatterns = {"/PredicateServlet"} )

// PredicateServlet class
// CONSTRUCTOR: no constructor specified (default)
//
// ***************  PUBLIC OPERATIONS  **********************************
// public void doPost ()  --> prints a blank HTML page
// public void doGet ()  --> prints a blank HTML page
// private void PrintHead (PrintWriter out) --> Prints the HTML head section
// private void PrintBody (PrintWriter out) --> Prints the HTML body with
//              the form. Fields are blank.
// private void PrintBody (PrintWriter out, String lhs, String rhs, String rslt)
//              Prints the HTML body with the form.
//              Fields are filled from the parameters.
// private void PrintTail (PrintWriter out) --> Prints the HTML bottom
//***********************************************************************

public class TwoButtonsServlet extends HttpServlet
{

// Location of servlet.
// Adds the path of your form submit action. Will be different for each person
static String Domain  = "https://swe432-webapp.herokuapp.com/";
static String Path    = "";
static String Servlet = "PredicateServlet";

// Button labels
static String ...

// Other strings.
static String Style = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css";
static String Style = "https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js";
static String Style = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js";
static String BJS1 = "https://code.jquery.com/jquery-3.3.1.slim.min.js";
static String BJS2 = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js";
static String BJS3 = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js";
static String JS = "../../js/main.js";

/** *****************************************************
 *  Overrides HttpServlet's doPost().
 *  Converts the values in the form, performs the operation
 *  indicated by the submit button, and sends the results
 *  back to the client.
********************************************************* */
@Override
public void doPost (HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException
{

   // 

   response.setContentType("text/html");
   PrintWriter out = response.getWriter();
   PrintHead(out);
   PrintBody(out, ...);
   PrintTail(out);
}  // End doPost

/** *****************************************************
 *  Overrides HttpServlet's doGet().
 *  Prints an HTML page with a blank form.
********************************************************* */
@Override
public void doGet (HttpServletRequest request, HttpServletResponse response)
       throws ServletException, IOException
{
   response.setContentType("text/html");
   PrintWriter out = response.getWriter();
   PrintHead(out);
   PrintBody(out);
   PrintTail(out);
} // End doGet

/** *****************************************************
 *  Prints the <head> of the HTML page, no <body>.
********************************************************* */
private void PrintHead (PrintWriter out)
{
   out.println("<html>");
   out.println("");

   out.println("<head>");
   out.println("<titleSWE Assignment 5</title>");
   out.println(" <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
   out.println(" <link rel=\"stylesheet\" href=\"" + Style + "\">");
   out.println(" <link rel=\"stylesheet\" href=\"" + Style2 + "\">");
   out.println(" <link rel=\"stylesheet\" href=\"" + Style3 + "\">");
   out.println("</head>");
   out.println("");
} // End PrintHead

/** *****************************************************
 *  Prints the <BODY> of the HTML page with the form data
 *  values from the parameters.
********************************************************* */
private void PrintBody (PrintWriter out, ...)
{
   out.println("<body>");
   out.println("<div id=\"predicateForm\" class=\"container-fluid w-75 p-5 mt-10\">");
   out.println("<h1>SWE 432 - Assignment 5</h1>");
   out.println("<br />");
   out.println("<h5>This form accepts logic predicates. You must first choose the number of variables you want to use. Then, enter the variable name(s) into the textbox(es). You may
              include the NOT operator in your variable name in the following format: !variablename. Finally, select the appropriate operator(s) from the drop-down menu(s) 
              and click \"Print Table\" when all fields are filled.</h5>");
   out.println("<br />");
   out.println("<h6>&emsp;Note: The operator(s) will be processed in its capitalized form, and you are limited to up to 10 variables.</h6>");
   out.println("<br />");

   out.println("<form id=\"inputForm\" class=\"form-inline\" method=\"post\"");
   out.println(" action=\"/" + Servlet + "\">");
   out.println("<div class=\"container-fluid\">");
   out.println("<div id=\"submitInfo\" class=\"row\" >");
   out.println("<div class=\"form-group\">");
   out.println("<label for=\"inputNum\" style=\"margin-right: 10px;\">Number of variables: </label>");
   out.println("<input type=\"number\" class=\"form-control\" id=\"inputNum\" name=\"inputNum\" min=\"0\" max=\"10\" value=\"1\">");
   out.println("</div>");
   out.println("</div>");

   out.println("<div class=\"row submitInfo\">");
   out.println("<div class=\"col-lg-3\"><p style=\"text-align: center;\">Your Predicate:</p></div>");
   out.println("<div class=\"col-lg-8\"><p id=\"predicateTxt\"></p></div>");
   out.println("<div class=\"col-lg-1\"><input type=\"submit\" class=\"btn btn-primary\" id=\"submitForm\" value=\"Show Table\"/></div>");
   out.println("</div>");

   out.println("<div class=\"row\">");
   out.println("<div class=\"col-lg-3\"><div id=\"lblInput\"></div></div>");
   out.println("<div class=\"col-lg-9\"><div id=\"result\"></div></div>");
   out.println("</div>");
   out.println("</div>");
   out.println("</form>");
   out.println("<br />");
   
   out.println("<form class=\"form-inline\">");
   out.println("<label for=\"outlineBg\" style=\"margin-right: 10px;\">Change the outline's background color: </label>");
   out.println("<input type=\"color\" id=\"outlineBg\" name=\"outlineBg\" value=\"#ffffff\">");
   out.println("</form>");

   out.println("<p><b>Collaboration Summary: George Tang (gtang2), Morgan Abreu (mabreu3)</b><br />");
   out.println("Optional Elements focused: <br />");
   out.println("1) Validate the input predicate and report invalid strings to the users in a clear way <br />");
   out.println("2) Allow multiple syntaxes for logical operators (for example, “&,” “&&,” “AND,” “and,” etc. <br />");
   out.println("3) Include an additional logical operator—exclusive or <br />");
   out.println("Morgan focused on the POST request while George focused on the GET request. Both worked on outpitting the table.</p>");
   out.println("</div>");

   out.println(" <script src=\"" + BJS1 + "\">");
   out.println(" <script src=\"" + BJS2 + "\">");
   out.println(" <script src=\"" + BJS3 + "\">");
   out.println(" <script src=\"" + JS + "\">");
   out.println("</body>");
} // End PrintBody

/** *****************************************************
 *  Overloads PrintBody (out, ...) to print a page
 *  with blanks in the form fields.
********************************************************* */
private void PrintBody (PrintWriter out)
{
   PrintBody(out ...);
}

/** *****************************************************
 *  Prints the bottom of the HTML page.
********************************************************* */
private void PrintTail (PrintWriter out)
{
   out.println("");
   out.println("</html>");
} // End PrintTail

}  // End PredicateServlet
