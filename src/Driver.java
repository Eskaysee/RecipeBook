import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Driver {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
        cookAMeal(args[0]);
    }

    private static String cookAMeal(String meal) {
        Recipe recipe = readInXML(meal);
        return "";
    }

    private static Recipe readInXML(String file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Recipe recipe;
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.newDocumentBuilder();
            Document doc = factory.newDocumentBuilder().parse(file);
            doc.getDocumentElement().normalize();
            String recipeName = doc.getDocumentElement().getAttribute("name");
            String currency = doc.getDocumentElement().getAttribute("currency");
            recipe = new Recipe(recipeName, currency);
            NodeList ingredients = doc.getElementsByTagName("ingredient");
            for (int i = 0; i < ingredients.getLength(); i++) {
                Node node = ingredients.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element ingredientElement = (Element) node;
                    String ingredientName = ingredientElement.getElementsByTagName("name").item(0).getTextContent();
                    String priceStr = ingredientElement.getElementsByTagName("price").item(0).getTextContent();
                    double price = Double.parseDouble(priceStr);
                    recipe.addIngredient(new Ingredients(ingredientName, price));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        return recipe;
    }
}