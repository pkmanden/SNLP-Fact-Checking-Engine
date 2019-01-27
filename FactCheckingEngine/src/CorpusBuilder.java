import java.util.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * Class to create the build the corpus. An object is created if
 * the subject of the fact cannot be found in the corpus already.
 *
 * Connection to the web source (Wikipedia),and extraction and formatting
 * of the information from the infobox is done by functions inside this class.
 *
 * */

public class CorpusBuilder {

    public CorpusBuilder(String mySubjectRaw)  {

        String mySubject = new String();
        mySubject = mySubjectRaw.replaceAll(" ", "_");
        String myURL = new String("https://en.wikipedia.org/wiki/"+mySubject);
        Connection.Response res;
        try {
            res = Jsoup.connect(myURL).execute();


        String html = res.body();
        Document doc2 = Jsoup.parseBodyFragment(html);
        Element body = doc2.body();
        Elements tables = body.getElementsByTag("table");
        for (Element table : tables) {
            if (table.className().contains("infobox")==true) {

                Elements elements = table.getElementsByTag("tr");
                for(Element element : elements)
                {

                    for(String catchWord: CategoryHandler.catchWordsList)
                    {

                        if((element.text().toLowerCase()).contains(catchWord.toLowerCase()))
                        {

                            HashMap<String, List<String>> innerMap = new HashMap<>();
                            List<String> factString = new ArrayList<String>();
                            if(CorpusHandler.corpus.containsKey(mySubjectRaw))
                            {
                                innerMap = CorpusHandler.corpus.get(mySubjectRaw);
                                if(innerMap.containsKey(CategoryHandler.getCategoryName(catchWord)))
                                {
                                    factString= innerMap.get(CategoryHandler.getCategoryName(catchWord));
                                }
                            }
                            factString.add(element.text());
                            innerMap.put(CategoryHandler.getCategoryName(catchWord),factString);
                            CorpusHandler.corpus.put(mySubjectRaw, innerMap);
                        }
                    }

                }

                break;
            }
        } } catch (Exception e)
        {
            /* Exception for subject not found in Wikipedia.
             * Handled inside CorpusHandler by returning score as 0.
            */
        }

    }
}
