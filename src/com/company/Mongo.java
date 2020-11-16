package com.company;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Mongo {

  public static void mongoParse() {
    BasicDBObject doc2remove = new BasicDBObject();
    // initialize the client object
    MongoClient mongoClient = new MongoClient();
    // get the 'test' dataset
    MongoDatabase dbObj = mongoClient.getDatabase("test");
    //    MongoCollection<org.bson.Document> collection = dbObj.getCollection("hw5");
    //    collection.drop();
    //    dbObj.createCollection("hw5");
    MongoCollection<org.bson.Document> col = dbObj.getCollection("hw5");
    Iterator it = col.find().iterator();
    ArrayList docs = new ArrayList<Document>();

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
          List<String> columnsTitle =
              Arrays.asList(element.getElementsByTagName("Title").item(0).getTextContent());
          List<String> columnsDate =
              Arrays.asList(element.getElementsByTagName("PubDate").item(0).getTextContent());
          for (int n = 0; n < columnsTitle.size(); n++) {
            for (int v = 0; v < columnsDate.size(); v++) {
              org.bson.Document d1 = new org.bson.Document();
              d1.append("title", columnsTitle.get(n));
              d1.append("date", columnsDate.get(n));
              docs.add(d1);
            }
          }
        }
      }
      col.insertMany(docs);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void mongoSearch() {
    BasicDBObject doc2remove = new BasicDBObject();
    MongoClient mongoClient = new MongoClient();
    MongoDatabase dbObj = mongoClient.getDatabase("test");
    MongoCollection<org.bson.Document> col = dbObj.getCollection("hw5");
    BasicDBObject whereQuery = new BasicDBObject();
    whereQuery.put("title", new BasicDBObject("$regex", "cancer").append("$options", "i"));
    whereQuery.put("title", new BasicDBObject("$regex", "obesity").append("$options", "i"));
    whereQuery.put("date", new BasicDBObject("$regex", "2017").append("$options", "i"));
    whereQuery.put("date", new BasicDBObject("$regex", "2018").append("$options", "i"));
    whereQuery.put("date", new BasicDBObject("$regex", "2019").append("$options", "i"));

    FindIterable<org.bson.Document> cursor = col.find(whereQuery);
    long count = col.count(whereQuery); // count the number of titles
    // query to search for range 2017-2018
    BasicDBObject query =
        new BasicDBObject("Date", new BasicDBObject("$gt", "2017")).append("$lte", "2018");
  }
}
