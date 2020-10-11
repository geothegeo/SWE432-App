/** *****************************************************************
  PredicateServlet.java   servlet example
  
  @author Morgan Abreu, George Tang
  ********************************************************************* */

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.ArrayList;

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
  
  public class PredicateServlet extends HttpServlet
{
  
// Location of servlet.
// Adds the path of your form submit action. Will be different for each person
  static String Domain  = "https://swe432-webapp.herokuapp.com/";
  static String Path    = "";
  static String Servlet = "PredicateServlet";
  
// Other strings.
  static String Style1 = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css";
  static String Style2 = "https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js";
  static String Style3 = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js";
  static String Style = "http://mason.gmu.edu/~gtang2/other/main.css";
  static String BJS1 = "https://code.jquery.com/jquery-3.3.1.slim.min.js";
  static String BJS2 = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js";
  static String BJS3 = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js";
  static String JS = "http://mason.gmu.edu/~gtang2/other/main.js";
  
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
    List<String> operators = new ArrayList<String>();
    List<String> variables = new ArrayList<String>();
    
    Integer inputAmnt = new Integer(0);
    String inputNum = request.getParameter("inputNum");
    if ((inputNum != null) && (inputNum.length() > 0)) {
      inputAmnt = new Integer(inputNum);
    }
    
    for(int i = 1; i < inputAmnt; i++) {
      operators.add(request.getParameter("input" + i));
    }
    
    String printTable = "<thead><tr>";
    for(int i = 1; i <= inputAmnt; i++) {
      String varName = request.getParameter("var" + i);
      variables.add(varName);
      printTable = printTable + "<th scope=\"col\">" + varName + "</th>";
    }
    printTable += "<th scope=\"col\"> Result </th></tr></thead>";
    
    try {
      ScriptEngineManager sem = new ScriptEngineManager();
      ScriptEngine se = sem.getEngineByName("JavaScript");            
      
      printTable += "<tbody>";
      for(int i = 0; i < Math.pow(2, inputAmnt); i++) { // build expression for each possible combination of boolean values
        String myExpression = "";
        String values = Integer.toBinaryString(i); // start at 00..00 in binary and use this value for boolean values in expression
        // pad values with 0s until the number of "bits" equals the number of variables in the equation
        while(values.length() < inputAmnt) {
          values = "0" + values;
        }
        for(int j = 0; j < values.length(); j++) { // extract each "bit" and add it to expression
          if(variables.get(j).startsWith("!")) {
            myExpression += "!";
          }
          myExpression += String.valueOf(values.charAt(j));
          if(j < operators.size()) { // after retrieving "bit", add the operator unless it's the last "bit"
            myExpression += (operators.get(j));
          }
        }
        // print row
        printTable += "<tr>";
        for(int k = 0; k < values.length(); k++) { // iterate over "binary" to print current input
          printTable = printTable + "<td>" + values.charAt(k) + "</td>";
        }
        printTable = printTable + "<td>" + se.eval(myExpression) + "</td></tr>";
      }
      
    } catch (ScriptException e) {
      System.out.println("Invalid Expression");
      e.printStackTrace();
    }
    printTable += "</tbody></table>";
    
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    PrintHead(out);
    PrintBody(out, printTable);
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
    out.println(" <link rel=\"stylesheet\" href=\"" + Style1 + "\">");
    out.println(" <link rel=\"stylesheet\" href=\"" + Style2 + "\">");
    out.println(" <link rel=\"stylesheet\" href=\"" + Style3 + "\">");
    out.println(" <link rel=\"stylesheet\" href=\"" + Style + "\">");
    
    out.println(" <script src=\"" + BJS1 + "\"></script>");
    out.println(" <script src=\"" + BJS2 + "\"></script>");
    out.println(" <script src=\"" + BJS3 + "\"></script>");
    
    out.println(" <script>");
    out.println(" var variables = \"1\"; var i;");
    out.println(" var formGroup, inputLabel, selectLabel, inputName, varName;");
    out.println(" var predicate = \"\";");
    
    out.println(" window.onload = function() { startUp(); selectInputs(); showUserInputs(variables);} ");
    
    out.println(" function startUp() { var bgColor = document.getElementById(\"outlineBg\");");
    out.println(" bgColor.value = \"#ffffff\"; bgColor.addEventListener(\"input\", updateColor, false); }");
    
    out.println(" function updateColor(event) {");
    out.println(" document.getElementById(\"predicateForm\").setAttribute(\"style\", \"background-color: \" + event.target.value + \";\"); }");
    
    out.println(" function selectInputs() { var inputNum = document.getElementById(\"inputNum\");");
    out.println(" variables = inputNum.value; inputNum.addEventListener(\"change\", updateVarNum, false); }");
    
    out.println(" function updateVarNum(event) { variables = document.getElementById(\"inputNum\").value;");
    out.println(" var lblInput = document.getElementById(\"lblInput\"); lblInput.innerHTML = \"\"; showUserInputs(variables); }");
    
