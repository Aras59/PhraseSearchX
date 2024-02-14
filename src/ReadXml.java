import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ReadXml {
    public boolean compareLP(String xmlpath,String login,String password){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlpath));
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("user");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String l = eElement.getElementsByTagName("login").item(0).getTextContent();
                    String p=eElement.getElementsByTagName("password").item(0).getTextContent();
                    if(l.equals(login)&&p.equals(password)) {
                        return true;
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.getMessage();
        }
        
        return false;
    }

    public String getPassword(String xmlpath,String login){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlpath));
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("user");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String l = eElement.getElementsByTagName("login").item(0).getTextContent();
                    String p = eElement.getElementsByTagName("password").item(0).getTextContent();
                    if(l.equals(login)) {
                        return p;
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.getMessage();
        }
        return "";
    }
}
