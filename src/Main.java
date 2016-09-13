import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Main entry point.
 * Imports configuration parameters from config file and starts server.
 */
public class Main {
    private static final String CONFIG_FILE = "config.xml";

    public static void main(String[] args) {
        // parse config file
        File fXmlFile = new File(CONFIG_FILE);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Cannot parse config file");
            e.printStackTrace();
            return;
        }
        doc.getDocumentElement().normalize();
        String port = doc.getElementsByTagName("port").item(0)
                .getTextContent();
        String rootDir = doc.getElementsByTagName("rootDir").item(0)
                .getTextContent();
        String threadPoolSize = doc.getElementsByTagName("threadPoolSize").item(0)
                .getTextContent();

        // start server
        Server server = new Server(Integer.parseInt(port),
                rootDir, Integer.parseInt(threadPoolSize));
        server.start();
    }
}
