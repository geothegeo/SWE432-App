package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.*;
import java.sql.*;

/*require mysql dependency in your pom.xml
 <dependencies>
...

    <dependency>
    	<groupId>mysql</groupId>
    	<artifactId>mysql-connector-java</artifactId>
    	<version>8.0.15</version>
    </dependency>
...
*/

@WebServlet(name = "Starter", urlPatterns = {"/search"})
public class StarterServlet extends HttpServlet{
    private Statement stmt;
    private Connection conn;
    private String URL="jdbc:mysql://p2jxdb.mysql.database.azure.com:3306/papers?useSSL=true&requireSSL=false&serverTimezone=UTC";
    private String username="db_user@p2jxdb";
    private String password="zksTu8%d";
   

    public void init (ServletConfig config)
	throws ServletException
    {
	super.init(config);
	try{
	    conn=DriverManager.getConnection(URL,username,password);
	    stmt=conn.createStatement();
	}
	catch (Exception e){
	    e.printStackTrace();
	    conn=null;
	}
    }

    public void destroy(){
	     try{
		 stmt.close();
		 conn.close();
	     }
	     catch (Exception e){
		 System.err.println("problem closing the database");
	     }
    }
}

		    
			    
				   
				   
			       

