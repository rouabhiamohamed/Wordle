package backend;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import  java.lang.String;

/*


    Original Author : Mohamed Rouabhia, Refactorer : Nassim Ezzaamari
 */
public class Partie {
    private ArrayList<String> tableauDeMots;

    private String example;
    private int tentative;
    private Scanner scanner;
    private Dico dictionnaire;

    private int[] couleurMot;

    // in order to save the current state of the game
    private int attempts;


    public Partie() {
        tableauDeMots = new ArrayList<>();
        tentative = 0;
        scanner = new Scanner(System.in);
        dictionnaire = new Dico();
        couleurMot = new int[0];
    }

    /*So I've added this function to do the stuff that was done previously on a single big
       function so I could properly use it in front end, anyways this function just sets up the difficulty
       and starts the game
     */
    public void initialization(int Cells)
    {
        String level = levelIntToString(Cells);
        //fill the arraylist of words
        tableauDeMots = parsing(level);
        Random random = new Random();
        //get a random word that we will have to find
        example = mot_random(random);
        couleurMot = new int[example.length()];
        //Display the word to find out, you might want to remove it lol
        System.out.println(example);
    }


    /*
        Because seemingly you enjoy making stuff harder for no reason
        I had to make this function in order to translate the "Cells" count into
        a string...
     */
    public String levelIntToString(int Cells)
    {
        switch (Cells) {
            case 5:
                return "f";
            case 6:
                return "m";
            case 8:
                return "d";
            default:
                return "f";
        }
    }



    //method for playing
    // Nassim Ezzamaari Edit : Changes I've done, so because we need to link this function with the frontend, putting a while
    // loop won't work out, this is why I've added new paramaters in order to save the state of the game
    // therefore the frontend will simply call this function to do the work
    // Besides I also removed the replay stuff since we're just going to destroy and recreate the object anyways
    // Finally, I moved the initialization stuff elsewhere as well
    // Actually, I made it so it returns an int, that way the front end knows what to do...
    // It will return -1 if the word has been refused by the dictionnary
    // It will return 1 if the word was the correct answer
    // It will return -2 if the player lost
    // Otherwise it will return 0
    public int jouer(String mot_entre) {

        // Small fix
        mot_entre = mot_entre.toLowerCase();
        //check if its in the dictionary and the extracted words from the url
        boolean motTrouve = Dictionnaire(mot_entre);
        if (!motTrouve) {
            return -1;
        }

        //increment the attempts only if the word entered have the same length as the word to find
        if (mot_entre.length() == example.length()) {
            attempts++;
        }
        //System.out.println("attempts used so far : " + attempts);
        //condition of win when we find the word
        if (mot_entre.equals(example)) {
            System.out.println("Bravo");
            return 1;
        }
        //if we didnt find the word and the attempts number reached 5
        if (attempts == 5) {
            System.out.println("Perdu");
            return -2;
            //we cant enter word that have more letter than the word to find
        }


        //if the word we enter have the same length of the word to find we fill the array that show the color of each character depending of it posiiton
        if (mot_entre.length() == example.length()) {
            tab_couleur(mot_entre);
        }

        return 0;
    }

//parse the url and extract words depending on the level chosen
    private ArrayList<String> parsing(String level) {
        Parser parser = new Parser();
        return parser.parseWords(level);
    }
//randomize a word from our words array
    public String mot_random(Random random) {
        int min = 0;
        int max = tableauDeMots.size();
        int randomInt = random.nextInt((max - min) + 1) + min;
        return Accent.removeAccents(tableauDeMots.get(randomInt));
    }


//the dictionary where we check if its in the dictionary or from the url we parsed
    public boolean Dictionnaire(String word) {
        Parser parser = new Parser();
        //extract all the words of the links of the url
        ArrayList<String> allWords = parser.parseAllWords();
        for (String mots_dico: dictionnaire.lireFichier()) {
            //check if the word is in the dicitonary or the in arraylist of the parsing
            if (mots_dico.contains(word) || allWords.contains(word)) {
                return true;
            }
        }
        try {
            //the exception if its not in the dictionary
            throw new MotNonTrouveException("Le mot n'est pas dans le dictionnaire.");
        } catch (MotNonTrouveException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
//an array to see wich character of the word enter depending on the word we have to find
    public void tab_couleur(String mot_entre) {
        //initialize the array to 0 each iteration (word we enters) so the array we have an array for each word so we can show the right color
        Arrays.fill(couleurMot, 0);
        //if it is in the right place adn the characters are equals betewen the word enntered and the word to find (green)

        for (int i = 0; i < mot_entre.length(); i++) {
            for (int j = 0; j < example.length(); j++) {
                if (mot_entre.charAt(i) == example.charAt(j) && i == j) {
                    couleurMot[i] = 2;
                    break;
                }
            }
        }
        //if the characters is not in the right place but is present in the word to find and we didnt already found that it it was in its right place (green)
        for (int i = 0; i < mot_entre.length(); i++) {
            for (int j = 0; j < example.length(); j++) {
                if (mot_entre.charAt(i) == example.charAt(j) && i != j && couleurMot[i] != 2 && couleurMot[j] != 2) {
                    couleurMot[i] = 1;
                }
            }
        }
        //we display the array with what we filled in the previous for loops
        for (int i = 0; i < mot_entre.length(); i++) {
            int currentCharColor = couleurMot[i];
            /*
            if (currentCharColor == 1) {
                System.out.println("Orange " + i);
            } else if (currentCharColor == 2) {
                System.out.println("Vert " + i);
            } else {
                System.out.println("Gris " + i);
            }
             */
        }
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public int[] getCouleurMot() {
        return couleurMot;
    }


}





