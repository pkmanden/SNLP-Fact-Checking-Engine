import java.io.*;
import java.util.*;

/**
 * Class to handle the corpus storage with the basic functions of fact checking
 * based on the available corpus.
 */

public class CorpusHandler {

	public static HashMap<String, HashMap<String, List<String>>> corpus = new HashMap<>();
	public static HashMap<String, HashMap<String, String>> categoryDefinition = new HashMap<>();

	public CorpusHandler() {
		initialiseCorpus();
		initalizeCategories();
	}

	static boolean containSubject(String mySubject) {
		if (corpus.containsKey(mySubject)) {
			return true;
		}
		return false;
	}

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

	public static void writeCorpus() {

		try {
			File file = new File("corpus.txt");
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(corpus);
			s.close();
		} catch (Exception e) {
			System.out.println("Corpus file cannot be written");
		}
	}

	public static void initialiseCorpus() {
		try {
			File myFile = new File("corpus.txt");
			if(myFile.exists()) {
				FileInputStream myFileInputStream = new FileInputStream(myFile);
				ObjectInputStream myObjectInputStream = new ObjectInputStream(myFileInputStream);
				corpus = (HashMap<String, HashMap<String, List<String>>>) myObjectInputStream.readObject();
				myObjectInputStream.close();
			}
		} catch (Exception e) {
			System.out.println("Corpus file cannot be read");
		}
	}

	public static void initalizeCategories() {
		HashMap<String, String> myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("birth place", "is");
		myCategoryDefinition.put("nascence place", "is");
		categoryDefinition.put("born", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("last place", "is");
		myCategoryDefinition.put("death place", "is");
		categoryDefinition.put("died", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("author", "is");
		categoryDefinition.put("author", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("spouse", "is");
		categoryDefinition.put("spouse", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("subordinate", "is");
		myCategoryDefinition.put("subsidiary", "is");
		categoryDefinition.put("subsidiaries", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("award", "is");
		myCategoryDefinition.put("honour", "is");
		categoryDefinition.put("awards", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("team", "is");
		myCategoryDefinition.put("squad", "is");
		categoryDefinition.put("team", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("stars", "stars");
		myCategoryDefinition.put("cast", "is");
		categoryDefinition.put("starring", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("office", "is");
		myCategoryDefinition.put("role", "is");
		myCategoryDefinition.put("President", "is");
		myCategoryDefinition.put("Prime Minister", "is");
		categoryDefinition.put("office", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("foundation place", "is");
		categoryDefinition.put("Founded", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("office", "is");
		myCategoryDefinition.put("role", "is");
		myCategoryDefinition.put("president", "is");
		myCategoryDefinition.put("primeminister", "is");
		categoryDefinition.put("President", myCategoryDefinition);

		myCategoryDefinition = new HashMap<>();
		myCategoryDefinition.put("office", "is");
		myCategoryDefinition.put("role", "is");
		myCategoryDefinition.put("president", "is");
		myCategoryDefinition.put("primeminister", "is");
		categoryDefinition.put("Primeminister", myCategoryDefinition);

	}

}
