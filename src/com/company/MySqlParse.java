package com.company;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MySqlParse {
  private static Connection con = null;

  public void parse() throws IOException, ParserConfigurationException, SAXException, SQLException {
    con =
        DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/met622?useTimezone=true" + "&serverTimezone=UTC",
            "root",
            "");
    PreparedStatement stmt =
        con.prepareStatement("INSERT INTO Articles(\n" + "  Title, date)\n" + "VALUES(?, ?)");
    try {
      // Instantiate BufferedReader to read user input
      // produce DOM object trees
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder(); // obtain document from XML
      Document document = builder.parse(new File("pubmed1221.xml")); // access the document
      document.getDocumentElement().normalize(); // method to put all text nodes into a normal form
      // NodeList object with a collection of all elements with the tag name Journal
      NodeList nodeList = document.getElementsByTagName("Journal");
      // iterate the node list
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) node;
          List<String> columns =
              Arrays.asList(
                  element.getElementsByTagName("Title").item(0).getTextContent(),
                  element.getElementsByTagName("PubDate").item(0).getTextContent());
          for (int n = 0; n < columns.size(); n++) {
            stmt.setString(n + 1, columns.get(n));
          }
          stmt.execute();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
