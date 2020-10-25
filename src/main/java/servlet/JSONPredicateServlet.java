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

  // Button labels
  static String OperationSave = "Save";

  public class Entry {
    List<String> operators;
    List<String> variables;
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
      htmlOut.append("<tr><th>Name</th><th>Age</th></tr>");
      if(entries == null
          || entries.entries == null || entries.entries.size() == 0){
        htmlOut.append("<tr><td>No entries yet.</td></tr>");
      }else{
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
    
     for(int i = 1; i < inputAmnt; i++) {
       operators.add(request.getParameter("input" + i));
     }
     for(int i = 1; i <= inputAmnt; i++) {
       variables.add(request.getParameter("var" + i));
     }

      EntryManager entryManager = new EntryManager();
      entryManager.setFilePath(RESOURCE_FILE);
      Entries newEntries=entryManager.save(operators, variables);

      printHead(out);
      if(newEntries ==  null){
      printResponseBody(out, "No Entry");
      }else{
      printResponseBody(out, entryManager.getAllAsHTMLTable(newEntries));
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
     private void printResponseBody(out, "doGet");
     printTail(out);
  }

  /** *****************************************************
   *  Prints the <head> of the HTML page, no <body>.
  ********************************************************* */
  private void printHead (PrintWriter out){
     out.println("<html>");
     out.println("");
     out.println("<head>");
     out.println("<title>JSON File Persistence Example</title>");
     // Put the focus in the name field
     out.println("</head>");
     out.println("");
  }

  /** *****************************************************
   *  Prints the <BODY> of the HTML page
  ********************************************************* */
//   private void printBody (
//     PrintWriter out, String name, String age, String error){
//     out.println("<body onLoad=\"setFocus()\">");
//     out.println("<p>");
//     out.println(
//       "A simple example that demonstrates how to persist JSON data to a file"
//       );
//     out.println("</p>");

//     if(error != null && error.length() > 0){
//       out.println(
//       "<p style=\"color:red;\">Please correct the following and resubmit.</p>"
//       );
//       out.println("<ol>");
//       out.println(error);
//       out.println("</ol>");
//     }

//     out.print  ("<form name=\"persist2file\" method=\"post\"");
//     out.println(" action=\""+Domain+Path+Servlet+"\">");
//     out.println("");
//     out.println(" <table>");
//     out.println("  <tr>");
//     out.println("   <td>Name:</td>");
//     out.println("   <td><input type=\"text\" name=\""+Data.NAME.name()
//       +"\" value=\""+name+"\" size=30 required></td>");
//     out.println("  </tr>");
//     out.println("  <tr>");
//     out.println("   <td>Age:</td>");
//     out.println("   <td><input type=\"text\"  name=\""+Data.AGE.name()
//       +"\" oninput=\"this.value=this.value.replace(/[^0-9]/g,'');\" value=\""
//       +age+"\" size=3 required></td>");
//     out.println("  </tr>");
//     out.println(" </table>");
//     out.println(" <br>");
//     out.println(" <br>");
//     out.println(" <input type=\"submit\" value=\"" + OperationAdd
//       + "\" name=\"Operation\">");
//     out.println(" <input type=\"reset\" value=\"Reset\" name=\"reset\">");
//     out.println("</form>");
//     out.println("");
//     out.println("</body>");
//   }

  /** *****************************************************
   *  Prints the <BODY> of the HTML page with persisted entries
  ********************************************************* */
  private void printResponseBody (PrintWriter out, String tableString){
    out.println("<body>");
    out.println("<p>");
    out.println("A simple example that shows entries from a JSON file");
    out.println("</p>");
    out.println("");
    out.println(tableString);
    out.println("");
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
