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
      String keyword = request.getParameter("keyword");
      search(keyword, response);
   }

   void search(String keyword, HttpServletResponse response) throws IOException {
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

         if (keyword.isEmpty()) {
            String selectSQL = "SELECT * FROM myTableTE";
            preparedStatement = connection.prepareStatement(selectSQL);
         } else {
            String selectSQL = "SELECT * FROM myTableTE WHERE NAME LIKE ?";
            String theUserName = keyword + "%";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, theUserName);
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            //int id = rs.getInt("id");
            String species = rs.getString("species").trim();
            String name = rs.getString("name").trim();
            String type = rs.getString("type").trim();
            String method = rs.getString("method").trim();
            String part_desc = rs.getString("part_desc").trim();

            if (keyword.isEmpty() || name.contains(keyword)) {
               //out.println("ID: " + id + ", ");
               out.println("Species: " + species + "<br>");
               out.println("Armor Name: " + name + "<br>");
               out.println("Armor Type: " + type + "<br>");
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
