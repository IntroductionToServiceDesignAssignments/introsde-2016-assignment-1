package healthprofile2;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.reader.gbind.Element;


// class used to parse and query the document people.xml

public class HealthProfile {
	 Document doc;
	 XPath xpath;
	

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		
		//load database
		HealthProfile db = new HealthProfile();
        db.loadXML();
		   
		
			 //display all people details
				NodeList nodelist = db.getAllPeople();
				System.out.println("print all the people informations in the db");
				for (int i=0; i<nodelist.getLength();i++){
					int j;
					for (j=1; j<nodelist.item(i).getChildNodes().getLength(); j=j+2){
						if (j==7){
							System.out.println(nodelist.item(i).getChildNodes().item(j).getNodeName());
							for (int k=1;k<nodelist.item(i).getChildNodes().item(j).getChildNodes().getLength(); k=k+2){
							System.out.println(nodelist.item(i).getChildNodes().item(j).getChildNodes().item(k).getNodeName()+"  "+nodelist.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent()
							);}
							
						}else{
				  System.out.println(nodelist.item(i).getChildNodes().item(j).getNodeName()+"  "+nodelist.item(i).getChildNodes().item(j).getTextContent());					
						}}
					  System.out.println("");
					
				}
			//display health profile 
				String id="2";
				Node node = db.getHealthByID(id);
				System.out.println("\nprint the health profile of the person with id =5");
				for (int i=1; i<node.getChildNodes().getLength(); i=i+2){
				System.out.println(node.getChildNodes().item(i).getNodeName()+" "+node.getChildNodes().item(i).getTextContent());
				
				}
			//display all the people with with weight > 90
				String minweight="90";
				nodelist = db.getPeopleAccordingWeight(minweight,">");
				System.out.println("\nprint all the people informations on the db with weight>90");
				for (int i=0; i<nodelist.getLength();i++){
					int j;
					for (j=1; j<nodelist.item(i).getChildNodes().getLength(); j=j+2){
						if (j==7){
							System.out.println(nodelist.item(i).getChildNodes().item(j).getNodeName());
							for (int k=1;k<nodelist.item(i).getChildNodes().item(j).getChildNodes().getLength(); k=k+2){
							System.out.println(nodelist.item(i).getChildNodes().item(j).getChildNodes().item(k).getNodeName()+"  "+nodelist.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent()
							);}
							
						}else{
				  System.out.println(nodelist.item(i).getChildNodes().item(j).getNodeName()+"  "+nodelist.item(i).getChildNodes().item(j).getTextContent());					
						}}
					 System.out.println("");
				}
		}
				
	
	//function that load people.xml
	  public void loadXML() throws ParserConfigurationException, SAXException, IOException {

	        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        domFactory.setNamespaceAware(true);
	        DocumentBuilder builder = domFactory.newDocumentBuilder();
	        doc = builder.parse("people.xml");
	        
	        //creating xpath object
	        getXPathObj();
	    }
	  
	  //get xpath object
	  public XPath getXPathObj() {

	        XPathFactory factory = XPathFactory.newInstance();
	        xpath = factory.newXPath();
	        return xpath;
	    }
	  
	//get the weight by the id and returns the correspondent node
	  public Node getWeightByID(String id) throws XPathExpressionException {
	        XPathExpression expr = xpath.compile("/people/peopleList/person[@id='"+id+"']/healthprofile/weight");
	        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
	        return node;
	    }
	  
	  //get the Height by the id and returns the correspondent node
	  public Node getHeightByID(String id) throws XPathExpressionException {
	        XPathExpression expr = xpath.compile("/people/peopleList/person[@id='"+id+"']/healthprofile/height");
	        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
	        return node;
	    }
	  //get the Health profile by the id and returns the correspondent node
	  public Node getHealthByID(String id) throws XPathExpressionException {
	        XPathExpression expr = xpath.compile("/people/peopleList/person[@id='"+id+"']//healthprofile");
	        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
	        return node;
	    }
	//get all the people in the database
	  public NodeList getAllPeople() throws XPathExpressionException{
		  XPathExpression expr = xpath.compile("//person");
	      NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	      return nodes;}
	  //get all the people according to an operator and the weight
	  public NodeList getPeopleAccordingWeight(String weight,String operator)
			  throws XPathExpressionException{
		  XPathExpression expr = xpath.compile(
				  "/people/peopleList/person[healthprofile/weight"+operator+weight+"]");
	      NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	      return nodes;
		 }
	  

}