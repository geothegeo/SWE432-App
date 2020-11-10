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

import javax.servlet.annotation.WebServlet;
@WebServlet( name = "ForwardPredicateServlet", urlPatterns = {"/ForwardPredicateServlet"} )

public class ForwardPredicateServlet extends HttpServlet {
    
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
      
      String printTable = "<table class=\"table\" ><thead><tr>";
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
          String result = se.eval(myExpression).toString();
          if(result.equals("false")) {
            result = "0";
          }
          else if(result.equals("true")) {
            result = "1";
          }
          printTable = printTable + "<td>" + result + "</td></tr>";
        }
        
      } catch (ScriptException e) {
        System.out.println("Invalid Expression");
        e.printStackTrace();
      }
      printTable += "</tbody></table>";
      
      String predicate = "Displaying result for: " + request.getParameter("predicate");
      
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println(predicate);
      out.println(printTable);
    }  // End doPost

}
