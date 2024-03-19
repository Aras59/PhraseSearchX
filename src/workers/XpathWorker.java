package workers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XpathWorker {

    private final String userFilePath = "res/users.xml";
    private final String booksFilePath = "res/books.xml";

    public HashMap searchPhrase(String phrase){
        HashMap<String,String> hashNames= new HashMap<String,String>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(booksFilePath);
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression exprTitles = xpath.compile(getExpression("tytul", phrase));
            XPathExpression exprAuthor = xpath.compile(getExpression("autor", phrase));
            NodeList titles = (NodeList) exprTitles.evaluate(document, XPathConstants.NODESET);
            NodeList author = (NodeList) exprAuthor.evaluate(document, XPathConstants.NODESET);
            for (int i = 0,j=0; i < titles.getLength(); i++,j++) {
                if(j>9) break;
                hashNames.put(titles.item(i).getNodeValue(),author.item(i).getNodeValue());
            }
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.getMessage();
        }
        return hashNames;
    }

    private String getExpression (String section, String phrase) {
        return "/ksiazki/ksiazka[contains(tresc,'"+ phrase +"')]/" + section + "/text()";
    }

    public boolean saveUser(String login, String password, LocalDateTime createDate)  {
        String xmlPath = "res/users.xml";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new FileInputStream(new File(xmlPath)));
            Element root = document.getDocumentElement();
            Element nodeUser = document.createElement("user");
            Element nodeLogin = document.createElement("login");
            Element nodePassword = document.createElement("password");
            Element nodeDateTime = document.createElement("createDate");
            Text textLogin = document.createTextNode(login);
            Text textPassword = document.createTextNode(password);
            Text textCreateDate = document.createTextNode(createDate.toString());
            nodeLogin.appendChild(textLogin);
            nodePassword.appendChild(textPassword);
            nodeDateTime.appendChild(textCreateDate);
            nodeUser.appendChild(nodeLogin);
            nodeUser.appendChild(nodePassword);
            nodeUser.appendChild(nodeDateTime);
            root.appendChild(nodeUser);

            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(xmlPath);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.getMessage();
        }
        return true;
    }

    public String getRemindData(String login) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(userFilePath));
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
        return null;
    }

    public boolean checkLoginPassword(String login, String password) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(userFilePath));
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

}
