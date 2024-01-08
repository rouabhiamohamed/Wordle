package backend;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
;

public class Parser {
    public ArrayList<String> parseWords(String level) {
        //the arraylist where we going to put the extracted words
        ArrayList<String> tableauDeMots = new ArrayList<>();
        try {
            //the url we trying to connect to
            String url = "https://fr.wiktionary.org/wiki/Wiktionnaire:Liste_de_1750_mots_fran%C3%A7ais_les_plus_courants";
            //connecting the url
            Document document = Jsoup.connect(url).get();
     //select the elements we want to from the url and extract the links
            Elements links = document.select("p:contains(Noms) > a[href], p:contains(Adverbes) > a[href], p:contains(Adjectifs) > a[href], p:contains(Verbes) > a[href], p:contains(Prépositions) > a[href]");
          //iterate through the extracted links
            for (Element link : links) {
                //get the text from those links
                String linkText = link.text();
                //the conditions to fill the array depending on the difficulty
                if ((level.equals("f") && linkText.length() == 5) || (level.equals("m") && linkText.length() == 6) || (level.equals("d") && linkText.length() == 8)) {
                    if (!linkText.contains("-") && !linkText.contains(" ") && !linkText.contains("’")) {
                        tableauDeMots.add(linkText);
                    }
                }
            }
            // Print the stack trace if an exception occurs.
        } catch (Exception e) {
            e.printStackTrace();
        }
        // we retrun the arraylist with the wanted extracted words
        return tableauDeMots;
    }

    public ArrayList<String> parseAllWords() {
        ArrayList<String> allWords = new ArrayList<>();

        try {
            String url = "https://fr.wiktionary.org/wiki/Wiktionnaire:Liste_de_1750_mots_fran%C3%A7ais_les_plus_courants";
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("p:contains(Noms) > a[href], p:contains(Adverbes) > a[href], p:contains(Adjectifs) > a[href], p:contains(Verbes) > a[href], p:contains(Prépositions) > a[href]");
            for (Element link : links) {
                String linkText = link.text();
                //we axtract all the possible word the links
                        allWords.add(linkText);
                    }
                }

        catch (Exception e) {
            e.printStackTrace();
        }

        return allWords;
    }

}
