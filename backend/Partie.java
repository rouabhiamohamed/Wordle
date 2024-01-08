package backend;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import  java.lang.String;
public class Partie {
    private ArrayList<String> tableauDeMots;
    private String example;
    private int tentative;
    private Scanner scanner;
    private Dico dictionnaire;
    private int[] couleurMot;


    public Partie() {
        tableauDeMots = new ArrayList<>();
        tentative = 0;
        scanner = new Scanner(System.in);
        dictionnaire = new Dico();
        couleurMot = new int[0];
    }
     //method for playing
    public void jouer() {
        String level;
        boolean rejouer = true;
        while (rejouer) {
            //difficulty choice
            level = difficulte();
            System.out.println("Vous avez choisi la difficulté : " + level);
            //fill the arraylist of words
            tableauDeMots = parsing(level);
            Random random = new Random();
            //get a random word that we will have to find
            example = mot_random(random);
            couleurMot = new int[example.length()];
            System.out.println(example);
            while (true) {
                //the word that we enter
                String mot_entre = entrer_mot();
                //check if its in the dictionary and the extracted words from the url
                boolean motTrouve = Dictionnaire(mot_entre);

                if (!motTrouve ) {

                    continue;
                }
      //increment the attempts only if the word entered have the same length as the word to find
                if (mot_entre.length() == example.length()) {
                    tentative++;
                }
                System.out.println(tentative);
                //condition of win when we find the word
                if (mot_entre.equals(example)) {
                    System.out.println("Bravo");
                    break;
                }
                //if we didnt find the word and the attempts number reached 5
                if (tentative == 5) {
                    System.out.println("Perdu");
                    break;
                    //we cant enter word that have more letter than the word to find
                } else if (mot_entre.length() != example.length()) {
                    System.out.println("Entrez un mot avec le même nombre de lettres.");
                }
                //if the word we enter have the same length of the word to find we fill the array that show the color of each character depending of it posiiton
                if (mot_entre.length() == example.length()) {
                    tab_couleur(mot_entre);
                }
            }
            //replaying
            System.out.println("Voulez-vous rejouer (y/n)");
            String choix = scanner.next();
            if (choix.equalsIgnoreCase("n")) {
                System.out.println("au revoir");
                rejouer = false;
            }
        }
    }

//cchose the difficulty
    public String difficulte() {
        while (true) {
            System.out.println("Choisissez la difficulté :");
            System.out.println("Facile pour un mot de 5 lettres (appuyez sur 'f')");
            System.out.println("Moyen pour un mot de 6 lettres (appuyez sur 'm')");
            System.out.println("Difficile pour un mot de 8 lettres (appuyez sur 'd')");
            String level = scanner.next();
            if (level.equals("f") || level.equals("m") || level.equals("d")) {
                return level;
            } else {
                System.out.println("Choix invalide. Veuillez choisir 'f', 'm' ou 'd'.");
            }
        }
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
//to enter the word
    public String entrer_mot() {
        System.out.println("Entrer votre mot : ");
        return scanner.next();
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
            if (currentCharColor == 1) {
                System.out.println("Orange " + i);
            } else if (currentCharColor == 2) {
                System.out.println("Vert " + i);
            } else {
                System.out.println("Gris " + i);
            }
        }
    }
}





