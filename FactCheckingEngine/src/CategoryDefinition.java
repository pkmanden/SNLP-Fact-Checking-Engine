import java.io.File;
import org.apache.commons.io.*;
import java.util.*;

public class CategoryDefinition {

    HashMap<String, HashMap<String, String>> categoryDefinition = new HashMap<>();

    public void addCategory(String name, String catchword, String splitword) {

        HashMap<String, String> internalMap = new HashMap<>();
        if(categoryDefinition.containsKey(name))
        {
            internalMap = categoryDefinition.get(name);
        }
        internalMap.put(catchword,splitword);


    }

}




