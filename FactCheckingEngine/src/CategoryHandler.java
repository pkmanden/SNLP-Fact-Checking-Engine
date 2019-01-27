import java.io.File;
import org.apache.commons.io.*;
import java.util.*;

/**
 *
 * Class to create the handle the categories.
 *
 * Basic functions for category handling are implemented in this class.
 *
 * */

public class CategoryHandler {


    static HashMap <String, HashMap<String,String>> categoryDefinitions = new HashMap<>();
    static List<String> catchWordsList = new ArrayList<>();
    static HashMap<String, String> reverseCategoryDefinition = new HashMap<>();

    public static void addCategory (String categoryName, String categoryCatchWord, String categorySplitWord)
    {
        HashMap <String, String> innerHashMap = new HashMap<>();

        if(categoryDefinitions.containsKey(categoryName))
        {
            innerHashMap = categoryDefinitions.get(categoryName);

        }
        innerHashMap.put(categoryCatchWord, categorySplitWord);
        categoryDefinitions.put(categoryName,innerHashMap);
        catchWordsList.add(categoryCatchWord);
        reverseCategoryDefinition.put(categoryCatchWord, categoryName);
    }

    /* Function to get the list of catchwords */
    public static List<String> getCatchwordList()
    {
        return catchWordsList;
    }

    /* Function to get the category name of  the given catchwords */
    public static String getCategoryName(String catchWord)
    {
        return reverseCategoryDefinition.get(catchWord);
    }

    /* Function to get the split word of  the given catchwords */
    public static String getSplitWord(String catchWord)
    {
        return categoryDefinitions.get(reverseCategoryDefinition.get(catchWord)).get(catchWord);
    }

    /* Function to initialise the category*/
    public static void initialize() {

        CategoryHandler.addCategory("born","nascence place","is");
        CategoryHandler.addCategory("born","birth place","is");
        CategoryHandler.addCategory("born","born","is");
        CategoryHandler.addCategory("died","last place","is");
        CategoryHandler.addCategory("died","death place","is");
        CategoryHandler.addCategory("died","died","is");
        CategoryHandler.addCategory("author","author","is");
        CategoryHandler.addCategory("spouse","spouse","is");
        CategoryHandler.addCategory("spouse","better half","is");
        CategoryHandler.addCategory("subsidiary","subordinate","is");
        CategoryHandler.addCategory("subsidiary","subsidiary","is");
        CategoryHandler.addCategory("award","award","is");
        CategoryHandler.addCategory("award","honour","is");
        CategoryHandler.addCategory("team","team","is");
        CategoryHandler.addCategory("squad","squad","is");
        CategoryHandler.addCategory("starring","stars","stars");
        CategoryHandler.addCategory("starring","starring","is");
        CategoryHandler.addCategory("office","role","is");
        CategoryHandler.addCategory("office","President","is");
        CategoryHandler.addCategory("office","Prime Minister","is");
        CategoryHandler.addCategory("office","office","is");
        CategoryHandler.addCategory("foundation place","foundation place","is");
        CategoryHandler.addCategory("foundation place","innovation place","is");
        CategoryHandler.addCategory("foundation place","founded","is");


    }


}






