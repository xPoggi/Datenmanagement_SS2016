package xpathexample;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlPath {
	private static String xmlFile = "Musikstore.xml";
	private static String xmlpathFile = "test.xql";

	public static List<String> evalXPath(List<String> xPathExpressions, Document xpathbefehle, XPath xpath) {
		try {
			List<String> resultSet = new LinkedList<>();
			for (String s : xPathExpressions) {
				if(!s.equals("")){
					//System.out.println(s + " => ");
					
					NodeList nodes = (NodeList) xpath.evaluate(s, xpathbefehle, XPathConstants.NODESET);
	
					for (int i = 0; i < nodes.getLength(); i++) {
						StringWriter writer = new StringWriter();
						Transformer trans = TransformerFactory.newInstance().newTransformer();
						trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
						trans.transform(new DOMSource(nodes.item(i)), new StreamResult(writer));
						String xml = writer.toString().replace("\t", "").replace("\r\n", "");
						resultSet.add(xml);
//						System.out.println("- " + xml);
					}
				}
				else{
					System.out.println("End of File.");
				}
			}
			return resultSet;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}		
	}
	
	public static void getUserQuery() {
		
		try {
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Name des Künstlers: ");
		String name = scan.nextLine();

		System.out.println("Name des Genres: ");
		String genre = scan.nextLine();
		
		if (name.length() > 0 && genre.length() > 0) {
			
			String expr = "//titel[kuenstlerID = (//kuenstler[name=\"" + name + "\"]/@kuenstlerID) and @titelID=(//titel_hat_genre[@genre=\"" + genre + "\"]/@titelID)]";

			List<String> list = new LinkedList<>();
			
			list.add(expr);

			Document xmlfile = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			List<String> resultXML = evalXPath(list, xmlfile, xpath);
			
			for(String res : resultXML){

				System.out.println(res);				
				Matcher m = Pattern.compile("titelID=\"([a-z][0-9]+)\"").matcher(res);
				String titelid;
				if (m.find()) {
					titelid = (m.group(0).split("\"")[1]);
					System.out.println(get_Artistname(titelid));
				}
			}			
			
		} else if (name.length()  > 0) {
			
			String expr = "//titel[kuenstlerID = (//kuenstler[name=\"" + name + "\"]/@kuenstlerID)]";
			List<String> list = new LinkedList<>();
			
			list.add(expr);

			Document xmlfile = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			List<String> resultXML = evalXPath(list, xmlfile, xpath);
			
			for(String res : resultXML){

				System.out.println(res);				
				Matcher m = Pattern.compile("titelID=\"([a-z][0-9]+)\"").matcher(res);
				String titelid;
				if (m.find()) {
					titelid = (m.group(0).split("\"")[1]);
					System.out.println(get_Artistname(titelid));
				}
			}			
		} else if (genre.length() > 0) {
			
			String expr = "//titel[@titelID=(//titel_hat_genre[@genre=\"" + genre + "\"]/@titelID)]";
			List<String> list = new LinkedList<>();
			
			list.add(expr);

			Document xmlfile = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			List<String> resultXML = evalXPath(list, xmlfile, xpath);
			
			for(String res : resultXML){

				System.out.println(res);				
				Matcher m = Pattern.compile("titelID=\"([a-z][0-9]+)\"").matcher(res);
				String titelid;
				if (m.find()) {
					titelid = (m.group(0).split("\"")[1]);
					System.out.println(get_Artistname(titelid));
				}
			}			
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static String get_Artistname(String ID){
		try {
		String expr = "//kuenstler[@kuenstlerID = (//titel[@titelID = \"" + ID + "\"]/kuenstlerID)]/name";
		
		List<String> list = new LinkedList<>();
		
		list.add(expr);

		Document xmlfile;
		
			xmlfile = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
			
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		List<String> resultXML = evalXPath(list, xmlfile, xpath);
		
		return resultXML.get(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException, XPathExpressionException, ParserConfigurationException,
			SAXException, TransformerFactoryConfigurationError, TransformerException {
		getUserQuery();		
	}
}
