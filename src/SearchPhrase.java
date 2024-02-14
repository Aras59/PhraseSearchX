import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.HashMap;

public class SearchPhrase {

    public HashMap searchPhrase(String xmlpath, String phrase){
        HashMap<String,String> hashNames= new HashMap<String,String>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlpath);
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression exprTitles = xpath.compile("/ksiazki/ksiazka[contains(tresc,'"+phrase+"')]/tytul/text()");
            XPathExpression exprAuthor = xpath.compile("/ksiazki/ksiazka[contains(tresc,'"+phrase+"')]/autor/text()");
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
}
