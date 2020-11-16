package com.company;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

public class Main {

  private static Connection con = null;

  static {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException ex) {
      System.err.println("Driver not found: " + ex.getMessage());
    }
  }

  public static void main(String[] args)
      throws SQLException, ParserConfigurationException, IOException, SAXException,
          XPathExpressionException {
    Properties pass = new Properties();
    Mongo mg = new Mongo();
    con =
        DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/met622?useTimezone=true" + "&serverTimezone=UTC",
            "root",
            "");
    con.createStatement().execute("DROP TABLE Articles");

    con.createStatement()
        .execute(
            "CREATE TABLE Articles(\n"
                + "  id integer primary key auto_increment,\n"
                + "  Title varchar(250) not null,\n"
                + "  Date varchar(25) not null\n"
                + ")");
    
    MySqlParse p = new MySqlParse();
    p.parse();
    con.createStatement()
        .execute(
            "SELECT COUNT(*) FROM Articles \n"
                + "WHERE (\n"
                + "title like '%cancer%'\n"
                + "OR title like '%obesity%'\n"
                + "AND Date like '2018%')");
    con.createStatement()
        .execute(
            "SELECT COUNT(*) FROM Articles \n"
                + "WHERE (\n"
                + "title like '%cancer%'\n"
                + "OR title like '%obesity%'\n"
                + "AND Date like '2019%')");
    con.createStatement()
        .execute(
            "SELECT COUNT(*) FROM Articles \n"
                + "WHERE (\n"
                + "title like '%cancer%'\n"
                + "OR title like '%obesity%'\n"
                + "AND Date like '2020%')");
  
    // range of titles between 2018 and 2019
    con.createStatement()
        .execute(
            "SELECT * FROM Articles \n"
                + "WHERE date "
                + "BETWEEN '2018%' AND '2019%'");
    
    
    
    mg.mongoParse(); // parse the xml and add documents to the collection
    mg.mongoSearch(); // method to search for cancer and obesity for year 2017 - 2019
    
  }
}
