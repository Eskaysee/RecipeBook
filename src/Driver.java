import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

public class Driver {
    public static void main(String[] args) {
        System.out.println(cookAMeal(args[0]));
    }

    private static String cookAMeal(String meal) {
        Recipe recipe;
        recipe = readInXML(meal);
        boolean result = writeInstructions(recipe, meal);
        if (result) {
            return "Recipe written in root directory";
        } else {
            return "Error writing recipe";
        }
    }

    /**
     * WRITES out recipe to .out file in root directory base on inpute
     * @param recipe
     * @param meal
     * */
    private static boolean writeInstructions(Recipe recipe, String meal) {
        if (meal.startsWith("http")) {
            int index = meal.lastIndexOf("/");
            meal = meal.substring(index + 1);
        }
        meal = meal.replace(".xml", ".out");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(meal));
            writer.println("Recipe Name: " + recipe.getName());
            writer.println("Currency: " +  recipe.getCurrency());
            for (Ingredients ingredient : recipe.getIngredients()) {
                if (recipe.getCurrency().equalsIgnoreCase("rands")) {
                    writer.printf("%s: (R%s)\n", ingredient.getName(), ingredient.getPrice());
                } else if (recipe.getCurrency().equalsIgnoreCase("cents")) {
                    writer.printf("%s (%sc)\n", ingredient.getName(), ingredient.getPrice());
                }
            }
            writer.println("Total Ingredients Count: " + recipe.ingredientsCount());
            String mealCost = String.format("%.2f", recipe.calculateCost());
            writer.printf("Total Ingredient Cost in %s: %s", recipe.getCurrency(), mealCost);
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * reads in xml file and returns recipe
     * @param file
     * */
    private static Recipe readInXML(String file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Recipe recipe;
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.newDocumentBuilder();
            Document doc;
            //determine if its a local file or not
            if (file.startsWith("http")) {
                doc = factory.newDocumentBuilder().parse(new URL(file).openStream());
            } else {
                doc = factory.newDocumentBuilder().parse(file);
            }
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
                    priceStr = priceStr.replace(",","");
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