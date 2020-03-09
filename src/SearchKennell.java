import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SearchKennell")
public class SearchKennell extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SearchKennell() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  String species = request.getParameter("species");
	  String keyword = request.getParameter("keyword");
      String type = request.getParameter("type");
      search(species, keyword, type, response);
      //search(keyword, response);
   }

   void search(String species, String keyword, String type, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Armor Results";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnectionKennell.getDBConnection();
         connection = DBConnectionKennell.connection;

         if (species.isEmpty() && keyword.isEmpty() && type.isEmpty()) {
            String selectSQL = "SELECT * FROM myTableTE";
            preparedStatement = connection.prepareStatement(selectSQL);
         } else if (species.isEmpty()){
             String selectSQL = "SELECT * FROM myTableTE WHERE NAME LIKE ? AND TYPE LIKE ?";
             String theUserName = keyword + "%";
             String theTypeName = type + "%";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, theUserName);
             preparedStatement.setString(2, theTypeName);
          } else if (species.isEmpty() && keyword.isEmpty()){
              String selectSQL = "SELECT * FROM myTableTE WHERE TYPE LIKE ?";
              String theTypeName = type + "%";
              preparedStatement = connection.prepareStatement(selectSQL);
              preparedStatement.setString(1, theTypeName);
           } else if (species.isEmpty() && type.isEmpty()){
               String selectSQL = "SELECT * FROM myTableTE WHERE NAME LIKE ?";
               String theUserName = keyword + "%";
               preparedStatement = connection.prepareStatement(selectSQL);
               preparedStatement.setString(1, theUserName);
            } else if (keyword.isEmpty()){
            String selectSQL = "SELECT * FROM myTableTE WHERE SPECIES LIKE ? AND TYPE LIKE ?";
            String theSpeciesName = species + "%";
            String theTypeName = type + "%";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, theSpeciesName);
            preparedStatement.setString(2, theTypeName);
         } else if (keyword.isEmpty() && type.isEmpty()){
             String selectSQL = "SELECT * FROM myTableTE WHERE SPECIES LIKE ?";
             String theSpeciesName = species + "%";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, theSpeciesName);
          } else if (type.isEmpty()){
             String selectSQL = "SELECT * FROM myTableTE WHERE SPECIES LIKE ? AND NAME LIKE ?";
             String theSpeciesName = species + "%";
             String theUserName = keyword + "%";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, theSpeciesName);
             preparedStatement.setString(2, theUserName);
         } else {
             String selectSQL = "SELECT * FROM myTableTE WHERE SPECIES LIKE ? AND NAME LIKE ? AND TYPE LIKE ?";
             String theSpeciesName = species + "%";
             String theUserName = keyword + "%";
             String theTypeName = type + "%";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, theSpeciesName);
             preparedStatement.setString(2, theUserName);
             preparedStatement.setString(3, theTypeName);
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            //int id = rs.getInt("id");
            String speciesname = rs.getString("species").trim();
            String name = rs.getString("name").trim();
            String typename = rs.getString("type").trim();
            String method = rs.getString("method").trim();
            String part_desc = rs.getString("part_desc").trim();

            if (keyword.isEmpty() || name.contains(keyword)) {
               //out.println("ID: " + id + ", ");
               out.println("Species: " + speciesname + "<br>");
               out.println("Armor Name: " + name + "<br>");
               out.println("Armor Type: " + typename + "<br>");
               out.println("Unlock Method: " + method + "<br>");
               out.println("Part Description: " + part_desc + "<br>");
            }
         }
         out.println("<a href=/webproject-TE-Kennell/search_kennell.html>Halo 3 Armor Database</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
