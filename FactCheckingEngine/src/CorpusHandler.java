import java.io.*;
import java.util.*;

/**
 * Class to handle the corpus storage with the basic functions
 * of fact checking based on the available corpus.
 */

public class CorpusHandler {

    public static HashMap<String, HashMap<String, List<String>>> corpus = new HashMap<>();

    /* Function initalize the corpus i.e read corpus from the stored file if file is present*/
    public static void initalize() {
        try {
            File myFile = new File("corpus.txt");
            FileInputStream myFileInputStream = new FileInputStream(myFile);
            ObjectInputStream myObjectInputStream = new ObjectInputStream(myFileInputStream);
            corpus = (HashMap<String, HashMap<String, List<String>>>) myObjectInputStream.readObject();
            myObjectInputStream.close();
        } catch (Exception e) {
            System.out.println("Corpus file cannot be read");
        }
    }

    /* Function to check if the data for given subject is already extracted */
    static boolean containSubject(String mySubject) {
        if (corpus.containsKey(mySubject)) {
            return true;
        }
        return false;
    }

    /* Function to check the fact score of the given fact */
    public static double checkFact(String mySubject, String myClassification, String myFact) {
        double factScore = 0;

        if (containSubject(mySubject) == false) {
            CorpusBuilder newSubject = new CorpusBuilder(mySubject);
        }


        if (corpus.containsKey(mySubject)) {
            HashMap<String, List<String>> subjectFactMap = corpus.get(mySubject);

            List<String> subjectFactList = new ArrayList<>();
            if (subjectFactMap.containsKey(myClassification)) {
                subjectFactList = subjectFactMap.get(myClassification);
            }
            factScore = compareContents(myFact, subjectFactList);
        } else {
            factScore = 0;
        }

        return factScore;
    }

    /* Function to compare the contents of the given fact with the factlist extracted from Wikipedia */
    static double compareContents(String fact, List<String> factList) {
        double score = -1.0;

        for (String factFromList : factList) {

            if (factList.contains(fact)) {
                score = 1.0;
            } else {
                String baseFact = fact.replaceAll("[.,]", "");
                String baseFactList = factFromList.replaceAll("[.,]", "");
                if (baseFactList.contains(baseFact)) {
                    score = 1.0;
                }
            }
        }

        return score;
    }

    /* Function to write the corpus hashmap to a file for storage after program run */
    public static void writeCorpus() {

        try {
            File myFile = new File("corpus.txt");
            FileOutputStream myFileOutputStream = new FileOutputStream(myFile, false);
            ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
            myObjectOutputStream.writeObject(corpus);
            myObjectOutputStream.close();
        } catch (Exception e) {
            System.out.println("Corpus file could not be written");
        }

    }
}
