/**
 *
 * Class representing a single fact, with elements to store subject,
 * category and object.
 * If not initialised with any specific inputs, object is created with default values
 * of "!!".
 * Functions required for handling a single fact are encapsulated in this class.
 *
 * */

public class SingleFactHandler {
    String factSubject = new String();
    String factCategory = new String();
    String factObject = new String();

    double factScore = 0;

    /* Constructor with inputs provided */
    public SingleFactHandler(String a, String b, String c) {
        factSubject = a;
        factCategory = b;
        factObject = c;
    }


    /* Constructor with no inputs provided */
    public SingleFactHandler() {
        factSubject = "!!";
        factCategory = "!!";
        factObject = "!!";
    }

    /*Function to check if the fact is empty */
    boolean isEmpty() {
        return (factSubject == null);

    }

    /*Function to check the status of given fact */
    public double factCheck()  {
        factScore = 0;
        factScore = CorpusHandler.checkFact(factSubject, factCategory, factObject);
        return factScore;
    }

}