    out.println(" function showUserInputs(variables) { var lblInput = document.getElementById(\"lblInput\");");
    out.println(" var opTxt = [\"OR, or, |, ||\", \"AND, and, &, &&\", \"XOR, xor\"]; var opVal = [\"||\", \"&&\", \"^\"];");
    out.println(" for(i = 1; i <= parseInt(variables); i++) { if (i != 1) {");
    out.println(" var formGroup = document.createElement(\"div\"); formGroup.className = \"form-group\"; ");
    out.println(" var selectLabel = document.createElement(\"label\"); selectLabel.for = \"input\" + (i-1);");
    out.println(" selectLabel.className = \"col-md-4\"; selectLabel.innerHTML = \"Operator \" + (i-1) + \":\"; formGroup.appendChild(selectLabel);");
    out.println(" var inputName = document.createElement(\"select\"); inputName.className = \"col-md-7 form-control\";");
    out.println(" inputName.id = \"input\" + (i-1); inputName.name = \"input\" + (i-1); inputName.addEventListener(\"change\", updatePredicate, false);");
    out.println(" createDrop(inputName, opTxt, opVal); formGroup.appendChild(inputName); lblInput.appendChild(formGroup); }");
    out.println(" var formGroup = document.createElement(\"div\"); formGroup.className = \"form-group\";");
    out.println(" var inputLabel = document.createElement(\"label\"); inputLabel.for = \"var\" + i; inputLabel.className = \"col-md-4\";");
    out.println(" inputLabel.innerHTML = \"Variable \" + i + \":\"; formGroup.appendChild(inputLabel);");
    out.println(" var varName = document.createElement(\"input\"); varName.type = \"text\"; varName.className = \"col-md-7 form-control\";");
    out.println(" varName.id = \"var\" + i; varName.name = \"var\" + i; varName.addEventListener(\"change\", updatePredicate, false);");
    out.println(" formGroup.appendChild(varName); lblInput.appendChild(formGroup); } }");
    
    out.println(" function createDrop (inputElement, arr1, arr2) { var option = document.createElement(\"option\");");
    out.println(" option.text = \"-- Pick Op --\"; option.value = \"\"; option.selected = \"selected\";");
    out.println(" option.disabled = \"disabled\"; inputElement.appendChild(option); for (var i = 0; i < 4; i++) {");
    out.println(" option = document.createElement(\"option\"); option.text = arr1[i]; option.value = arr2[i];");
    out.println(" inputElement.appendChild(option); } }");
    
    out.println(" function updatePredicate(event) { predicateTxt = document.getElementById(\"predicateTxt\");");
    out.println(" predicateTxt.innerHTML = \"\"; predicate = \"\"; for(i = 1; i <= parseInt(variables); i++) {");
    out.println(" if (i != 1) { predicate += document.getElementById(\"input\" + (i-1)).value + \"  \"; }");
    out.println(" predicate += document.getElementById(\"var\" + i).value + \"  \"; } predicateTxt.innerHTML = predicate; }");
    
    out.println(" function validateForm() { var j; ");
    out.println(" for (j = 1; j <= parseInt(variables); j++) { var regex = /^\\!?[a-zA-Z]{1,15}[0-9]{0,3}$/;");
    out.println(" var ctrl =  document.getElementById(\"var\" + j).value.trim(); var testing = regex.test(ctrl);");
    out.println(" if (testing == false) { alert(\"Invalid name for Variable\" + j); return false; } } return true; }");
    out.println(" </script>");
    
    out.println("</head>");
    out.println("");
  } // End PrintHead
  
  /** *****************************************************
    *  Prints the <BODY> of the HTML page with the form data
    *  values from the parameters.
    ********************************************************* */
  private void PrintBody (PrintWriter out, String printTable)
  {
    out.println("<body>");
    out.println("<div id=\"predicateForm\" class=\"container-fluid w-75 p-5 mt-10\">");
    out.println("<h1>SWE 432 - Assignment 5</h1>");
    out.println("<br />");
    out.println("<h5>This form accepts logic predicates. You must first choose the number of variables you want to use. Then, enter the variable name(s) into the textbox(es). You may " +
                "include the NOT operator in your variable name in the following format: !variablename. Finally, select the appropriate operator(s) from the drop-down menu(s) " +
                "and click \"Print Table\" when all fields are filled.</h5>");
    out.println("<br />");
    out.println("<h6>&emsp;Note: The operator(s) will be processed in its capitalized form, and you are limited to up to 10 variables.</h6>");
    out.println("<br />");
    
    out.println("<form id=\"inputForm\" class=\"form-inline\" method=\"post\"");
    out.println(" action=\"/" + Servlet + "\" onsubmit=\"return validateForm()\">");
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
    out.println("<div class=\"col-lg-9\"><div id=\"result\">" + printTable + "</div></div>");
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
    out.println("2) Allow multiple syntaxes for logical operators (for example, \"&\", \"&&\", \"AND\", \"and\" etc.) <br />");
    out.println("3) Include an additional logical operator - exclusive or <br />");
    out.println("Morgan focused on the POST request while George focused on the GET request. Both worked on outputting the table.</p>");
    out.println("</div>");
    
    out.println("</body>");
  } // End PrintBody
  
  /** *****************************************************
    *  Overloads PrintBody (out, ...) to print a page
    *  with blanks in the form fields.
    ********************************************************* */
  private void PrintBody (PrintWriter out)
  {
    PrintBody(out, "");
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
