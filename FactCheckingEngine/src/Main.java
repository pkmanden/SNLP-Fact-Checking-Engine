import java.io.File;
import java.io.FileWriter;

import org.apache.commons.io.*;

import java.util.*;

public class Main {

    static List<String> resultListFormatted = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        CorpusHandler.initalize();
        CategoryHandler.initialize();


        List<String> incorrectFactList = new ArrayList<>();

        int factCount = 0, incorrectFacts = 0;

        /****Test and Train Usecases - true for training; false for testing****/
        boolean isTrainingFile = true;

        String input_filename = new String();
        String result_filename = new String();

        if (isTrainingFile == true) {
            input_filename = "train.tsv";
            result_filename = "results_train.ttl";
        } else {
            input_filename = "test.tsv";
            result_filename = "results.ttl";
        }

        System.out.println("Processing input file...");
        File myFile = new File(input_filename);
        if (!myFile.exists()) {
            System.out.println("File does not exists. Check filename!");
        } else {
            LineIterator myIterator = FileUtils.lineIterator(myFile, "UTF-8");

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
                        System.out.println("Checking " + mySentence);
                        singleFact = preprocessSentence(myFactList.get(1));
                        factScore = singleFact.factCheck();

                        if (isTrainingFile == true) {
                            factCount++;
                            if ((((factScore * 1.0) == 1) && (Double.parseDouble(myFactList.get(2)) == 0)) ||
                                    (((factScore * 1.0) != 1.0) && (Double.parseDouble(myFactList.get(2)) == 1))) {
                                incorrectFacts++;
                                incorrectFactList.add(mySentence);

                                /*Print the facts identified as wrong */
                                System.out.println("\t\t\t Learned Score:" + factScore + "<<<----------");
                            }

                        }

                        addToResultList(myFactList.get(0), factScore);
                    }
                }

            }

            CorpusHandler.writeCorpus();
            System.out.println("\nCreating output file...");
            createOutputFile(result_filename);
            System.out.println("\nDone!");
        }
    }


    /*
     * Function to add the facts into the
     * resultListFormatted List<String>
     */

    static void addToResultList(String factid, Double factScore) {

        String outputString = "<http://swc2017.aksw.org/task2/dataset/" + factid + "><http://swc2017.aksw.org/hasTruthValue>\"" + factScore.toString() + "\"^^<http://www.w3.org/2001/XMLSchema#double> .";
        resultListFormatted.add(outputString);
    }


    /*
     * Function to create output file based on the results
     * added into the ArrayList resultListFormatted
     */

    static void createOutputFile(String resultFile) throws Exception {

        Iterator myIterator = resultListFormatted.iterator();
        FileWriter myFilewriter = new FileWriter(resultFile, false);

        while (myIterator.hasNext()) {
            myFilewriter.write(myIterator.next().toString());
            myFilewriter.write("\n");
        }
        myFilewriter.close();

    }

    /*
     * Function to  format the input sentence mySentence into a
     * standard form, considering the structure of the sentence.
     */

    static SingleFactHandler preprocessSentence(String mySentence) {

        mySentence = mySentence.replace("s'", "s's").replaceAll(".$", "");

        HashMap<String, String> myCategoryHash = categorizeSentence(mySentence);

        String mySplitWord = new String("!!");
        String myFirstPart = new String("!!");
        String mySecondPart = new String("!!");

        String myCategory = myCategoryHash.keySet().iterator().next();
        String myCatchWord = new String();

        if (myCategoryHash.keySet().iterator().next() != "!!") {

            myCatchWord = myCategoryHash.get(myCategory);
            mySplitWord = CategoryHandler.getSplitWord(myCatchWord);

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

            myFirstPart = myFirstPart.replaceAll(" " + myCatchWord, "").replaceAll("'s", "");
            mySecondPart = mySecondPart.replaceAll(" " + myCatchWord, "");

        }

        //   System.out.println(myFirstPart + myCategory + mySecondPart);
        return new SingleFactHandler(myFirstPart, myCategory, mySecondPart);
    }

    /*
     * Function to categorize the given input sentence
     * sentence based on the objects already defined in categoryDefinition
     */

    public static HashMap<String, String> categorizeSentence(String mySentence) {

        String myCategory = new String();
        String myCatchWord = new String();

        Boolean foundflag = new Boolean(false);

        List<String> catchWords = CategoryHandler.getCatchwordList();

        for (String eachString : catchWords) {
            if (mySentence.contains(eachString)) {
                myCategory = CategoryHandler.getCategoryName(eachString);
                foundflag = true;
                myCatchWord = eachString;
                break;
            }
        }

        if (foundflag == false) {
            myCategory = "!!";
        }

        HashMap<String, String> myReturn = new HashMap<>();
        myReturn.put(myCategory, myCatchWord);

        return myReturn;
    }

}