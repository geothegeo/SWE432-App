package servlet;
// Written by David Gonzalez, April 2020
// Modified by Jeff Offutt
// Built to deploy in github with Heroku
import java.util.List;
import java.util.ArrayList;

/*
requires Gson in your pom.xml
<dependencies>
...

  <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.5</version>
    </dependency>

...
*/
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "JSONPredicateServlet", urlPatterns = {"/jsonServlet"})
public class JSONPredicateServlet extends HttpServlet{
  static String RESOURCE_FILE = "entries.json";

  static String Domain  = "https://swe432-webapp.herokuapp.com/";
  static String Path    = "/";
  static String Servlet = "jsonServlet";

  static String JSONServlet = "PredicateServlet";

// Other strings.
  static String Style1 = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css";
  static String Style2 = "https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js";
  static String Style3 = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js";

  static String BJS1 = "https://code.jquery.com/jquery-3.3.1.slim.min.js";
  static String BJS2 = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js";
  static String BJS3 = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js";

  static Integer divNum = 0;

  public class Entry {
    List<String> operators;
    List<String> variables;
    Integer inputAmnt;
    String predicate;
  }

  public class Entries{
    List<Entry> entries;
  }

  public class EntryManager{
    private String filePath = null;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public Entries save(List<String> operators, List<String> variables){
      Entries entries = getAll();
      Entry newEntry = new Entry();
      newEntry.operators = operators;
      newEntry.variables = variables;
      newEntry.inputAmnt = inputAmnt;
      newEntry.predicate = predicate;
      entries.entries.add(newEntry);
      try{
        FileWriter fileWriter = new FileWriter(filePath);
        new Gson().toJson(entries, fileWriter);
        fileWriter.flush();
        fileWriter.close();
      }catch(IOException ioException){
        return null;
      }

      return entries;
    }

    private Entries getAll(){
      Entries entries =  entries = new Entries();
      entries.entries = new ArrayList();

      try{
        File file = new File(filePath);
        if(!file.exists()){
          return entries;
        }

        BufferedReader bufferedReader =
          new BufferedReader(new FileReader(file));
        Entries readEntries =
          new Gson().fromJson(bufferedReader, Entries.class);

        if(readEntries != null && readEntries.entries != null){
          entries = readEntries;
        }
        bufferedReader.close();

      }catch(IOException ioException){
      }

      return entries;
    }
    
    /***********************************************************************/

    public String getAllAsHTMLTable(Entries entries){
      StringBuilder htmlOut = new StringBuilder("<table>");
      if(entries == null || entries.entries == null || entries.entries.size() == 0){
        htmlOut.append("<tr><td>No entries yet.</td></tr>");
      }else{
        divNum = entries.entries.size();
        for(Entry entry: entries.entries){          
           for(String var: entry.variables){
           	htmlOut.append("<tr><td>"+var+"</td></tr>");
           }
           for (String op: entry.operators){
             htmlOut.append("<tr><td>"+op+"</td></tr>");
           }

        }
      }
      htmlOut.append("</table>");
      return htmlOut.toString();
    }

    public String createDropDown(Entries entries) {
      StringBuilder htmlOut = new StringBuilder("<p>");
      if(entries == null || entries.entries == null || entries.entries.size() == 0){
        htmlOut.append("No predicates");
      }else{
      htmlOut.append("<select name=\"predicate\" class=\"form-group\" id=\"predicate\">");
      htmlOut.append("<option value=\"\" disabled selected>-- Choose Your Predicate --</option>");
        Integer i = 0;
        Integer v = 1;
        Integer o = 1;
        for(Entry entry: entries.entries){
           htmlOut.append("<option value=\"" + i + "\">" + entry.predicate + "</option>");
           htmlOut.append("<div id=\"bunlde" + i + "\"");
           for(String var: entry.variables){
           	 htmlOut.append("<input type=\"hidden\" id=\"var" + v + "\" name=\"var" + v + "\" value=" + var + ">");
             v++;
           }
           for (String op: entry.operators){
             htmlOut.append("<input type=\"hidden\" id=\"input" + o + "\" name=\"input" + o + "\" value=" + op + ">");
             o++;
           }
           htmlOut.append("<input type=\"hidden\" id=\"inputNum\" name=\"inputNum\" value=" + inputAmnt + ">");
           htmlOut.append("</div>");
           i ++;
        }
        htmlOut.append("</select>");
        htmlOut.append("<input type=\"submit\" class=\"btn btn-primary\" id=\"submitForm\" value=\"Show Table\"/></div>");
      }
      htmlOut.append("</p>");
      return htmlOut.toString();
    }
    
  }

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

