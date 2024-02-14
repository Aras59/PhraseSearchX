import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WriteXml {

    public boolean register(String xmlpath, String login, String password)  {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new FileInputStream(new File(xmlpath)));
            Element root = document.getDocumentElement();
            Element nodeUser = document.createElement("user");
            Element nodeLogin = document.createElement("login");
            Element nodePassword = document.createElement("password");
            Text textLogin = document.createTextNode(login);
            Text textPassword = document.createTextNode(password);
            nodeLogin.appendChild(textLogin);
            nodePassword.appendChild(textPassword);
            nodeUser.appendChild(nodeLogin);
            nodeUser.appendChild(nodePassword);
            root.appendChild(nodeUser);

            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(xmlpath);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.getMessage();
        }
        return true;
    }


}
