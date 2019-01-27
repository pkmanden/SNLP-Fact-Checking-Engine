import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.*;
import java.util.*;

public class Main {

    static List<String> resultListFormatted = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        CorpusHandler corpus = new CorpusHandler();
        List<String> incorrectFactList = new ArrayList<>();

        CorpusHandler.initialiseCorpus();

        int factCount = 0, incorrectFacts = 0;

        System.out.println("Processing input file..");
        File myFile = new File("train.tsv");
        LineIterator myIterator = FileUtils.lineIterator(myFile, "UTF-8");

        /*Check if file is a training file (TRUE) or testing file (FALSE) */
        boolean isTrainingFile = CheckFileType(myFile);

        if (isTrainingFile) {
            System.out.println("File identified as training file");
        }

        while (myIterator.hasNext()) {
            String mySentence = myIterator.next();

            /* Ignore empty sentences in the input files */
            if (!mySentence.isEmpty()) {

                List<String> myFactList = Arrays.asList(mySentence.split("\t"));
                SingleFactHandler singleFact;

                /*
                 Expected inputs formats
                 FactId -- Fact Statement-- FactScore (Training document)
                 FactId -- Fact Statement (Test document)
                 */

                if ((myFactList.get(0).matches("[0-9]+") && myFactList.size() > 1)) {

                    double factScore;
                    singleFact = PreprocessSentence(myFactList.get(1));
                    factScore = singleFact.factCheck();

                    if (isTrainingFile == true) {
                        factCount++;
                        if ((factScore * 1.0) != Double.parseDouble(myFactList.get(2))) {
                            incorrectFacts++;
                            incorrectFactList.add(mySentence);

                            /*Todo: Print the facts identified as wrong */
                            System.out.println("Wrong fact" + mySentence);
                        }

                    }

                    AddToResultList(myFactList.get(0), factScore);
                }
            }

        }

        CorpusHandler.writeCorpus();

        CreateOutputFile();
        System.out.println("Done!");
    } 


    /**
     *
     * Function to add the facts into the
     * resultListFormatted List<String>
     */

    static void AddToResultList(String factid, Double factScore) {

        String outputString = "<http://swc2017.aksw.org/task2/dataset/" + factid + "><http://swc2017.aksw.org/hasTruthValue>\"" + factScore.toString() + "\"^^<http://www.w3.org/2001/XMLSchema#double> .";
        resultListFormatted.add(outputString);
    }


    /**
     * Function to create output file based on the results
     * added into the ArrayList resultListFormatted
     */

    static void CreateOutputFile() throws Exception {

    	System.out.println("Creating output file..");
        Iterator myIterator = resultListFormatted.iterator();
        FileWriter myFilewriter = new FileWriter("result_train.ttl");

        while(myIterator.hasNext())
        {
            myFilewriter.write(myIterator.next().toString());
            myFilewriter.write("\n");
        }
        myFilewriter.close();

    }

    /**
     * Function to  format the input sentence mySentence into a
     * standard form, considering the structure of the sentence.
     */

    static SingleFactHandler PreprocessSentence(String mySentence) {

        mySentence = mySentence.replace("s'", "s's").replaceAll(".$", "");

        HashMap<String, HashMap<String, String>> myCategoryDefinition = CategorizeSentence(mySentence);

        String myCategory, mySplitWord, myReplaceWord = new String();
        String myFirstPart = new String();
        String mySecondPart = new String();

        /*TODO Check category definition again*/
        myCategory = myCategoryDefinition.keySet().iterator().next();

        if (myCategory != "!!") {

            HashMap<String, String> myCategoryInstance = (myCategoryDefinition.get(myCategory));

            myReplaceWord = myCategoryInstance.keySet().iterator().next();
            mySplitWord = myCategoryInstance.get(myReplaceWord);

            List<String> mySplitText = Arrays.asList(mySentence.split(" " + mySplitWord + " "));


            if (mySplitText.get(0).contains("'s")) {
                myFirstPart = mySplitText.get(0);
                mySecondPart = mySplitText.get(1);

            } else if (mySplitText.get(1).contains("'s")) {
                myFirstPart = mySplitText.get(1);
                mySecondPart = mySplitText.get(0);
            } else {
                myFirstPart = mySplitText.get(0);
                mySecondPart = mySplitText.get(1);

            }

            //*TODO: Replace world to be checked  */
            myFirstPart = myFirstPart.replaceAll(" " + myReplaceWord, "").replaceAll("'s", "");
            mySecondPart = mySecondPart.replaceAll(" " + myReplaceWord, "");

        }

        return new SingleFactHandler(myFirstPart, myCategory, mySecondPart);
    }

    /**
     * Function to categorize the given input sentence
     * sentence based on the objects already defined in categoryDefinition
     */

    public static HashMap<String, HashMap<String, String>> CategorizeSentence(String mySentence) {

        String myCategory = new String();
        String mySplitWord = new String();
        String myReplaceWorld = new String();

        Boolean foundflag = new Boolean(false);

        for (String outerIterator : CorpusHandler.categoryDefinition.keySet()) {
            HashMap<String, String> innerVariable = CorpusHandler.categoryDefinition.get(outerIterator);

            for (String innerIterator : innerVariable.keySet()) {

                if (mySentence.contains(innerIterator)) {
                    myCategory = outerIterator;
                    myReplaceWorld = innerIterator;
                    mySplitWord = innerVariable.get(innerIterator);
                    foundflag = true;
                    break;
                }

            }
            if (foundflag == true) {
                break;
            }
        }


        if (foundflag == false) {
            myCategory = "!!";
            myReplaceWorld = null;
            mySplitWord = "is";
        }


        HashMap<String, HashMap<String, String>> myReturnData = new HashMap<>();
        HashMap<String, String> myInnerReturnData = new HashMap<>();

        myInnerReturnData.put(myReplaceWorld, mySplitWord);
        myReturnData.put(myCategory, myInnerReturnData);

        return myReturnData;
    }

    /**
     * Function to find out the type of the input file.
     * This is used to handle the two different use cases of test file and train file reading
     */
    static boolean CheckFileType(File filename) throws Exception {
        LineIterator myIterator = FileUtils.lineIterator(filename, "UTF-8");
        if (myIterator.next().split("/t").length > 2) {
            return true;
        } else {
            return false;
        }
    }
}