     String predicate = request.getParameter("predicate");
    
     for(int i = 1; i < inputAmnt; i++) {
       operators.add(request.getParameter("input" + i));
     }
     for(int i = 1; i <= inputAmnt; i++) {
       variables.add(request.getParameter("var" + i));
     }

     response.setContentType("text/html");
     PrintWriter out = response.getWriter();

      EntryManager entryManager = new EntryManager();
      entryManager.setFilePath(RESOURCE_FILE);
      Entries newEntries=entryManager.save(operators, variables, inputAmnt, predicate);

      printHead(out);
      if(newEntries ==  null){
      printBody(out, "No Entry", "");
      }else{
      printBody(out, entryManager.getAllAsHTMLTable(newEntries), entryManager.createDropDown(newEntries));
      }
      printTail(out);

  }

  /** *****************************************************
   *  Overrides HttpServlet's doGet().
   *  Prints an HTML page with a blank form.
  ********************************************************* */
  @Override
  public void doGet (HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException{
     response.setContentType("text/html");
     PrintWriter out = response.getWriter();
     printHead(out);
     printBody(out, "doGet", "");
     printTail(out);
  }

  /** *****************************************************
   *  Prints the <head> of the HTML page, no <body>.
  ********************************************************* */
  private void printHead (PrintWriter out){
     out.println("<html>");
     out.println("");
     out.println("<head>");
     out.println("<title>JSON Predicate File</title>");
     out.println(" <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
     out.println(" <link rel=\"stylesheet\" href=\"" + Style1 + "\">");
     out.println(" <link rel=\"stylesheet\" href=\"" + Style2 + "\">");
     out.println(" <link rel=\"stylesheet\" href=\"" + Style3 + "\">");

     out.println(" <script src=\"" + BJS1 + "\"></script>");
     out.println(" <script src=\"" + BJS2 + "\"></script>");
     out.println(" <script src=\"" + BJS3 + "\"></script>");

     out.println(" <script>");
     out.println(" function cleanUpForm() { userVal = document.getElementById(\"inputNum\").value; var d;");
     out.println(" for (d = 0; d < " + divNum + "; d++) { if (d != userVal)");
     out.println(" document.getElementById(\"div\"+d).innerHTML = \"\";} return true;");
     out.println(" </script>");

     out.println("</head>");
     out.println("");
  }

  /** *****************************************************
   *  Prints the <BODY> of the HTML page with persisted entries
  ********************************************************* */
  private void printBody (PrintWriter out, String tableString, String optionString){
    out.println("<body>");
    out.println("<p>");
    out.println("A simple example that shows entries from a JSON file");
    out.println("</p>");
    out.println("");
    out.println(tableString);
    out.println("");
    out.println("<form id=\"JSONForm\" class=\"form-inline\" method=\"post\" onsubmit=\"return cleanUpForm()\"");
    out.println(" action=\"/" + PredicateServlet + "\">");
    out.println(optionString);
    out.println("</body>");
  }

  /** *****************************************************
   *  Prints the bottom of the HTML page.
  ********************************************************* */
  private void printTail (PrintWriter out){
     out.println("");
     out.println("</html>");
  }
}
