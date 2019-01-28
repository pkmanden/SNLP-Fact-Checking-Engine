# SNLP-Fact Checking Engine

A corpus-driven fact-checking engine, which returns a confidence value between -1 (fact is false) and +1 (fact is true) given a fact from DBpedia

# Approach
For this Fact Checking Engine, Wikipedia is used as the data source for creation of the corpus used. To generate the fact score of a given fact, the following steps are performed:

## Initialization:
The initialization initiates the data elements required for storage of the corpus, and reads the corpus data from the stored location (if already available).  The corpus consists of facts extracted and stored as FactSubject, FactCategory and FactObject.

The definitions of Categories stored in the form of CategoryName, CategoryCatchWord and CategorySplitWord are also initialized. CategoryName is used to uniquely identify the category of a fact. CategoryCatchWords for a given CategoryName are the list of words which would be used to identify the category a given fact belongs to. Each catchword is also associated with a specific CategorySplitWord, which is used to standardize the form of a given fact in the next step. 

## Fact Checking:
The input file is read, and each fact to be checked is extracted one by one. The given fact is then converted into a standard form of FactSubject: FactCategory: FactObject by analyzing the sentence structure, and the category definitions done above. 

The corpus is generated dynamically, i.e. if there is no information existing in the corpus on the given FactSubject, it is then fetched from the web source (Wikipeda) and all the possibly required facts regarding this FactSubject is updated into the corpus.

If the given input FactObject matches with the object in the corpus under the pair of FactSubject:FactCategory, the fact is determined as TRUE (Fact score 1.0). If the FactObject does not match with the content of corpus under the pair of FactSubject: FactCategory, the fact is determined as FALSE (Fact score -1.0). In the cases where there is no information available in Wikipedia, or there is no required FactCategory information available for the given FactSubject, the fact is treated as UNKOWN (Fact Score 0.0).


## Result & Corpus Storage
Once the complete input file is parsed, the fact scores of the facts are converted into the format required and stored into the file results.ttl (or results_train.ttl in case of train file).

Since with each fact more and more Wikipedia infoboxes are extracted and this information already available in the stored data structures, it is stored into a file for the use during the further run of the program.


# Executing the Project
Run as a simple Java project by running the Main file.
Input file format should be as  
FactID	Fact_Statement  
Result file of the test.tsv file will be updated in the results.ttl file.  

**Note** : Set the variable isTrainingFile in Main.java to “true” or “false” to make the system read data from “train.tsv” or “test.tsv” respectively. By default, the value is set to “false” and the system will output the results of the “test.tsv” file.
