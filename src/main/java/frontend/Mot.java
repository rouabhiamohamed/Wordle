package frontend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Mot {


    /*
        I changed somewhat this function so it can properly behave with the frontend
        cause there's no way in hell I'm doing multithreading for this project so both codes
        works without much changes...
     */
    public static void backEndGameLoop(String level)
    {
        ArrayList<String> tableauDeMots = new ArrayList<>();
        int count = 0;


        // Would it kill you to write what the fuck is this part of code supposed to do ?
        //Besides why it's not a function ??????
        try {
            String url = "https://fr.wiktionary.org/wiki/Wiktionnaire:Liste_de_1750_mots_fran%C3%A7ais_les_plus_courants";
            Document document = Jsoup.connect(url).get();
            Elements links = document.select(" p:contains(Noms) > a[href], p:contains(Adverbes) > a[href], p:contains(Adjectifs) > a[href] ,p:contains(Verbes) > a[href],p:contains(Prépositions) > a[href]");

            for (Element link : links) {

                String linkText = link.text();
                if (level.equals("f") && linkText.length() == 5 || level.equals("m") && linkText.length() == 6 || level.equals("d") && linkText.length() == 8) {
                    tableauDeMots.add(linkText);
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] tableauFinal = tableauDeMots.toArray(new String[0]);

        System.out.println(count);
        Random random = new Random();
        int min = 0;  // Lower bound
        int max = tableauFinal.length;
        int randomInt = random.nextInt((max - min) + 1) + min;

        String example = tableauFinal[randomInt];
        System.out.println(example);

        int[] couleurMot = new int[example.length()];
        int tentative = 0;
        Scanner mot = new Scanner(System.in);
        String mot_entre;
        while (true) {

            System.out.println("entrer votre mot");

            mot_entre = mot.nextLine();

         if (mot_entre.length()==example.length()){
            tentative++;}
            System.out.println(tentative);
            if(mot_entre.equals(example)) {
                System.out.println("bravo");
                break;
            }

           if (tentative == 5) {
                System.out.println("perdu");
                break;
            }
           else if (mot_entre.length() != example.length()) {

                System.out.println("entrez un mot evc le meme nombre de lettre");

           }

            if (mot_entre.length() == example.length()) {
            for (int i = 0; i <= mot_entre.length() - 1; i++) {
                for (int j = 0; j <= example.length() - 1; j++) {


                    //vert
                    if (mot_entre.charAt(i) == example.charAt(j) && i == j) {
                        couleurMot[i] = 2;

                        break;
                    }

                }
            }
            for (int i = 0; i <= mot_entre.length() - 1; i++) {
                for (int j = 0; j <= example.length() - 1; j++) {
                    //vert
                    if (mot_entre.charAt(i) == example.charAt(j) && i != j && couleurMot[i] != 2 && couleurMot[j] != 2) {
                        couleurMot[i] = 1;
                    }

                }
            }
            for (int i = 0; i <= mot_entre.length() - 1; i++) {
                int currentCharColor = couleurMot[i];
                if (currentCharColor == 1) {
                    System.out.println("orange" + i);
                } else if (currentCharColor == 2) {
                    System.out.println("vert" + i);
                } else {
                    System.out.println("gris" + i);
                }

            }
        }

    }


    }



}
